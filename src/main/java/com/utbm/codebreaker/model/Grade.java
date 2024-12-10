package com.utbm.codebreaker.model;

public enum Grade {
    A(18, "Excellent"),
    B(15, "Très Bien"),
    C(12, "Bien"),
    D(9, "Passable"),
    F(5, "Échec");

    private final int value;
    private final String description;

    Grade(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
