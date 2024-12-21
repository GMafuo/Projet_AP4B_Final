package com.utbm.codebreaker.gui;

import com.utbm.codebreaker.utils.Constants;
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel welcomePanel;
    private JPanel configPanel;
    
    public MainWindow() {
        // Configuration de base de la fenêtre
        setTitle("UTBM Code Breaker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Initialisation du CardLayout pour gérer les écrans
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Création des différents écrans
        createWelcomePanel();
        createConfigPanel();
        
        // Ajout des panels au CardLayout
        mainPanel.add(welcomePanel, "WELCOME");
        mainPanel.add(configPanel, "CONFIG");
        
        // Affichage du panel de bienvenue
        add(mainPanel);
        cardLayout.show(mainPanel, "WELCOME");
    }
    
    private void createWelcomePanel() {
        welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(new Color(240, 240, 245));
        
        // Panel principal
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(30, 50, 30, 50)
        ));
        
        // Titre
        JLabel welcomeLabel = new JLabel("UTBM Code Breaker", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(50, 50, 50));
        
        // Sous-titre
        JLabel subtitleLabel = new JLabel("Trouve ou je t'enfoure !", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI Light", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        
        // Bouton moderne
        JButton startButton = createStyledButton("Nouvelle Partie");
        startButton.addActionListener(e -> cardLayout.show(mainPanel, "CONFIG"));
        
        // Layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        mainContent.add(welcomeLabel, gbc);
        mainContent.add(Box.createVerticalStrut(10), gbc);
        mainContent.add(subtitleLabel, gbc);
        mainContent.add(Box.createVerticalStrut(30), gbc);
        mainContent.add(startButton, gbc);
        
        welcomePanel.add(mainContent);
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180));
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        
        // Effet de survol
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
        
        return button;
    }
    
    private void createConfigPanel() {
        configPanel = new JPanel(new GridBagLayout());
        configPanel.setBackground(new Color(240, 240, 245));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        
        JTextField nameField = createStyledTextField();
        JComboBox<String> difficultyCombo = createStyledComboBox();
        
        // Composants
        formPanel.add(createStyledLabel("Nom du joueur"));
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(createStyledLabel("Difficulté"));
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(difficultyCombo);
        formPanel.add(Box.createVerticalStrut(30));
        
        // Création des boutons avec leurs actions
        JButton startButton = createStyledButton("Commencer");
        startButton.addActionListener(e -> {
            String playerName = nameField.getText();
            int difficulty = difficultyCombo.getSelectedIndex() + 1; // +1 car l'index commence à 0
            startGame(playerName, difficulty);
        });
        
        JButton backButton = createStyledButton("Retour");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "WELCOME"));
        
        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(startButton);
        buttonPanel.add(backButton);
        formPanel.add(buttonPanel);
        
        configPanel.add(formPanel);
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }
    
    private JComboBox<String> createStyledComboBox() {
        String[] difficulties = {"TC01 - Facile", "TC02 - Moyen", "Branche - Difficile"};
        JComboBox<String> combo = new JComboBox<>(difficulties);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        return combo;
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(70, 70, 70));
        return label;
    }
    
    private void startGame(String playerName, int difficulty) {
        if (playerName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez entrer un nom de joueur", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Création et ajout du GameScreen
        GameScreen gameScreen = new GameScreen(playerName, difficulty);
        mainPanel.add(gameScreen, "GAME");
        cardLayout.show(mainPanel, "GAME");
    }
}