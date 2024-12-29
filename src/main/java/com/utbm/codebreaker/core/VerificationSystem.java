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

        // Vérification des règles avec exemples
        boolean allRulesValid = true;
        for (Rule rule : rules) {
            if (!rule.verify(attempt)) {
                feedback.append("❌ ").append(rule.getDescription()).append("\n");
                
                // Ajout d'information pour la règle de moyenne
                if (rule.getDescription().contains("moyenne")) {
                    double sum = 0;
                    for (Grade g : attempt) {
                        sum += g.getValue();
                    }
                    double average = sum / attempt.length;
                    feedback.append(String.format("   📊 Moyenne actuelle : %.1f\n", average));
                }
                
                feedback.append("   💡 Exemple valide : ").append(generateExampleForRule(rule)).append("\n");
                allRulesValid = false;
            }
        }
        
        if (allRulesValid) {
            feedback.append("✅ Toutes les règles sont respectées !\n");
        }
        
        // Analyse détaillée des positions
        feedback.append("\nIndices pour chaque position :\n");
        Grade[] solution = puzzle.getSolution();
        int correctPositions = 0;
        
        for (int i = 0; i < attempt.length; i++) {
            feedback.append("Position ").append(i + 1).append(" (").append(attempt[i]).append(") : ");
            
            if (attempt[i] == solution[i]) {
                feedback.append("✅ Correct !\n");
                correctPositions++;
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

        // Conseils stratégiques
        feedback.append("\n💡 Conseil : ");
        if (!allRulesValid) {
            feedback.append("Concentrez-vous d'abord sur le respect des règles !");
        } else if (correctPositions == 0) {
            feedback.append("Essayez de changer complètement votre approche !");
        } else if (correctPositions == 1) {
            feedback.append("Bonne direction ! Gardez cette note et ajustez les autres.");
        } else if (correctPositions == 2) {
            feedback.append("Vous y êtes presque ! Une seule note à modifier.");
        }

        return feedback.toString();
    }

    private String generateExampleForRule(Rule rule) {
        // Génère un exemple simple qui respecte la règle spécifique
        if (rule.getDescription().contains("moyenne")) {
            return "A B B";
        } else if (rule.getDescription().contains("consécutives")) {
            return "A B C";
        } else if (rule.getDescription().contains("différentes")) {
            return "A B C";
        }
        return "A B C"; 
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