package com.utbm.codebreaker.core;

import com.utbm.codebreaker.model.Grade;
import com.utbm.codebreaker.model.Rule;
import java.util.List;

public class VerificationSystem {
    private Puzzle puzzle;

    public VerificationSystem(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public boolean verify(Grade[] attempt, List<Rule> rules) {
        // Vérifie que toutes les règles sont respectées
        for (Rule rule : rules) {
            if (!rule.verify(attempt)) {
                return false;
            }
        }
        return true;
    }

    public String getFeedback(Grade[] attempt, List<Rule> rules) {
        if (attempt == null || attempt.length != 3) {
            return "❌ Tentative invalide";
        }

        StringBuilder feedback = new StringBuilder();
        feedback.append("Analyse de votre tentative :\n\n");

        // Vérification des règles
        boolean allRulesValid = true;
        for (Rule rule : rules) {
            if (!rule.verify(attempt)) {
                feedback.append("❌ ").append(rule.getDescription()).append("\n");
                allRulesValid = false;
            }
        }
        
        if (allRulesValid) {
            feedback.append("✅ Toutes les règles sont respectées !\n");
        }
        
        feedback.append("\nIndices pour chaque position :\n");
        
        // Comparaison avec la solution
        Grade[] solution = puzzle.getSolution(); // Il faudra ajouter une référence au Puzzle
        
        for (int i = 0; i < attempt.length; i++) {
            feedback.append("Position ").append(i + 1).append(" (").append(attempt[i]).append(") : ");
            
            if (attempt[i] == solution[i]) {
                feedback.append("✅ Correct !\n");
            } else {
                int diff = solution[i].getValue() - attempt[i].getValue();
                if (diff > 0) {
                    feedback.append("⬆️ La note devrait être plus haute");
                    if (Math.abs(diff) <= 3) {
                        feedback.append(" (proche !)");
                    }
                } else {
                    feedback.append("⬇️ La note devrait être plus basse");
                    if (Math.abs(diff) <= 3) {
                        feedback.append(" (proche !)");
                    }
                }
                feedback.append("\n");
            }
        }
        
        return feedback.toString();
    }

    public boolean isValidAttempt(Grade[] attempt) {
        // Vérifie que la tentative est valide (bon nombre de notes, pas de null, etc.)
        if (attempt == null || attempt.length != 3) {
            return false;
        }
        
        for (Grade grade : attempt) {
            if (grade == null) {
                return false;
            }
        }
        
        return true;
    }
}