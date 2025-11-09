package com.automata2regexp.resolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.automata2regexp.automate.Automate;
import com.automata2regexp.automate.Etat;
import com.automata2regexp.automate.Transition;
import com.automata2regexp.expression.Expression;
import com.automata2regexp.expression.leaf.Epsilon;
import com.automata2regexp.expression.leaf.Symbole;
import com.automata2regexp.expression.leaf.Variable;
import com.automata2regexp.expression.leaf.Vide;
import com.automata2regexp.expression.operations.Concatenation;
import com.automata2regexp.expression.operations.Etoile;
import com.automata2regexp.expression.operations.Union;

public class SolverArden {

    private final Automate automate;
    private final List<Etat> etatsOrdonnes;
    private final Map<Etat, Equation> systeme;

    public SolverArden(Automate automate){
        this.automate = automate;
        this.etatsOrdonnes = new ArrayList<>(automate.getEtats());

        this.etatsOrdonnes.sort(
            (e1, e2)->{
                if(e1.getEstInitial()) return -1;
                if(e2.getEstInitial()) return 1;
                return(e1.getNom().compareTo(e2.getNom()));}
        );

        this.systeme = new HashMap<>();

        construireSysteme();

    }

    public void construireSysteme(){
        for(Etat etat_i : etatsOrdonnes){
            Expression A = Vide.INSTANCE;
            Expression B = Vide.INSTANCE;

            if(etat_i.getEstFinal()){
                B = Epsilon.INSTANCE;
            }

            for(Transition t : automate.getTransitions()){
                if(!t.depart().equals(etat_i)){
                    continue;
                }

                Etat etat_j = t.arrivee();
                Expression symbole = new Symbole(t.symbole());

                if(etat_i.equals(etat_j)){
                    A = union(A, symbole);
                } else{
                    Expression termSortant = new Concatenation(symbole, new Variable(etat_j));
                    B = union(B, termSortant);
                }
            }
            systeme.put(etat_i, new Equation(etat_i, A, B));

        }
    }

    public Expression resoudre(){
        Map<Etat, Expression> solutions = new HashMap<>();

        for(int i= etatsOrdonnes.size()-1; i>=0; i--){
            Etat etat_i = etatsOrdonnes.get(i);
            Equation eq_i = systeme.get(etat_i);

            Expression solution_i = new Concatenation(new Etoile(eq_i.getA()), eq_i.getB());

            solutions.put(etat_i, solution_i);

            for(int j=i -1; j>=0; j--){
                Etat etat_j = etatsOrdonnes.get(j);
                systeme.get(etat_j).substituer(etat_i, solution_i);
            }
        }

        return solutions.get(automate.getEtatInit());
    }

    private Expression union(Expression a, Expression b){
        if(a == null || a.equals(Vide.INSTANCE)) return b;
        if(b == null || b.equals(Vide.INSTANCE)) return a;
        return new Union(a, b);
    }
    
}
