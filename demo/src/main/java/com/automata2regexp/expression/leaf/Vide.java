package com.automata2regexp.expression.leaf;

import com.automata2regexp.expression.Expression;

public  class Vide implements Expression {
    public static final Vide INSTANCE = new Vide();

    private Vide(){

    }

    @Override
    public String toString(){
        return "Ø";
    }

    @Override
    public boolean equals(Object obj){
        return obj instanceof Vide;
    }

    @Override
    public int hashCode(){
        return 0;
    }
    
}
