package com.automata2regexp.ui_graphique;

import com.automata2regexp.automate.Automate;
import com.automata2regexp.automate.Etat;
import com.automata2regexp.automate.Transition;

import javax.swing.*;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class FenetrePrincipale extends JFrame{

    private Automate automate;

    private mxGraph graph;
    private mxGraphComponent graphComponent;
    private Map<Etat, Object> mapCellulesEtat;
    
    public FenetrePrincipale(){
        super("Automata to RegExp");

        this.automate = new Automate();
        this.graph = new mxGraph();
        this.mapCellulesEtat = new HashMap<>();

        this.graphComponent = new mxGraphComponent(graph);
        this.graphComponent.setConnectable(false);
        getContentPane().add(graphComponent, BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();

        JButton btnAjouterEtat = new JButton("Ajouter un état");
        JButton btnAjouterTransition = new JButton("Ajouter une transition");

        toolBar.add(btnAjouterEtat);
        toolBar.add(btnAjouterTransition);
        getContentPane().add(toolBar, BorderLayout.NORTH);

        btnAjouterEtat.addActionListener(e -> ajouterEtat());
        btnAjouterTransition.addActionListener(e -> ajouterTransition());


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void ajouterEtat(){
        String nom = JOptionPane.showInputDialog(this, "Nom du nouvel état :");
        if(nom==null || nom.trim().isEmpty()){
            return;
        }

        boolean estInitial = JOptionPane.showConfirmDialog(this, "Est-ce un état initial ?","Etat initial", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
        boolean estFinal = JOptionPane.showConfirmDialog(this, "Est-ce un état final ?","Etat final", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

        Etat nouvel_etat = new Etat(nom, estInitial, estFinal);
        automate.ajouterEtat(nouvel_etat);

        graph.getModel().beginUpdate();
        try {
            String style = "shape=ellipse;strokeColor=black;";
            if (estFinal) {
                style += "perimeter=ellipsePerimeter;shape=doubleEllipse;";
            }
            if (estInitial) {
                 style += "fontColor=blue;fontStyle=1";
            }

            Object celluleEtat = graph.insertVertex(graph.getDefaultParent(), null, nom, 100, 100, 80, 30, style);

            mapCellulesEtat.put(nouvel_etat, celluleEtat);


        } finally {
            graph.getModel().endUpdate();
        }
        appliquerLayout();
    }

    private void ajouterTransition(){
        if(automate.getEtats().size() < 1 ){
            JOptionPane.showMessageDialog(this, "Un état est nécessaire pour créer une transition");
            return;
        }

        Object[] tousLesEtats = automate.getEtats().toArray();

        Etat depart = (Etat) JOptionPane.showInputDialog(this, "Etat de départ : ", "Transition", JOptionPane.QUESTION_MESSAGE, null, tousLesEtats, tousLesEtats[0]);
        if(depart==null)return;

        String symbole = JOptionPane.showInputDialog(this, "Symbole de la transition");
        if(symbole==null){
            return;
        }
        Etat arrivee = (Etat) JOptionPane.showInputDialog(this, "Etat d'arrivée' : ", "Transition", JOptionPane.QUESTION_MESSAGE, null, tousLesEtats, tousLesEtats[0]);
        if(arrivee==null){
            return;
        }

        Transition nouvelle_transition = new Transition(depart, symbole, arrivee);
        automate.ajouterTransition(nouvelle_transition);

        graph.getModel().beginUpdate();
        try {
            Object celluleDepart = mapCellulesEtat.get(depart);
            Object celluleArrivee = mapCellulesEtat.get(arrivee);

            graph.insertEdge(graph.getDefaultParent(), null, symbole, celluleDepart, celluleArrivee);

        } finally {
            graph.getModel().endUpdate();
        }
        appliquerLayout();
    }

    //réorganise les différents états et transitions. Fonction à intégrer après ajout d'un nouvel état/transition
    private void appliquerLayout() {
        
        mxIGraphLayout layout = new mxHierarchicalLayout(graph);
        
        
        graph.getModel().beginUpdate();
        try {
            layout.execute(graph.getDefaultParent());
        } finally {
            graph.getModel().endUpdate();
        }
    }

    
}
