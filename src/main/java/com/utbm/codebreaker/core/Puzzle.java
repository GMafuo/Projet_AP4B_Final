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
        
        // Règles communes à toutes les difficultés
        possibleRules.add(Rule.createNoFailRule());
        possibleRules.add(Rule.createUniqueGradesRule());
        possibleRules.add(Rule.createConsecutiveGradesRule());
        possibleRules.add(Rule.createContainsGradeRule(Grade.A));
        possibleRules.add(Rule.createContainsGradeRule(Grade.B));
        possibleRules.add(Rule.createMinGradeRule(Grade.D));
        possibleRules.add(Rule.createMaxGradeRule(Grade.A));
        possibleRules.add(Rule.createSumRule(45));
        
        // Nombre de règles selon la difficulté
        int numberOfRules = switch (difficulty) {
            case Constants.DIFFICULTY_EASY -> 3;    // TC01
            case Constants.DIFFICULTY_MEDIUM -> 4;   // TC02
            case Constants.DIFFICULTY_HARD -> 5;     // Branche
            default -> 3;
        };
        
        // Génération d'une moyenne aléatoire selon la difficulté
        double minAverage = switch (difficulty) {
            case Constants.DIFFICULTY_EASY -> 11.0 + Math.random() * 3.0;    // Entre 11 et 14
            case Constants.DIFFICULTY_MEDIUM -> 13.0 + Math.random() * 3.0;  // Entre 13 et 16
            case Constants.DIFFICULTY_HARD -> 14.0 + Math.random() * 3.0;    // Entre 14 et 17
            default -> 11.0 + Math.random() * 3.0;
        };
        
        // Arrondir à 0.5 près pour plus de lisibilité
        minAverage = Math.round(minAverage * 2) / 2.0;
        
        rules.add(Rule.createAverageRule(minAverage));
        
        Collections.shuffle(possibleRules);
        int rulesAdded = 1;
        
        for (Rule rule : possibleRules) {
            if (rulesAdded >= numberOfRules) break;
            
            boolean isCompatible = true;
            for (Rule existingRule : rules) {
                if (rule.getDescription().equals(existingRule.getDescription())) {
                    isCompatible = false;
                    break;
                }
            }
            
            if (isCompatible) {
                rules.add(rule);
                rulesAdded++;
            }
        }
    }

    private void generateValidSolution() {
        Grade[] possibleGrades = Grade.values();
        boolean validSolutionFound = false;
        
        while (!validSolutionFound) {
            // Génère une solution aléatoire
            for (int i = 0; i < solution.length; i++) {
                solution[i] = possibleGrades[(int)(Math.random() * possibleGrades.length)];
            }
            
            // Vérifie si la solution respecte toutes les règles
            validSolutionFound = verificationSystem.verify(solution, rules);
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
