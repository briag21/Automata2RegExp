package com.automata2regexp.expression.leaf;

import com.automata2regexp.expression.Expression;

public record Symbole(String valeur) implements Expression{
    @Override
    public String toString(){
        return valeur;
    }
    
}
