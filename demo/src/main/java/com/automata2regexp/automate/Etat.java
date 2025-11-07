package com.automata2regexp.automate;

import java.util.Objects;

public class Etat {
    private String nom;
    private boolean estInitial;
    private boolean estFinal;

    public Etat(String nom, boolean estInitinal, boolean estFinal){
        this.nom = nom;
        this.estInitial = estInitinal;
        this.estFinal = estFinal;
    }

    public String getNom(){
        return this.nom;
    }


    public boolean getEstInitial(){
        return this.estInitial;
    }

    public boolean getEstFinal(){
        return this.estFinal;
    }

    public void setEstInitial(boolean estInitial){
        this.estInitial = estInitial;
    }

    public void setEstFinal(boolean estFinal){
        this.estFinal = estFinal;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || this.getClass() != o.getClass()) return false;
        Etat etat = (Etat) o;
        return nom.equals(etat.nom);
    }

    @Override
    public int hashCode(){
        return Objects.hash(nom);
    }

    @Override
    public String toString(){
        return nom;
    }
}
