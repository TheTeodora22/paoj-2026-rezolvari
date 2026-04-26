package com.pao.proiect.elearning.service;

import com.pao.proiect.elearning.exception.InvalidDataException;
import com.pao.proiect.elearning.exception.PlatformStateException;
import com.pao.proiect.elearning.model.Categorie;
import com.pao.proiect.elearning.model.Certificat;
import com.pao.proiect.elearning.model.Curs;
import com.pao.proiect.elearning.model.Instructor;
import com.pao.proiect.elearning.model.Intrebare;
import com.pao.proiect.elearning.model.Lectie;
import com.pao.proiect.elearning.model.Quiz;
import com.pao.proiect.elearning.model.Student;
import com.pao.proiect.elearning.model.Submisie;
import com.pao.proiect.elearning.model.Tema;
import com.pao.proiect.elearning.model.Utilizator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class CursService {

    private static CursService instance;

    private final TreeMap<String, Curs> cursuriSortateDupaTitlu = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final List<Curs> toateCursurile = new ArrayList<>();
    private final Map<Integer, List<Submisie>> submisiiDupaTemaId = new HashMap<>();
    private final List<Certificat> certificateEmise = new ArrayList<>();

    private int nextCursId = 1;
    private int nextLectieId = 1;
    private int nextQuizId = 1;
    private int nextIntrebareId = 1;
    private int nextTemaId = 1;
    private int nextSubmisieId = 1;
    private int nextCertificatId = 1;

    private CursService() {
    }

    public static synchronized CursService getInstance() {
        if (instance == null) {
            instance = new CursService();
        }
        return instance;
    }

    public Curs adaugaCurs(String titlu, Categorie categorie, Instructor instructor) {
        if (cursuriSortateDupaTitlu.containsKey(titlu)) {
            throw new InvalidDataException("Exista deja un curs cu titlul: " + titlu);
        }
        Curs c = new Curs(nextCursId++, titlu, categorie, instructor);
        cursuriSortateDupaTitlu.put(titlu, c);
        toateCursurile.add(c);
        return c;
    }

    public void stergeCurs(int cursId) {
        Curs curs = gasesteCursById(cursId);
        for (Integer sid : new ArrayList<>(curs.getStudentiInscrisiIds())) {
            UtilizatorService.getInstance().cautaDupaId(sid).filter(Student.class::isInstance)
                    .ifPresent(u -> ((Student) u).renuntaLaCurs(cursId));
        }
        for (Tema t : curs.getTeme()) {
            submisiiDupaTemaId.remove(t.getId());
        }
        certificateEmise.removeIf(cert -> cert.getCursId() == cursId);
        cursuriSortateDupaTitlu.remove(curs.getTitlu());
        toateCursurile.remove(curs);
    }

    public Optional<Curs> cautaCursDupaId(int cursId) {
        return toateCursurile.stream().filter(c -> c.getId() == cursId).findFirst();
    }

    public Optional<Curs> cautaCursDupaTitlu(String titlu) {
        return Optional.ofNullable(cursuriSortateDupaTitlu.get(titlu));
    }

    public List<Curs> listeazaToateCursurile() {
        return Collections.unmodifiableList(new ArrayList<>(toateCursurile));
    }

    public List<Curs> listeazaCursurileSortateDupaTitlu() {
        return new ArrayList<>(cursuriSortateDupaTitlu.values());
    }

    public Quiz adaugaQuizLaCurs(int cursId, String titluQuiz) {
        Curs curs = gasesteCursById(cursId);
        Quiz q = new Quiz(nextQuizId++, titluQuiz);
        curs.adaugaQuiz(q);
        return q;
    }

    public void stergeQuizLaCurs(int cursId, int quizId) {
        Curs curs = gasesteCursById(cursId);
        if (!curs.stergeQuiz(quizId)) {
            throw new InvalidDataException("Quiz inexistent pe curs: " + quizId);
        }
    }

    public List<Curs> afiseazaCursurileStudentului(int studentId) {
        Utilizator u = UtilizatorService.getInstance().getUtilizatorDupaIdIntern(studentId);
        if (!(u instanceof Student student)) {
            return List.of();
        }
        return student.getCursuriInscrisIds().stream()
                .map(this::gasesteCursById)
                .collect(Collectors.toList());
    }

    public List<Student> afiseazaStudentiiLaCurs(int cursId) {
        Curs curs = gasesteCursById(cursId);
        List<Student> rezultat = new ArrayList<>();
        for (Integer sid : curs.getStudentiInscrisiIds()) {
            Utilizator u = UtilizatorService.getInstance().getUtilizatorDupaIdIntern(sid);
            if (u instanceof Student s) {
                rezultat.add(s);
            }
        }
        return rezultat;
    }

    public void inscrieStudentLaCurs(int studentId, int cursId) {
        Utilizator u = UtilizatorService.getInstance().getUtilizatorDupaIdIntern(studentId);
        if (!(u instanceof Student student)) {
            throw new InvalidDataException("ID-ul nu corespunde unui student: " + studentId);
        }
        Curs curs = gasesteCursById(cursId);
        curs.inscrieStudent(studentId);
        student.inscrieLaCurs(cursId);
    }

    public Lectie adaugaLectieLaCurs(int cursId, String titluLectie, String continut, int ordine) {
        Curs curs = gasesteCursById(cursId);
        Lectie l = new Lectie(nextLectieId++, titluLectie, continut, ordine);
        curs.adaugaLectie(l);
        return l;
    }

    public Intrebare adaugaIntrebareLaQuiz(int cursId, int quizId, String text, List<String> variante, int indexCorect) {
        Quiz quiz = gasesteQuiz(cursId, quizId);
        Intrebare intrebare = new Intrebare(nextIntrebareId++, text, variante, indexCorect);
        quiz.adaugaIntrebare(intrebare);
        return intrebare;
    }

    public double rezolvaQuiz(int cursId, int quizId, List<Integer> indecsiRaspunsuri) {
        Quiz quiz = gasesteQuiz(cursId, quizId);
        List<Intrebare> intrebari = quiz.getIntrebari();
        if (intrebari.isEmpty()) {
            return 0.0;
        }
        if (indecsiRaspunsuri.size() != intrebari.size()) {
            throw new InvalidDataException("Numar de raspunsuri diferit de numarul de intrebari.");
        }
        int corecte = 0;
        for (int i = 0; i < intrebari.size(); i++) {
            if (intrebari.get(i).esteCorect(indecsiRaspunsuri.get(i))) {
                corecte++;
            }
        }
        return (double) corecte / intrebari.size();
    }

    public Certificat genereazaCertificatFinalizareCurs(int studentId, int cursId) {
        Utilizator u = UtilizatorService.getInstance().getUtilizatorDupaIdIntern(studentId);
        if (!(u instanceof Student)) {
            throw new InvalidDataException("Certificatul se emite doar pentru studenti.");
        }
        Curs curs = gasesteCursById(cursId);
        if (!curs.getStudentiInscrisiIds().contains(studentId)) {
            throw new PlatformStateException("Studentul nu este inscris la acest curs.");
        }
        if (curs.getLectii().isEmpty()) {
            throw new PlatformStateException("Cursul nu are lectii - considerat nefinalizat.");
        }
        String cod = "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Certificat cert = new Certificat(nextCertificatId++, studentId, cursId, LocalDate.now(), cod);
        certificateEmise.add(cert);
        return cert;
    }

    public Tema adaugaTemaLaCurs(int cursId, String titlu, String descriere, LocalDate termen) {
        Curs c = gasesteCursById(cursId);
        Tema t = new Tema(nextTemaId++, titlu, descriere, termen);
        c.adaugaTema(t);
        return t;
    }

    public Submisie trimiteSubmisie(int studentId, int temaId, String continut) {
        Submisie s = new Submisie(nextSubmisieId++, studentId, temaId, continut, LocalDateTime.now());
        submisiiDupaTemaId.computeIfAbsent(temaId, k -> new ArrayList<>()).add(s);
        return s;
    }

    public List<Certificat> getCertificateEmise() {
        return Collections.unmodifiableList(certificateEmise);
    }

    public void eliminaStudentDinToateCursurile(int studentId) {
        for (Curs c : new ArrayList<>(toateCursurile)) {
            c.renuntaStudent(studentId);
        }
        Utilizator u = UtilizatorService.getInstance().getUtilizatorDupaIdIntern(studentId);
        if (u instanceof Student s) {
            for (int cid : new ArrayList<>(s.getCursuriInscrisIds())) {
                s.renuntaLaCurs(cid);
            }
        }
    }

    public void eliminaCursuriInstruiteDe(int instructorId) {
        List<Integer> idsDeSters = toateCursurile.stream()
                .filter(c -> c.getInstructor().getId() == instructorId)
                .map(Curs::getId)
                .collect(Collectors.toList());
        for (int id : idsDeSters) {
            stergeCurs(id);
        }
    }

    private Curs gasesteCursById(int cursId) {
        return toateCursurile.stream()
                .filter(c -> c.getId() == cursId)
                .findFirst()
                .orElseThrow(() -> new InvalidDataException("Curs inexistent: " + cursId));
    }

    private Quiz gasesteQuiz(int cursId, int quizId) {
        Curs curs = gasesteCursById(cursId);
        return curs.getQuizuri().stream()
                .filter(q -> q.getId() == quizId)
                .findFirst()
                .orElseThrow(() -> new InvalidDataException("Quiz inexistent pe curs: " + quizId));
    }
}
