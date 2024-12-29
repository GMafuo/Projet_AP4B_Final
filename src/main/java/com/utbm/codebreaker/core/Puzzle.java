package com.utbm.codebreaker.core;

import com.utbm.codebreaker.model.Grade;
import com.utbm.codebreaker.model.Rule;
import com.utbm.codebreaker.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Puzzle {
    private Grade[] solution;
    private List<Rule> rules;
    private int difficulty;
    private VerificationSystem verificationSystem;

    public Puzzle(int difficulty) {
        this.difficulty = difficulty;
        this.rules = new ArrayList<>();
        this.solution = new Grade[3]; // 3 notes à deviner
        this.verificationSystem = new VerificationSystem(this);
    }

    public void generatePuzzle() {
        // Génération des règles selon la difficulté
        generateRules();
        // Génération d'une solution valide
        generateValidSolution();
    }

    private void generateRules() {
        rules.clear();
        List<Rule> possibleRules = new ArrayList<>();
        
        // Ajustement des règles selon la difficulté
        switch (difficulty) {
            case Constants.DIFFICULTY_EASY -> {
                // Règles actuelles pour facile
                possibleRules.add(Rule.createNoFailRule());
                possibleRules.add(Rule.createUniqueGradesRule());
                possibleRules.add(Rule.createContainsGradeRule(Grade.A));
            }
            case Constants.DIFFICULTY_MEDIUM -> {
                // Règles ajustées pour moyen
                possibleRules.add(Rule.createNoFailRule());
                possibleRules.add(Rule.createUniqueGradesRule());
                possibleRules.add(Rule.createContainsGradeRule(Grade.A));
                possibleRules.add(Rule.createMinGradeRule(Grade.D));
            }
            case Constants.DIFFICULTY_HARD -> {
                // Règles ajustées pour difficile
                possibleRules.add(Rule.createNoFailRule());
                possibleRules.add(Rule.createUniqueGradesRule());
                possibleRules.add(Rule.createContainsGradeRule(Grade.A));
                possibleRules.add(Rule.createMinGradeRule(Grade.C));
                possibleRules.add(Rule.createConsecutiveGradesRule());
            }
        }
        
        // Génération d'une moyenne aléatoire selon la difficulté avec des plages ajustées
        double minAverage = switch (difficulty) {
            case Constants.DIFFICULTY_EASY -> 11.0 + Math.random() * 3.0;    // Entre 11 et 14
            case Constants.DIFFICULTY_MEDIUM -> 12.0 + Math.random() * 3.0;  // Entre 12 et 15 (ajusté)
            case Constants.DIFFICULTY_HARD -> 13.0 + Math.random() * 3.0;    // Entre 13 et 16 (ajusté)
            default -> 11.0 + Math.random() * 3.0;
        };
        
        minAverage = Math.round(minAverage * 2) / 2.0;
        rules.add(Rule.createAverageRule(minAverage));
        
        // Ajout des règles aléatoires supplémentaires
        Collections.shuffle(possibleRules);
        int numberOfRules = switch (difficulty) {
            case Constants.DIFFICULTY_EASY -> 3;
            case Constants.DIFFICULTY_MEDIUM -> 4;
            case Constants.DIFFICULTY_HARD -> 5;
            default -> 3;
        };
        
        // Ajout des règles en respectant le nombre maximum
        for (int i = 0; i < Math.min(numberOfRules - 1, possibleRules.size()); i++) {
            rules.add(possibleRules.get(i));
        }
    }

    private void generateValidSolution() {
        Grade[] possibleGrades = Grade.values();
        boolean validSolutionFound = false;
        int maxAttempts = 1000;
        int attempts = 0;

        while (!validSolutionFound && attempts < maxAttempts) {
            attempts++;
            
            // Génère une solution aléatoire
            for (int i = 0; i < solution.length; i++) {
                solution[i] = possibleGrades[(int)(Math.random() * possibleGrades.length)];
            }
            
            // Vérifie si la solution respecte toutes les règles
            validSolutionFound = verificationSystem.verify(solution, rules);
        }

        // Si aucune solution n'est trouvée après maxAttempts essais,
        // on régénère les règles avec des contraintes plus souples
        if (!validSolutionFound) {
            System.out.println("Impossible de générer une solution valide, régénération des règles...");
            rules.clear();
            generateRules();
            generateValidSolution(); 
        }
    }

    public boolean validateAttempt(Grade[] attempt) {
        return verificationSystem.verify(attempt, rules);
    }

    public List<Rule> getRules() {
        return new ArrayList<>(rules);
    }

    public String getFeedback(Grade[] attempt) {
        return verificationSystem.getFeedback(attempt, rules);
    }

    public boolean isSolution(Grade[] attempt) {
        if (!verificationSystem.isValidAttempt(attempt)) {
            return false;
        }
        
        // Compare avec la solution
        for (int i = 0; i < solution.length; i++) {
            if (attempt[i] != solution[i]) {
                return false;
            }
        }
        return true;
    }

    public Grade[] getSolution() {
        return solution.clone(); // Retourne une copie de la solution
    }
}
