package com.pao.proiect.elearning.model;

import java.util.Objects;

public abstract class Utilizator {
    protected int id;
    protected String username;
    protected String email;
    protected String parola;

    protected Utilizator() {
    }

    protected Utilizator(int id, String username, String email, String parola) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.parola = parola;
    }

    public abstract String getRol();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Utilizator that = (Utilizator) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Utilizator{id=" + id + ", username='" + username + "', email='" + email + "', rol=" + getRol() + "}";
    }
}
