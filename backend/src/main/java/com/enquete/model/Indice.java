package com.enquete.model;

public class Indice {
	private String nom;
	private String description;
	private boolean estCrucial;
	
	public Indice(String nom, String description, boolean estCrucial) {
		this.nom = nom;
		this.description = description;
		this.estCrucial = estCrucial;
	}
	
	public String getNom() {return nom;}
	public String getDescription() {return description;}
	public boolean isCrucial() {return estCrucial;}
	
	@Override
	public String toString() {
		return nom + ": " + description + (estCrucial ? "(preuve clé)" : "");
	}
	
	
}


