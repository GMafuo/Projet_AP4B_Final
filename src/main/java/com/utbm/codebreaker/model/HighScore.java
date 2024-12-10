package com.utbm.codebreaker.model;

import java.util.Date;

public class HighScore implements Comparable<HighScore> {
    private String playerName;
    private int score;
    private int difficulty;
    private Date date;
    private int timeSpent; // en secondes

    public HighScore(String playerName, int score, int difficulty, int timeSpent) {
        this.playerName = playerName;
        this.score = score;
        this.difficulty = difficulty;
        this.date = new Date();
        this.timeSpent = timeSpent;
    }

    @Override
    public int compareTo(HighScore other) {
        return Integer.compare(other.score, this.score); // Ordre décroissant
    }

    // Getters
    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public int getDifficulty() { return difficulty; }
    public Date getDate() { return date; }
    public int getTimeSpent() { return timeSpent; }
} 