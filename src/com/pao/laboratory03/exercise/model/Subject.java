package com.pao.laboratory03.exercise.model;

public enum Subject {
    PAOJ("Progamare avansata pe obiecte", 6),
    BD("Baze de date", 5),
    SO("Sisteme de operare", 4),
    RC("Retele de calculatoare", 3);

    Subject(String fullName, int credits) {
        this.fullName = fullName;
        this.credits = credits;
    }

    private final String fullName;
    private final int credits;

    public String getFullName() {
        return fullName;
    }
    public int getCredits() {
        return credits;
    }

    @Override
    public String toString()
    {
        return this.name() + " (" + fullName + ", " + credits + " credite)";
    }


}
