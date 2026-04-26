package com.pao.proiect.elearning.service;

import com.pao.proiect.elearning.exception.InvalidDataException;
import com.pao.proiect.elearning.model.Instructor;
import com.pao.proiect.elearning.model.Student;
import com.pao.proiect.elearning.model.Utilizator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class UtilizatorService {

    private static UtilizatorService instance;

    private final Map<String, Utilizator> utilizatoriDupaUsername = new HashMap<>();
    private final Map<Integer, Utilizator> utilizatoriDupaId = new HashMap<>();
    private int nextUserId = 1;

    private UtilizatorService() {
    }

    public static synchronized UtilizatorService getInstance() {
        if (instance == null) {
            instance = new UtilizatorService();
        }
        return instance;
    }

    public Student creareContStudent(String username, String email, String parola, String grupa) {
        if (utilizatoriDupaUsername.containsKey(username)) {
            throw new InvalidDataException("Username deja folosit: " + username);
        }
        Student s = new Student(nextUserId++, username, email, parola, grupa);
        inregistreazaUtilizator(s);
        return s;
    }

    public Instructor creareContInstructor(String username, String email, String parola, String departament) {
        if (utilizatoriDupaUsername.containsKey(username)) {
            throw new InvalidDataException("Username deja folosit: " + username);
        }
        Instructor i = new Instructor(nextUserId++, username, email, parola, departament);
        inregistreazaUtilizator(i);
        return i;
    }

    private void inregistreazaUtilizator(Utilizator u) {
        utilizatoriDupaUsername.put(u.getUsername(), u);
        utilizatoriDupaId.put(u.getId(), u);
    }

    public void stergeUtilizator(int id) {
        Utilizator u = utilizatoriDupaId.get(id);
        if (u == null) {
            throw new InvalidDataException("Utilizator inexistent: " + id);
        }
        if (u instanceof Student) {
            CursService.getInstance().eliminaStudentDinToateCursurile(id);
        } else if (u instanceof Instructor) {
            CursService.getInstance().eliminaCursuriInstruiteDe(id);
        }
        utilizatoriDupaUsername.remove(u.getUsername());
        utilizatoriDupaId.remove(id);
    }

    public Optional<Utilizator> cautaDupaId(int id) {
        return Optional.ofNullable(utilizatoriDupaId.get(id));
    }

    public Optional<Utilizator> cautaDupaUsername(String username) {
        return Optional.ofNullable(utilizatoriDupaUsername.get(username));
    }

    public List<Utilizator> listeazaTotiUtilizatorii() {
        return Collections.unmodifiableList(new ArrayList<>(utilizatoriDupaId.values()));
    }

    public Optional<Utilizator> login(String username, String parola) {
        Utilizator u = utilizatoriDupaUsername.get(username);
        if (u == null || !u.getParola().equals(parola)) {
            return Optional.empty();
        }
        return Optional.of(u);
    }

    Utilizator getUtilizatorDupaIdIntern(int id) {
        return utilizatoriDupaId.get(id);
    }

    public Set<String> getUsernamesInregistrate() {
        return Collections.unmodifiableSet(utilizatoriDupaUsername.keySet());
    }
}
