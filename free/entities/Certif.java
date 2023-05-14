package com.free.entities;

public class Certif {

    private int id;
    private String nom;
    private String descrip;
    private String image;

    public Certif() {
    }

    public Certif(int id, String nom, String descrip, String image) {
        this.id = id;
        this.nom = nom;
        this.descrip = descrip;
        this.image = image;
    }

    public Certif(String nom, String descrip, String image) {
        this.nom = nom;
        this.descrip = descrip;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public String toString() {
        return nom;
    }
}