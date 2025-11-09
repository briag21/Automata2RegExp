package com.automata2regexp.resolution;


import com.automata2regexp.automate.Automate;
import com.automata2regexp.automate.Etat;
import com.automata2regexp.automate.Transition;
import com.automata2regexp.expression.Expression;
import com.automata2regexp.simplificateur.Simplificateur;;
//On teste notre résolution en utilisant le Lemme d'Arden
public class test {
    public static void main(String[] args) {
        Etat etat1 = new Etat("1",true, false);
        Etat etat2 = new Etat("2", false, false);
        Etat etat3 = new Etat("3", false, true);

        Transition onetoone_1 = new Transition(etat1, "a", etat1);
        Transition onetoone_2 = new Transition(etat1, "b", etat1);
        Transition onetotwo_1 = new Transition(etat1, "a", etat2);
        Transition twototwo_1 = new Transition(etat2, "b", etat2);
        Transition twotothree_1 = new Transition(etat2, "b", etat3);





        Automate automate= new Automate();
        automate.ajouterEtat(etat1);
        automate.ajouterEtat(etat2);
        automate.ajouterEtat(etat3);

        automate.ajouterTransition(onetoone_1);
        automate.ajouterTransition(onetoone_2);
        automate.ajouterTransition(onetotwo_1);
        automate.ajouterTransition(twototwo_1);
        automate.ajouterTransition(twotothree_1);

        System.out.println(" \nMon automate :");
        System.out.println("    Etats:");
        System.out.println(automate.getEtats()+"\n");

        System.out.println("    Transitions :");
        System.out.println(automate.getTransitions()+"\n");


        SolverArden solver1 = new SolverArden(automate);
        solver1.construireSysteme();


        Expression solution = solver1.resoudre();
        System.out.println("    Solution brut :");
        System.out.println(solution.toString());

        Expression solutionSimp = Simplificateur.simplifier(solution);
        System.out.println(solutionSimp.toString());
        Expression solutionSimp2 = Simplificateur.simplifier(solution);
        System.out.println(solutionSimp2.toString());
    }
    
}
