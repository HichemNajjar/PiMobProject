package com.free.entities;

public class Question {

    private int id;
    private Test test;
    private String question;
    private int note;
    private String reponse;

    public Question() {
    }

    public Question(int id, Test test, String question, int note, String reponse) {
        this.id = id;
        this.test = test;
        this.question = question;
        this.note = note;
        this.reponse = reponse;
    }

    public Question(Test test, String question, int note, String reponse) {
        this.test = test;
        this.question = question;
        this.note = note;
        this.reponse = reponse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }


}