package com.utbm.codebreaker.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Attempt> attemptHistory;
    private int score;

    public Player(String name) {
        this.name = name;
        this.attemptHistory = new ArrayList<>();
        this.score = 0;
    }

    public boolean makeAttempt(Grade[] code, boolean result) {
        Attempt attempt = new Attempt(code);
        attempt.setResult(result);
        attemptHistory.add(attempt);
        return result;
    }

    public List<Attempt> viewHistory() {
        return new ArrayList<>(attemptHistory);
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }
    
}