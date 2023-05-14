package com.free.entities;

import com.free.utils.Statics;

public class Test implements Comparable<Test> {

    private int id;
    private Certif certif;
    private String titre;
    private String categorie;
    private String description;
    private float prix;

    public Test() {
    }

    public Test(int id, Certif certif, String titre, String categorie, String description, float prix) {
        this.id = id;
        this.certif = certif;
        this.titre = titre;
        this.categorie = categorie;
        this.description = description;
        this.prix = prix;
    }

    public Test(Certif certif, String titre, String categorie, String description, float prix) {
        this.certif = certif;
        this.titre = titre;
        this.categorie = categorie;
        this.description = description;
        this.prix = prix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Certif getCertif() {
        return certif;
    }

    public void setCertif(Certif certif) {
        this.certif = certif;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }


    @Override
    public int compareTo(Test test) {
        switch (Statics.compareVar) {
            case "Certif":
                return this.getCertif().getNom().compareTo(test.getCertif().getNom());
            case "Titre":
                return this.getTitre().compareTo(test.getTitre());
            case "Categorie":
                return this.getCategorie().compareTo(test.getCategorie());
            case "Description":
                return this.getDescription().compareTo(test.getDescription());
            case "Prix":
                return Float.compare(this.getPrix(), test.getPrix());

            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return titre;
    }
}