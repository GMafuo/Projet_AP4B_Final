package com.utbm.codebreaker.core;

import com.utbm.codebreaker.model.Grade;
import com.utbm.codebreaker.model.Rule;
import com.utbm.codebreaker.utils.Constants;

import java.util.ArrayList;
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
        // Vider la liste des règles existantes
        rules.clear();
        
        switch (difficulty) {
            case Constants.DIFFICULTY_EASY: // TC01
                rules.add(Rule.createAverageRule(12.0));
                rules.add(Rule.createContainsGradeRule(Grade.B));
                rules.add(Rule.createNoFailRule());
                break;
                
            case Constants.DIFFICULTY_MEDIUM: // TC02
                rules.add(Rule.createAverageRule(14.0));
                rules.add(Rule.createContainsGradeRule(Grade.A));
                rules.add(Rule.createNoFailRule());
                break;
                
            case Constants.DIFFICULTY_HARD: // Branche
                rules.add(Rule.createAverageRule(15.0));
                rules.add(Rule.createContainsGradeRule(Grade.A));
                rules.add(new Rule("Au moins deux notes supérieures à B", 
                    attempt -> {
                        int count = 0;
                        for (Grade g : attempt) {
                            if (g == Grade.A || g == Grade.B) count++;
                        }
                        return count >= 2;
                    }));
                break;
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
