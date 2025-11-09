package com.automata2regexp.simplificateur;

import com.automata2regexp.expression.Expression;
import com.automata2regexp.expression.leaf.Epsilon;
import com.automata2regexp.expression.leaf.Symbole;
import com.automata2regexp.expression.leaf.Vide;
import com.automata2regexp.expression.operations.Concatenation;
import com.automata2regexp.expression.operations.Etoile;
import com.automata2regexp.expression.operations.Plus;
import com.automata2regexp.expression.operations.Union;

public class Simplificateur {
    public static Expression simplifier(Expression expr){
        if(expr==null) return Vide.INSTANCE;

        if(expr instanceof Symbole || expr instanceof Epsilon || expr instanceof Vide){
            return expr;
        }

        if(expr instanceof Union){
            Expression g = simplifier(((Union) expr).gauche());
            Expression d = simplifier(((Union) expr).droite());

            return simplifierUnion(g,d);

        } else if(expr instanceof Concatenation){   
            Expression g = simplifier(((Concatenation) expr).gauche());
            Expression d = simplifier(((Concatenation) expr).droite());

            return simplifierConcatenation(g,d);

        }  else if(expr instanceof Etoile){
            Expression e = simplifier(((Etoile) expr).enfant());

            return simplifierEtoile(e);
        }

        return expr;


    }

    private static Expression simplifierUnion(Expression g, Expression d){
        //Vide+A=A
        if(g.equals(Vide.INSTANCE)) return d;

        //A+Vide=A
        if(d.equals(Vide.INSTANCE)) return g;

        //A+A=A
        if(g.equals(d)) return g;


        return new Union(g, d);
    }

    private static Expression simplifierConcatenation(Expression g, Expression d){
        //A.Vide = Vide Vide.A = Vide
        if(g.equals(Vide.INSTANCE) || d.equals(Vide.INSTANCE)) return Vide.INSTANCE;

        //A.Epsilon = A
        if(g.equals(Epsilon.INSTANCE)) return d;

        //Epsilon.A = A
        if(d.equals(Epsilon.INSTANCE)) return g;

        //A*A=A+
        if(g instanceof  Etoile){
            Expression contenuEtoile = ((Etoile) g).enfant();
            if(contenuEtoile.equals(d)){
                return new Plus(contenuEtoile);
            }
        }

        return new Concatenation(g,d);

        
    }

    private static Expression simplifierEtoile(Expression e){
        if(e instanceof Etoile) return e;

        if(e.equals(Vide.INSTANCE)) return Epsilon.INSTANCE;

        if(e.equals(Epsilon.INSTANCE)) return Epsilon.INSTANCE;

        return new Etoile(e);
    }

    
}
