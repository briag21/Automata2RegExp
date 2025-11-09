package com.automata2regexp.expression.operations;

import com.automata2regexp.automate.Etat;
import com.automata2regexp.expression.Expression;

public record Etoile(Expression enfant) implements Expression {
    @Override
    public String toString(){
        boolean needsParent = (enfant instanceof Union) || (enfant instanceof Concatenation);

        String e = enfant.toString();

        return (needsParent ? "(" +e+")" : e)+"*";
    }

    @Override
    public Expression substituer(Etat aRemplacer, Expression remplacement) {
        
        Expression nouvelEnfant = enfant.substituer(aRemplacer, remplacement);
        return new Etoile(nouvelEnfant);
    }
    
}
