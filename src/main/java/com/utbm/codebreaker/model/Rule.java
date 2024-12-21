package com.utbm.codebreaker.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
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

    // Ajout de nouvelles règles prédéfinies
    public static Rule createMaxGradeRule(Grade maxGrade) {
        return new Rule(
            "Aucune note ne doit dépasser " + maxGrade.name(),
            attempt -> {
                for (Grade g : attempt) {
                    if (g.getValue() > maxGrade.getValue()) return false;
                }
                return true;
            }
        );
    }

    public static Rule createMinGradeRule(Grade minGrade) {
        return new Rule(
            "Toutes les notes doivent être supérieures ou égales à " + minGrade.name(),
            attempt -> {
                for (Grade g : attempt) {
                    if (g.getValue() < minGrade.getValue()) return false;
                }
                return true;
            }
        );
    }

    public static Rule createConsecutiveGradesRule() {
        return new Rule(
            "Les notes doivent être consécutives",
            attempt -> {
                Grade[] sorted = attempt.clone();
                Arrays.sort(sorted, Comparator.comparing(Grade::getValue));
                for (int i = 1; i < sorted.length; i++) {
                    if (Math.abs(sorted[i].getValue() - sorted[i-1].getValue()) > 3) {
                        return false;
                    }
                }
                return true;
            }
        );
    }

    public static Rule createUniqueGradesRule() {
        return new Rule(
            "Toutes les notes doivent être différentes",
            attempt -> {
                Set<Grade> grades = new HashSet<>();
                for (Grade g : attempt) {
                    if (!grades.add(g)) return false;
                }
                return true;
            }
        );
    }

    public static Rule createSumRule(int targetSum) {
        return new Rule(
            "La somme des notes doit être égale à " + targetSum,
            attempt -> {
                int sum = 0;
                for (Grade g : attempt) {
                    sum += g.getValue();
                }
                return sum == targetSum;
            }
        );
    }
}