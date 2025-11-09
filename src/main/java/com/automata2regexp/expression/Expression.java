package com.automata2regexp.expression;

import com.automata2regexp.automate.Etat;

public interface Expression {
    @Override
    String toString();

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    public Expression substituer(Etat aRemplacer, Expression remplacement);
}
