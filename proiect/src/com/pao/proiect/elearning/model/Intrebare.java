package com.pao.proiect.elearning.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Intrebare {
    private int id;
    private String text;
    private final List<String> variante;
    private int indexRaspunsCorect;

    public Intrebare() {
        this.variante = new ArrayList<>();
    }

    public Intrebare(int id, String text, List<String> variante, int indexRaspunsCorect) {
        this.id = id;
        this.text = text;
        this.variante = new ArrayList<>(variante);
        this.indexRaspunsCorect = indexRaspunsCorect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getVariante() {
        return Collections.unmodifiableList(variante);
    }

    public void setVariante(List<String> variante) {
        this.variante.clear();
        this.variante.addAll(variante);
    }

    public int getIndexRaspunsCorect() {
        return indexRaspunsCorect;
    }

    public void setIndexRaspunsCorect(int indexRaspunsCorect) {
        this.indexRaspunsCorect = indexRaspunsCorect;
    }

    public boolean esteCorect(int indexRaspuns) {
        return indexRaspuns == indexRaspunsCorect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Intrebare intrebare = (Intrebare) o;
        return id == intrebare.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Intrebare{id=" + id + ", text='" + text + "'}";
    }
}
