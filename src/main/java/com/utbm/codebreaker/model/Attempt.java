package com.utbm.codebreaker.model;

import java.util.Date;

public class Attempt {
    private final Grade[] code;
    private final Date timestamp;
    private boolean result;
    private String feedback;

    public Attempt(Grade[] code) {
        this.code = code.clone();
        this.timestamp = new Date();
        this.result = false;
        this.feedback = "";
    }

    public Grade[] getCode() {
        return code.clone();
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Grade grade : code) {
            sb.append(grade.name()).append(" ");
        }
        return sb.toString().trim() + " - " + (result ? "Correct" : "Incorrect");
    }
}