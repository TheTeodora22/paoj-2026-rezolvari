package com.pao.laboratory03.exercise.service;

import java.util.*;

import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.model.Subject;

public class StudentService {
    private static StudentService instance;
    private List<Student> students;

    private StudentService() {
        students = new ArrayList<>();
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public void addStudent(String name, int age) {
        for (Student s : students) {
            if (s.getName().equals(name)) {
                throw new RuntimeException("Studentul" + name + " deja exista");
            }
        }
        Student student = new Student(name, age);
        students.add(student);
    }

    public Student findByName(String name) {
        for (Student s : students) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        throw new RuntimeException("Studentul " + name + " nu exista");
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        Student student = findByName(studentName);
        student.addGrade(subject, grade);
    }

    public void printAllStudents() {
        for (Student s : students) {
            System.out.println(s);
            for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    public void printTopStudents() {
        students.sort((s1, s2) -> Double.compare(s2.getAverage(), s1.getAverage()));
        for (Student s : students) {
            System.out.println(s);
        }
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, List<Double>> subjectGrades = new HashMap<>();
        for (Student s : students) {
            for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()) {
                subjectGrades.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).add(entry.getValue());
            }
        }
        Map<Subject, Double> averagePerSubject = new HashMap<>();
        for (Map.Entry<Subject, List<Double>> entry : subjectGrades.entrySet()) {
            double sum = 0;
            for (double grade : entry.getValue()) {
                sum += grade;
            }
            averagePerSubject.put(entry.getKey(), sum / entry.getValue().size());
        }
        return averagePerSubject;
    }
}
