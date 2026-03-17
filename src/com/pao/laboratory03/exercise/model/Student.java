package com.pao.laboratory03.exercise.model;
import java.util.*;
import com.pao.laboratory03.exercise.exception.*;

public class Student {
    private final String name;
    private final int age;
    private final Map<Subject, Double> grades;
    public Student(String name, int age) {
        if (age < 18 || age > 60) {
            throw new InvalidStudentException("Vârsta studentului trebuie să fie între 18 și 60 de ani.");
        }
        this.name = name;
        this.age = age;
        this.grades = new HashMap<>();
    }
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }
    public Map<Subject, Double> getGrades() {
        return grades;
    }
    public void addGrade(Subject subject, double grade) {
        if (grade < 1 || grade > 10) {
            throw new InvalidGradeException("Nota trebuie sa fie intre 1 si 10");
        }
        grades.put(subject, grade);
    }
    public double getAverage() {
        if (grades.isEmpty()) {
            return 0;
        }
        double s=0;
        for (double grade : grades.values()) {
            s+=grade;
        }
        return s/grades.size();
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", avg=" + getAverage() +
                '}';
    }
}
