package com.automata2regexp.expression.operations;

import com.automata2regexp.automate.Etat;
import com.automata2regexp.expression.Expression;

public record Concatenation(Expression gauche, Expression droite) implements Expression{
    @Override
    public String toString(){

        String g = (gauche instanceof Union) 
                   ? "(" + gauche.toString() + ")" 
                   : gauche.toString();
        
        String d = (droite instanceof Union) 
                   ? "(" + droite.toString() + ")" 
                   : droite.toString();

        return g+d;
    }


@Override
public Expression substituer(Etat aRemplacer, Expression remplacement) {

    Expression nouveauGauche = gauche.substituer(aRemplacer, remplacement);
    Expression nouveauDroite = droite.substituer(aRemplacer, remplacement);

    return new Concatenation(nouveauGauche, nouveauDroite);
}
}
