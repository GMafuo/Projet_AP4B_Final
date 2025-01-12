package com.utbm.codebreaker.gui;

import com.utbm.codebreaker.core.Game;
import com.utbm.codebreaker.model.Attempt;
import com.utbm.codebreaker.model.Grade;
import com.utbm.codebreaker.gui.components.GameBoard;
import com.utbm.codebreaker.model.Rule;
import com.utbm.codebreaker.model.HighScore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.border.AbstractBorder;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Insets;
import javax.swing.border.TitledBorder;

public class GameScreen extends JPanel {
    private Game game;
    private GameBoard gameBoard;
    private JPanel controlPanel;
    private JPanel rulesPanel;
    private JComboBox<Grade>[] gradeSelectors;
    private JTextArea feedbackArea;
    private int difficulty;
    private JLabel attemptsLabel;
    private JButton restartButton;
    private JButton historyButton;
    private String playerName;
    private Timer gameTimer;
    private int timeSpent;
    private JLabel timerLabel;
    private static List<HighScore> highScores = new ArrayList<>();
    
    @SuppressWarnings("unchecked")
    public GameScreen(String playerName, int difficulty) {
        this.difficulty = difficulty;
        this.playerName = playerName;
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 242, 245));
        
        // Initialisation du jeu
        game = new Game(difficulty);
        game.startGame(playerName);
        
        // Initialisation du timer
        timeSpent = 0;
        gameTimer = new Timer(1000, e -> {
            timeSpent++;
            updateTimerLabel();
        });
        gameTimer.start();
        
        // Panel principal
        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setBackground(new Color(240, 242, 245));
        
        // Création des composants
        createRulesPanel();
        createGameBoard();
        createControlPanel();
        
        // Ajout des composants
        mainContent.add(createStyledPanel(rulesPanel, "Règles du jeu"), BorderLayout.WEST);
        mainContent.add(createStyledPanel(gameBoard, "Plateau de jeu"), BorderLayout.CENTER);
        
        // Panel de droite avec contrôles et feedback
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        
        // Création de la zone de feedback
        feedbackArea = new JTextArea();
        feedbackArea.setEditable(false);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        feedbackArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane feedbackScroll = new JScrollPane(feedbackArea);
        feedbackScroll.setPreferredSize(new Dimension(200, 100));
        
        rightPanel.add(createStyledPanel(controlPanel, "Contrôles"));
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(createStyledPanel(feedbackScroll, "Feedback"));
        mainContent.add(rightPanel, BorderLayout.EAST);
        
        add(mainContent, BorderLayout.CENTER);
    }
    
    private JPanel createStyledPanel(Component component, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                title,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 70, 70)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }
    
    private void createRulesPanel() {
        if (rulesPanel == null) {
            rulesPanel = new JPanel();
        }
        rulesPanel.removeAll();
        rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
        rulesPanel.setBorder(BorderFactory.createTitledBorder("Règles"));
        
        JTextArea rulesArea = new JTextArea();
        rulesArea.setEditable(false);
        rulesArea.setLineWrap(true);
        rulesArea.setWrapStyleWord(true);
        rulesArea.setPreferredSize(new Dimension(200, 150));
        
        // Récupération des règles actuelles
        StringBuilder rulesText = new StringBuilder();
        for (Rule rule : game.getCurrentPuzzle().getRules()) {
            rulesText.append("• ").append(rule.getDescription()).append("\n");
        }
        rulesArea.setText(rulesText.toString());
        
        rulesPanel.add(new JScrollPane(rulesArea));
        rulesPanel.setPreferredSize(new Dimension(220, 200));
        rulesPanel.revalidate();
        rulesPanel.repaint();
    }
    
    private void createGameBoard() {
        // Taille plateau
        gameBoard = new GameBoard(game.getRemainingAttempts());
        gameBoard.setPreferredSize(new Dimension(800, 400)); 
        gameBoard.setMaximumSize(new Dimension(400, 400));
    }
    
    @SuppressWarnings("unchecked")
    private void createControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(Color.WHITE);
        
        // Compteur de tentatives
        attemptsLabel = createStyledLabel("Tentatives restantes : " + game.getRemainingAttempts());
        
        // Panel de sélection des notes
        JPanel selectorsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        selectorsPanel.setBackground(Color.WHITE);
        gradeSelectors = new JComboBox[3];
        
        // Label pour la moyenne
        JLabel averageLabel = createStyledLabel("Moyenne : --");
        
        // Création des sélecteurs avec leurs listeners
        for (int i = 0; i < 3; i++) {
            gradeSelectors[i] = createStyledComboBox(Grade.values());
            gradeSelectors[i].setSelectedItem(Grade.A); 
            selectorsPanel.add(createStyledLabel("Note " + (i + 1)));
            selectorsPanel.add(gradeSelectors[i]);
            
            gradeSelectors[i].addItemListener(e -> {
                updateAverageLabel(averageLabel);
            });
        }

        updateAverageLabel(averageLabel);

        // Boutons d'action
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(3, 1, 0, 10));
        buttonsPanel.setBackground(Color.WHITE);
        
        JButton validateButton = createStyledButton("Valider", new Color(46, 125, 50));
        JButton newGameButton = createStyledButton("Nouvelle partie", new Color(25, 118, 210));
        JButton historyButton = createStyledButton("Historique", new Color(156, 39, 176));
        
        validateButton.addActionListener(e -> makeAttempt());
        newGameButton.addActionListener(e -> restartGame());
        historyButton.addActionListener(e -> showHistory());
        
        buttonsPanel.add(validateButton);
        buttonsPanel.add(newGameButton);
        buttonsPanel.add(historyButton);

        // Ajout des composants
        controlPanel.add(attemptsLabel);
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(selectorsPanel);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(averageLabel);  
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(buttonsPanel);
        
        // Timer
        timerLabel = createStyledLabel("Temps : 0:00");
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(timerLabel);
    }
    
    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, baseColor.darker()),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });

        return button;
    }
    
    private JComboBox<Grade> createStyledComboBox(Grade[] items) {
        JComboBox<Grade> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return combo;
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(70, 70, 70));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
    
    private void makeAttempt() {
        try {
            Grade[] attempt = new Grade[3];
            for (int i = 0; i < 3; i++) {
                attempt[i] = (Grade) gradeSelectors[i].getSelectedItem();
            }
            
            boolean result = game.makeAttempt(attempt);
            String feedback = game.getCurrentPuzzle().getFeedback(attempt);
            
            gameBoard.addAttempt(attempt, result, feedback);
            feedbackArea.setText(feedback);
            
            if (result) {
                handleWin();
            }
            
            attemptsLabel.setText("Tentatives restantes : " + game.getRemainingAttempts());
            
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                "Tentative invalide : " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Game Over ! :(",
                JOptionPane.INFORMATION_MESSAGE);
            showGameEndDialog("La partie est terminée !");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur inattendue : " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showGameEndDialog(String message) {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), 
            "Fin de partie", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Panel pour le message
        JLabel messageLabel = new JLabel(message);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dialog.add(messageLabel, BorderLayout.NORTH);
        
        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JButton newGameButton = createStyledButton("Nouvelle partie", new Color(46, 125, 50));
        JButton difficultyButton = createStyledButton("Changer la difficulté", new Color(25, 118, 210));
        JButton quitButton = createStyledButton("Quitter", new Color(211, 47, 47));
        
        // Placement des boutons
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        buttonPanel.add(newGameButton, gbc);
        
        gbc.gridx = 1;
        buttonPanel.add(difficultyButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 15, 5); 
        buttonPanel.add(quitButton, gbc);
        
        JPanel bottomPadding = new JPanel();
        bottomPadding.setPreferredSize(new Dimension(1, 10));
        dialog.add(bottomPadding, BorderLayout.SOUTH);
        
        dialog.add(buttonPanel, BorderLayout.CENTER);

        newGameButton.addActionListener(e -> {
            dialog.dispose();
            restartGame();
        });
        
        difficultyButton.addActionListener(e -> {
            dialog.dispose();
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
                SwingUtilities.invokeLater(() -> {
                    MainWindow mainWindow = new MainWindow();
                    mainWindow.skipToGameSetup(playerName);
                    mainWindow.setVisible(true);
                });
            }
        });
        
        quitButton.addActionListener(e -> {
            dialog.dispose();
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        });
        
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);
    }
    
    private void restartGame() {
        // Arrêt du timer actuel
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        // Création d'une nouvelle instance du jeu avec le même joueur
        game = new Game(difficulty);
        game.startGame(playerName);
        
        // Réinitialisation du chronomètre
        timeSpent = 0;
        updateTimerLabel();
        gameTimer.start();
        
        // Recréation des composants
        remove(getComponent(0)); // Supprime le mainContent existant
        
        // Panel principal avec effet de profondeur
        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setBackground(new Color(240, 242, 245));
        
        // Recréation des panneaux
        createRulesPanel();
        createGameBoard();
        createControlPanel();
        
        // Ajout des composants avec le nouveau style
        mainContent.add(createStyledPanel(rulesPanel, "Règles du jeu"), BorderLayout.WEST);
        mainContent.add(createStyledPanel(gameBoard, "Plateau de jeu"), BorderLayout.CENTER);
        
        // Panel de droite avec contrôles et feedback
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        
        // Réinitialisation de la zone de feedback
        feedbackArea.setText("");
        JScrollPane feedbackScroll = new JScrollPane(feedbackArea);
        feedbackScroll.setPreferredSize(new Dimension(200, 100));
        
        rightPanel.add(createStyledPanel(controlPanel, "Contrôles"));
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(createStyledPanel(feedbackScroll, "Feedback"));
        mainContent.add(rightPanel, BorderLayout.EAST);
        
        add(mainContent, BorderLayout.CENTER);
        
        // Mise à jour de l'interface
        revalidate();
        repaint();
    }
    
    private void showHistory() {
        JDialog historyDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), 
            "Historique des parties", true);
        historyDialog.setLayout(new BorderLayout(10, 10));
        
        // Création du tableau d'historique
        String[] columnNames = {"Date", "Tentative", "Résultat"};
        List<Attempt> history = game.getCurrentPlayer().viewHistory();
        Object[][] data = new Object[history.size()][3];
        
        for (int i = 0; i < history.size(); i++) {
            Attempt attempt = history.get(i);
            data[i][0] = attempt.getTimestamp();
            data[i][1] = attempt.toString();
            data[i][2] = attempt.getResult() ? "✅" : "❌";
        }
        
        JTable historyTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(historyTable);
        
        // Score total
        JLabel scoreLabel = new JLabel("Score total : " + game.getCurrentPlayer().getScore());
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        historyDialog.add(scrollPane, BorderLayout.CENTER);
        historyDialog.add(scoreLabel, BorderLayout.SOUTH);
        
        historyDialog.setSize(400, 300);
        historyDialog.setLocationRelativeTo(this);
        historyDialog.setVisible(true);
    }
    
    private void updateTimerLabel() {
        int minutes = timeSpent / 60;
        int seconds = timeSpent % 60;
        timerLabel.setText(String.format("Temps : %d:%02d", minutes, seconds));
    }
    
    private void showHighScores() {
        JDialog scoresDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), 
            "Meilleurs Scores", true);
        scoresDialog.setLayout(new BorderLayout(10, 10));

        String[] columnNames = {"Joueur", "Score", "Difficulté", "Temps", "Date"};
        Object[][] data = new Object[highScores.size()][5];

        for (int i = 0; i < highScores.size(); i++) {
            HighScore hs = highScores.get(i);
            data[i][0] = hs.getPlayerName();
            data[i][1] = hs.getScore();
            data[i][2] = getDifficultyString(hs.getDifficulty());
            data[i][3] = String.format("%d:%02d", hs.getTimeSpent() / 60, hs.getTimeSpent() % 60);
            data[i][4] = hs.getDate();
        }

        JTable scoresTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(scoresTable);

        scoresDialog.add(scrollPane);
        scoresDialog.setSize(500, 300);
        scoresDialog.setLocationRelativeTo(this);
        scoresDialog.setVisible(true);
    }
    
    private String getDifficultyString(int difficulty) {
        return switch (difficulty) {
            case 1 -> "TC01 - Facile";
            case 2 -> "TC02 - Moyen";
            case 3 -> "Branche - Difficile";
            default -> "Inconnu";
        };
    }
    
    private int calculateTimeBonus(int timeSpent, int difficulty) {
        // Bonus de temps : plus rapide = plus de points
        int baseBonus = 1000;
        int timeLimit = switch (difficulty) {
            case 1 -> 180; // 3 minutes pour facile
            case 2 -> 120; // 2 minutes pour moyen
            case 3 -> 90;  // 1.5 minutes pour difficile
            default -> 150;
        };
        
        return Math.max(0, baseBonus - (timeSpent * baseBonus / (timeLimit * 2)));
    }

    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2, radius/2, radius/2, radius/2);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    // Méthode séparée pour le calcul de la moyenne
    private void updateAverageLabel(JLabel averageLabel) {
        double sum = 0;
        boolean allSelected = true;
        
        for (JComboBox<Grade> selector : gradeSelectors) {
            Grade selected = (Grade) selector.getSelectedItem();
            if (selected == null) {
                allSelected = false;
                break;
            }
            sum += selected.getValue();
        }
        
        if (allSelected) {
            double average = sum / 3.0;
            averageLabel.setText(String.format("Moyenne : %.1f", average));
        }
    }

    private void handleWin() {
        gameTimer.stop();
        int timeBonus = calculateTimeBonus(timeSpent, difficulty);
        int finalScore = game.getCurrentPlayer().getScore() + timeBonus;
        
        // Ajouter le score aux meilleurs scores
        highScores.add(new HighScore(playerName, finalScore, difficulty, timeSpent));
        Collections.sort(highScores);
        
        // Limiter la liste à 10 meilleurs scores
        if (highScores.size() > 10) {
            highScores = highScores.subList(0, 10);
        }
        
        // Afficher le message de victoire
        String message = String.format(
            "Félicitations ! Vous avez gagné !\n" +
            "Score : %d\n" +
            "Bonus temps : %d\n" +
            "Score final : %d\n" +
            "Temps : %d:%02d",
            game.getCurrentPlayer().getScore(),
            timeBonus,
            finalScore,
            timeSpent / 60,
            timeSpent % 60
        );
        
        JOptionPane.showMessageDialog(
            this,
            message,
            "Victoire !",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        showGameEndDialog("Voulez-vous commencer une nouvelle partie ?");
    }
} 