package com.pao.proiect.elearning;

import com.pao.proiect.elearning.exception.InvalidDataException;
import com.pao.proiect.elearning.exception.PlatformStateException;
import com.pao.proiect.elearning.model.Categorie;
import com.pao.proiect.elearning.model.Certificat;
import com.pao.proiect.elearning.model.Curs;
import com.pao.proiect.elearning.model.Instructor;
import com.pao.proiect.elearning.model.Lectie;
import com.pao.proiect.elearning.model.Quiz;
import com.pao.proiect.elearning.model.Student;
import com.pao.proiect.elearning.model.Tema;
import com.pao.proiect.elearning.model.Utilizator;
import com.pao.proiect.elearning.repository.CategorieRepository;
import com.pao.proiect.elearning.repository.CursRepository;
import com.pao.proiect.elearning.repository.InstructorRepository;
import com.pao.proiect.elearning.repository.StudentRepository;
import com.pao.proiect.elearning.service.AuditService;
import com.pao.proiect.elearning.service.CursService;
import com.pao.proiect.elearning.service.PlatformService;
import com.pao.proiect.elearning.service.UtilizatorService;
import com.pao.proiect.elearning.util.DatabaseConnection;
import com.pao.proiect.elearning.util.SchemaInitializer;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws Exception {
        AuditService audit = AuditService.getInstance();
        UtilizatorService utilizatori = UtilizatorService.getInstance();
        CursService platforma = CursService.getInstance();

        Categorie catJava = new Categorie(1, "Programare");
        Instructor prof = utilizatori.creareContInstructor("prof.ion", "ion@univ.ro", "parola123", "Informatica");
        Student stud = utilizatori.creareContStudent("maria.s", "maria@student.ro", "secret", "Grupa 232");
        audit.log("creare_cont_utilizator");

        System.out.println("Conturi");
        System.out.println(prof);
        System.out.println(stud);

        System.out.println("\nCRUD UtilizatorService: listeaza toti ");
        utilizatori.listeazaTotiUtilizatorii().forEach(u -> System.out.println(u.getUsername() + " (" + u.getRol() + ")"));

        System.out.println("\nTratare exceptie: username duplicat");
        try {
            utilizatori.creareContStudent("maria.s", "alt@mail.ro", "x", "Gr242");
        } catch (InvalidDataException e) {
            System.out.println("Prins InvalidDataException: " + e.getMessage());
        }

        System.out.println("\nLogin");
        Optional<Utilizator> sesiune = utilizatori.login("maria.s", "secret");
        sesiune.ifPresent(u -> System.out.println("Autentificat: " + u.getUsername()));
        audit.log("login_utilizator");

        Curs cursPaoj = platforma.adaugaCurs("Programare avansata obiecte Java", catJava, prof);
        platforma.adaugaCurs("Structuri de date", catJava, prof);
        audit.log("adaugare_curs");

        System.out.println("\nToate cursurile ");
        platforma.listeazaToateCursurile().forEach(c -> System.out.println(c.getTitlu()));
        audit.log("afisare_cursuri");

        System.out.println("\nCursuri sortate dupa titlu");
        platforma.listeazaCursurileSortateDupaTitlu().forEach(c -> System.out.println(c.getTitlu()));

        System.out.println("\nCautare curs dupa titlu");
        platforma.cautaCursDupaTitlu("Structuri de date")
                .ifPresentOrElse(c -> System.out.println("Gasit: " + c), () -> System.out.println("Lipsa"));
        audit.log("cautare_curs_titlu");

        System.out.println("\nCautare curs dupa id");
        platforma.cautaCursDupaId(cursPaoj.getId()).ifPresent(c -> System.out.println("ID " + c.getId() + ": " + c.getTitlu()));

        platforma.inscrieStudentLaCurs(stud.getId(), cursPaoj.getId());

        Lectie l1 = platforma.adaugaLectieLaCurs(cursPaoj.getId(), "Mostenire si polimorfism", "Note...", 1);
        System.out.println("\nLectie adaugata");
        System.out.println(l1);
        audit.log("adaugare_lectie");

        Quiz quiz = platforma.adaugaQuizLaCurs(cursPaoj.getId(), "Quiz 1 - PAOJ");
        platforma.adaugaIntrebareLaQuiz(
                cursPaoj.getId(),
                quiz.getId(),
                "Ce cuvant cheie folosesti pentru mostenire in Java?",
                List.of("implements", "extends", "inherit", "super"),
                1);
        audit.log("adaugare_quiz");

        double scor = platforma.rezolvaQuiz(cursPaoj.getId(), quiz.getId(), List.of(1));
        System.out.println("\nScor quiz");
        System.out.println("Scor: " + scor);
        audit.log("rezolvare_quiz");

        Tema tema = platforma.adaugaTemaLaCurs(
                cursPaoj.getId(),
                "Tema 1",
                "Implementati o ierarhie de clase.",
                LocalDate.now().plusWeeks(2));
        System.out.println("\nSubmisie");
        System.out.println(platforma.trimiteSubmisie(stud.getId(), tema.getId(), "Vezi atasamentul logic."));

        System.out.println("\nTratare exceptie: certificat fara inscriere");
        Student altStudent = utilizatori.creareContStudent("gigel.s", "gigel@student.ro", "pwd", "Gr223");
        try {
            platforma.genereazaCertificatFinalizareCurs(altStudent.getId(), cursPaoj.getId());
        } catch (PlatformStateException e) {
            System.out.println("Prins PlatformStateException: " + e.getMessage());
        }

        Certificat cert = platforma.genereazaCertificatFinalizareCurs(stud.getId(), cursPaoj.getId());
        System.out.println("\nCertificat (imutabil, fara setteri)");
        System.out.println(cert);
        audit.log("generare_certificat");

        System.out.println("\nCursuri la care e inscris studentul");
        platforma.afiseazaCursurileStudentului(stud.getId()).forEach(c -> System.out.println(c.getTitlu()));
        audit.log("cursuri_student");

        System.out.println("\nStudenti inscrisi la curs");
        platforma.afiseazaStudentiiLaCurs(cursPaoj.getId()).forEach(s -> System.out.println(s.getUsername()));
        audit.log("studenti_la_curs");

        System.out.println("\nCRUD CursService: sterge quiz gol");
        Quiz qTemp = platforma.adaugaQuizLaCurs(cursPaoj.getId(), "Quiz temporar");
        platforma.stergeQuizLaCurs(cursPaoj.getId(), qTemp.getId());
        System.out.println("Quiz temporar sters.");

        System.out.println("\nCRUD UtilizatorService: cauta dupa id + sterge utilizator temporar");
        utilizatori.cautaDupaId(altStudent.getId()).ifPresent(u -> System.out.println("Inainte de stergere: " + u.getUsername()));
        utilizatori.stergeUtilizator(altStudent.getId());
        utilizatori.cautaDupaId(altStudent.getId()).ifPresentOrElse(
                u -> System.out.println("Eroare: inca exista"),
                () -> System.out.println("Utilizator sters: gigel.s nu mai exista in sistem."));

        System.out.println("\nCursService: sterge curs secundar dupa titlu");
        platforma.cautaCursDupaTitlu("Structuri de date").ifPresent(c -> platforma.stergeCurs(c.getId()));
        System.out.println("Cursuri ramase: " + platforma.listeazaToateCursurile().size());

        System.out.println("\nEtapa II JDBC");
        Connection conn = DatabaseConnection.getInstance().getConnection();
        SchemaInitializer.init(conn);

        CategorieRepository categorieRepo = new CategorieRepository();
        InstructorRepository instructorRepo = new InstructorRepository();
        StudentRepository studentRepo = new StudentRepository();
        CursRepository cursRepo = new CursRepository();
        PlatformService db = PlatformService.getInstance();

        Categorie catDb = new Categorie(0, "Baze de date");
        categorieRepo.save(catDb);

        Instructor profDb = new Instructor(0, "db.prof", "db@univ.ro", "pass", "Informatica");
        instructorRepo.save(profDb);

        Student studDb = new Student(0, "ana.db", "ana@student.ro", "secret", "Gr242");
        studentRepo.save(studDb);

        Curs cursDb = new Curs(0, "SQL pentru incepatori", catDb, profDb);
        cursRepo.save(cursDb);
        System.out.println("Salvat in BD: " + cursDb);

        cursRepo.findById(cursDb.getId()).ifPresent(c -> System.out.println("Gasit: " + c.getTitlu()));

        db.inscrieStudentLaCurs(studDb.getId(), cursDb.getId());

        try {
            db.inscrieStudentLaCurs(studDb.getId(), cursDb.getId());
        } catch (Exception e) {
            System.out.println("A doua inscriere respinsa (corect).");
        }

        System.out.println("\nInscrieri active:");
        db.getInscrieriActiveCuDetalii().forEach(System.out::println);

        System.out.println("\nTop cursuri:");
        db.getTopCursuriCuCategorie().forEach(System.out::println);

        System.out.println("\nInscrieri per student:");
        db.getNumarInscrieriPerStudent().forEach(System.out::println);

        DatabaseConnection.getInstance().close();
    }
}
