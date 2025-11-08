package com.automata2regexp.expression.operations;

import com.automata2regexp.automate.Etat;
import com.automata2regexp.expression.Expression;

public record Union(Expression gauche, Expression droite) implements Expression{

    @Override
    public String toString(){
        return gauche.toString() + "+" + droite.toString();
    }


    @Override
    public Expression substituer(Etat aRemplacer, Expression remplacement) {

        Expression nouveauGauche = gauche.substituer(aRemplacer, remplacement);
        Expression nouveauDroite = droite.substituer(aRemplacer, remplacement);
        return new Union(nouveauGauche, nouveauDroite);
    }
    
}
