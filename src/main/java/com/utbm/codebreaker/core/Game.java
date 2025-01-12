package com.utbm.codebreaker.core;

import com.utbm.codebreaker.model.Player;
import com.utbm.codebreaker.core.Puzzle;
import com.utbm.codebreaker.model.Grade;

public class Game {
    private int difficulty;
    private int maxAttempts;
    private int currentAttempt;
    private boolean isGameOver;
    private Puzzle currentPuzzle;
    private Player currentPlayer;

    public Game(int difficulty) {
        this.difficulty = difficulty;
        this.maxAttempts = calculateMaxAttempts(difficulty);
        this.currentAttempt = 0;
        this.isGameOver = false;
    }

    private int calculateMaxAttempts(int difficulty) {
        // Plus la difficulté est élevée, moins il y a de tentatives
        return switch (difficulty) {
            case 1 -> 12; // TC01 - Facile
            case 2 -> 8;  // TC02 - Moyen
            case 3 -> 6;  // Branche - Difficile
            default -> 10;
        };
    }

    public void startGame(String playerName) {
        if (playerName == null || playerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du joueur ne peut pas être vide");
        }

        currentPlayer = new Player(playerName);
        currentPuzzle = new Puzzle(difficulty);
        currentPuzzle.generatePuzzle();
        isGameOver = false;
        currentAttempt = 0;

        Grade[] solution = currentPuzzle.getSolution();
        System.out.println("Solution (début du jeu) : " + formatSolution(solution));
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public int getRemainingAttempts() {
        return maxAttempts - currentAttempt;
    }

    public boolean makeAttempt(Grade[] attempt) {
        if (attempt == null || attempt.length != 3) {
            throw new IllegalArgumentException("La tentative doit contenir exactement 3 notes");
        }

        if (isGameOver) {
            throw new IllegalStateException("Game over ! :(");
        }

        if (currentAttempt >= maxAttempts) {
            throw new IllegalStateException("Nombre maximum de tentatives atteint");
        }

        for (Grade grade : attempt) {
            if (grade == null) {
                throw new IllegalArgumentException("Les notes ne peuvent pas être nulles");
            }
        }

        boolean result = currentPuzzle.isSolution(attempt);
        currentPlayer.makeAttempt(attempt, result);
        currentAttempt++;

        if (result || currentAttempt >= maxAttempts) {
            isGameOver = true;
            Grade[] solution = currentPuzzle.getSolution();
            System.out.println("Solution : " + formatSolution(solution));
            if (result) {
                int score = calculateScore();
                currentPlayer.addScore(score);
            }
        }

        return result;
    }

    private int calculateScore() {
        return (maxAttempts - currentAttempt + 1) * difficulty * 100;
    }

    public Puzzle getCurrentPuzzle() {
        return currentPuzzle;
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    private String formatSolution(Grade[] solution) {
        StringBuilder sb = new StringBuilder();
        for (Grade grade : solution) {
            sb.append(grade.name()).append(" ");
        }
        return sb.toString().trim();
    }
}
