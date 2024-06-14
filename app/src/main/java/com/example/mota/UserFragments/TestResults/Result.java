package com.example.mota.UserFragments.TestResults;

public class Result {
    private String question;
    private String chosenAnswer;
    private String check;
    private String correctAnswer;

    public Result(String question, String chosenAnswer, String check, String correctAnswer) {
        this.question = question;
        this.chosenAnswer = chosenAnswer;
        this.check = check;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChosenAnswer() {
        return chosenAnswer;
    }

    public void setChosenAnswer(String chosenAnswer) {
        this.chosenAnswer = chosenAnswer;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
