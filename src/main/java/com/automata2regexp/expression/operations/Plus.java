package com.automata2regexp.expression.operations;

import com.automata2regexp.automate.Etat;
import com.automata2regexp.expression.Expression;

public record Plus(Expression enfant) implements Expression{
    @Override
    public String toString() {
        boolean needsParent = (enfant instanceof Union) || (enfant instanceof Concatenation);
        String e = enfant.toString();

        if(needsParent){
            return "("+e+")"+"+";
        }else{
            return e+"+";
        }

    }

    @Override
    public Expression substituer(Etat aRemplacer, Expression remplacement) {
        
        Expression nouvelEnfant = enfant.substituer(aRemplacer, remplacement);
        return new Plus(nouvelEnfant);
    }
}
