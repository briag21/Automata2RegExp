package com.automata2regexp.automate;

public record Transition(Etat depart, String symbole, Etat arrivee) {
    //record permet de ne pas écrire le constructeur, getters, setters...
}


