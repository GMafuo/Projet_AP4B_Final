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
        this.verificationSystem = new VerificationSystem();
        this.solution = new Grade[3]; // 3 notes à deviner
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
        possibleRules.add(Rule.createAverageRule(12.0));
        
        // Nombre de règles selon la difficulté
        int numberOfRules = switch (difficulty) {
            case Constants.DIFFICULTY_EASY -> 3;    // TC01
            case Constants.DIFFICULTY_MEDIUM -> 4;   // TC02
            case Constants.DIFFICULTY_HARD -> 5;     // Branche
            default -> 3;
        };
        
        // Sélection aléatoire des règles avec contraintes selon la difficulté
        Collections.shuffle(possibleRules);
        
        // Ajout d'une règle de moyenne adaptée à la difficulté
        rules.add(switch (difficulty) {
            case Constants.DIFFICULTY_EASY -> Rule.createAverageRule(12.0);
            case Constants.DIFFICULTY_MEDIUM -> Rule.createAverageRule(14.0);
            case Constants.DIFFICULTY_HARD -> Rule.createAverageRule(15.0);
            default -> Rule.createAverageRule(12.0);
        });
        
        // Ajout des règles aléatoires supplémentaires
        int rulesAdded = 1;
        for (Rule rule : possibleRules) {
            if (rulesAdded >= numberOfRules) break;
            
            // Évite d'ajouter des règles trop similaires
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
