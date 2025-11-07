package com.automata2regexp.expression.operations;

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
}
