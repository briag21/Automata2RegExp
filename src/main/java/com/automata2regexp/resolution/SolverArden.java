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

            Equation eq_factorisee = refactoriserEquation(eq_i, etat_i); // permet de s'occuper du cas où B contient L_i dans L_i = A+B
            Expression A_Clean = eq_factorisee.getA();
            Expression B_Clean = eq_factorisee.getB();


            Expression solution_i = new Concatenation(new Etoile(A_Clean), B_Clean);

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

    private Equation refactoriserEquation(Equation eq, Etat etat){ //on factorise L_i tq L_i = A+B avec L_i pas dans B
        Expression A = eq.getA();
        Expression B =  eq.getB();

        ContainerLi result = extraireTermes(B, etat);

        Expression C = result.A();
        Expression B_propre = result.B();

        Expression A_new = union (A,C);
         
        return new Equation(etat, A_new, B_propre);
    }

    private ContainerLi extraireTermes(Expression expr, Etat etat){
        if(expr instanceof Variable v && v.etat().equals(etat)){
            return new ContainerLi(Epsilon.INSTANCE, Vide.INSTANCE);
        }

        if(expr instanceof Union u){
            ContainerLi gauche = extraireTermes(u.gauche(), etat);
            ContainerLi droite = extraireTermes(u.droite(), etat);
            return new ContainerLi(
                union(gauche.A(), droite.A()),
                union(gauche.B(), droite.B())
            );

        }

        if(expr instanceof Concatenation c){
            ContainerLi droite = extraireTermes(c.droite(), etat);

            //g*d=g*(A_d*L_i + B_d) = (g*A_d)*L_i + (g*B_d)
            Expression newA = concat(c.gauche(), droite.A());
            Expression newB = concat(c.gauche(), droite.B());

            return new ContainerLi(newA, newB);



        }

        //cas où L_i n'est pas dans B, B est déjà "propre"
        return new ContainerLi(Vide.INSTANCE, expr);
    }

    private Expression concat(Expression a, Expression b){
        if(a.equals(Vide.INSTANCE) || b.equals(Vide.INSTANCE))return Vide.INSTANCE;
        if(a.equals(Epsilon.INSTANCE)) return b;
        if(b.equals(Epsilon.INSTANCE)) return a;

        return new Concatenation(a, b);

    }
    
}
