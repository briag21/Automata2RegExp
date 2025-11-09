package com.automata2regexp.automate;

import java.util.HashSet;
import java.util.Set;

public class Automate{
    private Set<Etat> etats;
    private Set<Transition> transitions;

    private Etat etatInit;
    private Set<Etat> etatFin;

    public Automate(){
        this.etats = new HashSet<>();
        this.transitions = new HashSet<>();
        this.etatFin = new HashSet<>();
        this.etatInit = null;
    }

    public void ajouterEtat(Etat etat){
        this.etats.add(etat);
        if(etat.getEstInitial()){
            this.etatInit =etat;
        }
        if(etat.getEstFinal()){
            this.etatFin.add(etat);
        }
    }

    public void supprimerEtat(Etat etat){
        if(etat == null)return;

        etats.remove(etat);
        etatFin.remove(etat);

        if(etat.equals(this.etatInit)){
            this.etatInit=null;
        }


    }

    public void supprimerTransition(Transition transition){
        if(transition==null)return;
        transitions.remove(transition);
    }

    public void ajouterTransition(Transition transition){
        if(etats.contains(transition.depart()) && etats.contains(transition.arrivee())){
            this.transitions.add(transition);
        }
    }

    public Set<Etat> getEtats(){
        return this.etats;
    }
    public Set<Transition> getTransitions(){
        return this.transitions;
    }

    public Etat getEtatInit(){
        return this.etatInit;
    }

    public Set<Etat> getEtatsFin(){
        return this.etatFin;
    }
}