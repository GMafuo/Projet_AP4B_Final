package com.utbm.codebreaker.model;

import java.util.function.Predicate;

public class Rule {
    private String description;
    private Predicate<Grade[]> verificationLogic;
    private int id;
    private static int nextId = 1;

    public Rule(String description, Predicate<Grade[]> verificationLogic) {
        this.id = nextId++;
        this.description = description;
        this.verificationLogic = verificationLogic;
    }

    public boolean verify(Grade[] attempt) {
        return verificationLogic.test(attempt);
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    // Exemples de règles prédéfinies
    public static Rule createAverageRule(double minAverage) {
        return new Rule(
            "La moyenne doit être supérieure à " + minAverage,
            attempt -> {
                double sum = 0;
                for (Grade grade : attempt) {
                    sum += grade.getValue();
                }
                return (sum / attempt.length) > minAverage;
            }
        );
    }

    public static Rule createContainsGradeRule(Grade grade) {
        return new Rule(
            "Doit contenir au moins une note " + grade.name(),
            attempt -> {
                for (Grade g : attempt) {
                    if (g == grade) return true;
                }
                return false;
            }
        );
    }

    public static Rule createNoFailRule() {
        return new Rule(
            "Ne doit pas contenir de note F",
            attempt -> {
                for (Grade g : attempt) {
                    if (g == Grade.F) return false;
                }
                return true;
            }
        );
    }
}