package com.automata2regexp.expression.operations;

import com.automata2regexp.expression.Expression;

public record Union(Expression gauche, Expression droite) implements Expression{

    @Override
    public String toString(){
        return gauche.toString() + "+" + droite.toString();
    }
    
}
