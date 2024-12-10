package com.utbm.codebreaker.core;

import com.utbm.codebreaker.model.Grade;
import com.utbm.codebreaker.model.Rule;
import java.util.List;

public class VerificationSystem {
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
        StringBuilder feedback = new StringBuilder();
        
        // Vérifie chaque règle et ajoute le feedback approprié
        for (Rule rule : rules) {
            if (!rule.verify(attempt)) {
                feedback.append("❌ ").append(rule.getDescription()).append("\n");
            } else {
                feedback.append("✅ ").append(rule.getDescription()).append("\n");
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