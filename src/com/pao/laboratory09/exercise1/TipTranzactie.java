package com.pao.laboratory09.exercise1;

public enum TipTranzactie {
    CREDIT("CREDIT"),
    DEBIT("DEBIT");

    private String value;
    TipTranzactie(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
