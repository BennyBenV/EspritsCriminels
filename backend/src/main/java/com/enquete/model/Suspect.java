package com.enquete.model;

import java.util.*;

public class Suspect {
    private String nom;
    private String alibi;
    private boolean ment;
    private List<Indice> indices;
    private List<QuestionReponse> questions;

    public Suspect(String nom, String alibi, boolean ment, List<Indice> indices, List<QuestionReponse> questions) {
        this.nom = nom;
        this.alibi = alibi;
        this.ment = ment;
        this.indices = indices;
        this.questions = questions;
    }

    public String getNom() { return nom; }
    public String getAlibi() { return alibi; }
    public boolean getMent() { return ment; }
    public List<Indice> getIndices() { return indices; }
    public List<QuestionReponse> getQuestions() { return questions; }

    public String repondre(String question) {
        return questions.stream()
                .filter(qr -> qr.getQuestion().equalsIgnoreCase(question))
                .map(QuestionReponse::getReponse)
                .findFirst()
                .orElse("Je ne sais pas.");
    }

    @Override
    public String toString() {
        return nom + " | Alibi : " + alibi + (ment ? " (semble mentir)" : " (semble honnête)");
    }
}