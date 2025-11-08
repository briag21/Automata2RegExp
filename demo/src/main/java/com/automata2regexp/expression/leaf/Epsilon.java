package com.automata2regexp.expression.leaf;

import javax.swing.tree.ExpandVetoException;

import com.automata2regexp.automate.Etat;
import com.automata2regexp.expression.Expression;

public class Epsilon implements Expression{
    public static final Epsilon INSTANCE = new Epsilon();

    private Epsilon(){

    }

    @Override
    public String toString(){
        return "ε";
    }

    @Override
    public boolean equals(Object obj){
        return obj instanceof Epsilon;
    }

    @Override 
    public int hashCode(){
        return 1;
    }
    
    @Override
    public Expression substituer(Etat aRemplacer, Expression remplacement) {
        return this;
    }
}
