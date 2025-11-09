package com.automata2regexp.resolution;

import com.automata2regexp.automate.Etat;
import com.automata2regexp.expression.Expression;

public class Equation {
    private Etat etatLi;
    private Expression A; //partie de l'équation où l'état fait appel à lui même
    private Expression B;

    public Equation(Etat etatLi, Expression A, Expression B){
        this.etatLi = etatLi;
        this.A = A;
        this.B = B;
    }

    public Expression getA(){
        return A;
    }
    
    public Expression getB(){
        return B;
    }

    public Etat getEtatLi(){
        return etatLi;
    }

    public void substituer(Etat variable, Expression remplacement){
        this.A = this.A.substituer(variable, remplacement);
        this.B = this.B.substituer(variable, remplacement);
    }
    
}
