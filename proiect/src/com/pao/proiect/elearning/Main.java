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
import com.pao.proiect.elearning.service.CursService;
import com.pao.proiect.elearning.service.UtilizatorService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        UtilizatorService utilizatori = UtilizatorService.getInstance();
        CursService platforma = CursService.getInstance();

        Categorie catJava = new Categorie(1, "Programare");
        Instructor prof = utilizatori.creareContInstructor("prof.ion", "ion@univ.ro", "parola123", "Informatica");
        Student stud = utilizatori.creareContStudent("maria.s", "maria@student.ro", "secret", "Grupa 232");

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

        Curs cursPaoj = platforma.adaugaCurs("Programare avansata obiecte Java", catJava, prof);
        platforma.adaugaCurs("Structuri de date", catJava, prof);

        System.out.println("\nToate cursurile ");
        platforma.listeazaToateCursurile().forEach(c -> System.out.println(c.getTitlu()));

        System.out.println("\nCursuri sortate dupa titlu");
        platforma.listeazaCursurileSortateDupaTitlu().forEach(c -> System.out.println(c.getTitlu()));

        System.out.println("\nCautare curs dupa titlu");
        platforma.cautaCursDupaTitlu("Structuri de date")
                .ifPresentOrElse(c -> System.out.println("Gasit: " + c), () -> System.out.println("Lipsa"));

        System.out.println("\nCautare curs dupa id");
        platforma.cautaCursDupaId(cursPaoj.getId()).ifPresent(c -> System.out.println("ID " + c.getId() + ": " + c.getTitlu()));

        platforma.inscrieStudentLaCurs(stud.getId(), cursPaoj.getId());

        Lectie l1 = platforma.adaugaLectieLaCurs(cursPaoj.getId(), "Mostenire si polimorfism", "Note...", 1);
        System.out.println("\nLectie adaugata");
        System.out.println(l1);

        Quiz quiz = platforma.adaugaQuizLaCurs(cursPaoj.getId(), "Quiz 1 - PAOJ");
        platforma.adaugaIntrebareLaQuiz(
                cursPaoj.getId(),
                quiz.getId(),
                "Ce cuvant cheie folosesti pentru mostenire in Java?",
                List.of("implements", "extends", "inherit", "super"),
                1);

        double scor = platforma.rezolvaQuiz(cursPaoj.getId(), quiz.getId(), List.of(1));
        System.out.println("\nScor quiz");
        System.out.println("Scor: " + scor);

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

        System.out.println("\nCursuri la care e inscris studentul");
        platforma.afiseazaCursurileStudentului(stud.getId()).forEach(c -> System.out.println(c.getTitlu()));

        System.out.println("\nStudenti inscrisi la curs");
        platforma.afiseazaStudentiiLaCurs(cursPaoj.getId()).forEach(s -> System.out.println(s.getUsername()));

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
    }
}
