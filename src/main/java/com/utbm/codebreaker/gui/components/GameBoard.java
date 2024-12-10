package com.utbm.codebreaker.gui.components;

import com.utbm.codebreaker.model.Grade;
import com.utbm.codebreaker.model.Attempt;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameBoard extends JPanel {
    private List<AttemptRow> attemptRows;
    private int maxAttempts;
    
    public GameBoard(int maxAttempts) {
        this.maxAttempts = maxAttempts;
        this.attemptRows = new ArrayList<>();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Plateau de jeu"));
        
        // Création des lignes pour chaque tentative possible
        for (int i = 0; i < maxAttempts; i++) {
            AttemptRow row = new AttemptRow(i + 1);
            attemptRows.add(row);
            add(row);
        }
    }
    
    public void addAttempt(Grade[] grades, boolean isCorrect, String feedback) {
        // Trouve la première ligne vide
        for (AttemptRow row : attemptRows) {
            if (!row.isUsed()) {
                row.setAttempt(grades, isCorrect, feedback);
                break;
            }
        }
        revalidate();
        repaint();
    }
    
    // Classe interne pour représenter une ligne de tentative
    private static class AttemptRow extends JPanel {
        private final JLabel[] gradeLabels;
        private final JLabel resultLabel;
        private boolean used;
        
        public AttemptRow(int attemptNumber) {
            setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
            setBorder(BorderFactory.createEtchedBorder());
            
            add(new JLabel(String.format("Tentative %d:", attemptNumber)));
            
            gradeLabels = new JLabel[3];
            for (int i = 0; i < 3; i++) {
                gradeLabels[i] = new JLabel("_");
                gradeLabels[i].setPreferredSize(new Dimension(30, 20));
                gradeLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
                gradeLabels[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                add(gradeLabels[i]);
            }
            
            resultLabel = new JLabel();
            add(resultLabel);
            
            used = false;
        }
        
        public void setAttempt(Grade[] grades, boolean isCorrect, String feedback) {
            for (int i = 0; i < grades.length; i++) {
                gradeLabels[i].setText(grades[i].name());
                gradeLabels[i].setForeground(getColorForGrade(grades[i]));
            }
            
            resultLabel.setText(isCorrect ? "✅" : "❌");
            resultLabel.setToolTipText(feedback);
            
            used = true;
        }
        
        public boolean isUsed() {
            return used;
        }
        
        private Color getColorForGrade(Grade grade) {
            return switch (grade) {
                case A -> new Color(0, 150, 0);  // Vert
                case B -> new Color(0, 100, 200); // Bleu
                case C -> new Color(200, 150, 0); // Orange
                case D -> new Color(200, 100, 0); // Orange foncé
                case F -> Color.RED;
            };
        }
    }
}
