package com.automata2regexp.ui_console;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


import com.automata2regexp.automate.Automate;
import com.automata2regexp.automate.Etat;
import com.automata2regexp.automate.Transition;

public class ConsoleUI {
    private Scanner scanner;

    private Map<String, Etat> mapEtats;

    public ConsoleUI(){
        this.scanner = new Scanner(System.in);
        this.mapEtats = new HashMap<>();
    }

    public Automate creerAutomate(){
        Automate automate = new Automate();
        System.out.println("Automate init.");

        creerEtats(automate);

        creerTransitions(automate);

        System.out.println("Automate créé.");

        afficherAutomate(automate);

        return automate;
    }   

    private void creerEtats(Automate automate) {
        while(true){
            System.out.println("Ajouter un état ? (o/n)");
            if(!scanner.nextLine().equalsIgnoreCase("o")){
                break;
            }

            System.out.println("    Nom de l'état : ");
            String nom = scanner.nextLine();

            System.out.println("    Est il un état initial ? (o/n)");
            boolean estInitial = scanner.nextLine().equalsIgnoreCase("o");

            System.out.println("    Est-il un état final ? (o/n) ");
            boolean estFinal = scanner.nextLine().equalsIgnoreCase("o");

            Etat new_etat = new Etat(nom, estInitial, estFinal);

            this.mapEtats.put(nom, new_etat);
            automate.ajouterEtat(new_etat);
        }
        
    }

    private void creerTransitions(Automate automate) {
        while (true) {
            System.out.println("Ajouter une transition ? (o/n)");
            if(!scanner.nextLine().equals("o")){
                break;
            }

            System.out.println("    Nom de l'état de depart :");
            String depart = scanner.nextLine();
            Etat etatD = mapEtats.get(depart);


            System.out.println("    Nom de l'état d'arrivée ?");
            String arrivee = scanner.nextLine();
            Etat etatA = mapEtats.get(arrivee);

            System.out.println("    Symbole ?");
            String symbole = scanner.nextLine();

            if(etatA==null || etatD==null){
                System.out.println("Un des deux états de la transition n'existe pas. Transition non ajoutée.");
            }

            Transition new_transition = new Transition(etatD, symbole, etatA);
            automate.ajouterTransition(new_transition);
            
        }

    }

    public void afficherAutomate(Automate automate) {
        System.out.println("\n--- RÉSUMÉ DE L'AUTOMATE ---");
        System.out.println("État initial: " + automate.getEtatInit());
        System.out.println("États finaux: " + automate.getEtatsFin());
        System.out.println("Transitions:");
        for (Transition t : automate.getTransitions()) {
            System.out.println(
                String.format("  (%s) --%s--> (%s)", t.depart(), t.symbole(), t.arrivee())
            );
        }
    }
}



