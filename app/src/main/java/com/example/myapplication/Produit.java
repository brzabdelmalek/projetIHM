package com.example.myapplication;

class Produit {
    private int id;
    private String nom;
    private int utilisateur;
    private float benef;

    public Produit(int id, String nom, int utilisateur, float benef) {
        this.id = id;
        this.nom = nom;
        this.utilisateur = utilisateur;
        this.benef = benef;
    }

    public float getBenef() {
        return benef;
    }

    public void setBenef(float benef) {
        this.benef = benef;
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

    public int getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(int utilisateur) {
        this.utilisateur = utilisateur;
    }
}
