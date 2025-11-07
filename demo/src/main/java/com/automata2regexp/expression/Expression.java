package com.automata2regexp.expression;

public interface Expression {
    @Override
    String toString();

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();
}
