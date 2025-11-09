package com.automata2regexp.expression.leaf;

import com.automata2regexp.automate.Etat;
import com.automata2regexp.expression.Expression;

public record Variable(Etat etat)  implements Expression {
    
    @Override
    public String toString(){
        return "L("+etat.getNom()+")";
    }

    @Override
    public Expression substituer(Etat aRemplacer, Expression remplacement){
        if(this.etat.equals(aRemplacer)){
            return remplacement;
        }else {
            return this;
        }


    }
}
