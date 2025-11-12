package com.automata2regexp.ui_graphique;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import com.automata2regexp.automate.Automate;
import com.automata2regexp.automate.Etat;
import com.automata2regexp.automate.Transition;
import com.automata2regexp.expression.Expression;
import com.automata2regexp.resolution.SolverArden;
import com.automata2regexp.simplificateur.Simplificateur;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
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
        this.graph.setCellsMovable(false);     
        this.graph.setCellsEditable(false);     
        this.graph.setCellsResizable(false);    
        this.graph.setCellsDeletable(false);   
        this.graph.setCellsBendable(false);  
        this.graph.setCellsDisconnectable(false);   

        this.mapCellulesEtat = new HashMap<>();

        this.graphComponent = new mxGraphComponent(graph);
        this.graphComponent.setConnectable(false);
        
        getContentPane().add(graphComponent, BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();

        JButton btnAjouterEtat = new JButton("Ajouter un état");
        JButton btnAjouterTransition = new JButton("Ajouter une transition");
        JButton btnModifierAutomate = new JButton("Modifier automate");
        JButton btnResoudre = new JButton("Convertir en RegExp");

        JButton btnCredit = new JButton("Crédit");

        toolBar.add(btnAjouterEtat);
        toolBar.add(btnAjouterTransition);
        toolBar.add(btnModifierAutomate);
        toolBar.add(btnResoudre);

        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(btnCredit);

        getContentPane().add(toolBar, BorderLayout.NORTH);

        btnAjouterEtat.addActionListener(e -> ajouterEtat());

        btnAjouterTransition.addActionListener(e -> ajouterTransition());

        btnModifierAutomate.addActionListener(e->{
            FenetreModification fenetreModif = new FenetreModification(this, automate, this);
            fenetreModif.setVisible(true);
        });

        btnResoudre.addActionListener(e -> {
            SolverArden solveur = new SolverArden(this.automate);
            Expression solutionBrut = solveur.resoudre();

            Expression solutionSimp = solutionBrut;
            String strAvant = solutionBrut.toString();
            String strApres = "";

            int maxPasses = 10;
            int passe =0;
            while(passe<maxPasses && !strAvant.equals(strApres)){
                if(passe>0){
                    strAvant=strApres;
                }

                solutionSimp = Simplificateur.simplifier(solutionSimp);
                strApres = solutionSimp.toString();
                passe++;
            }

            String message = "Solution Brute : \n" + solutionBrut +"\n" + "Solution Simplifiée : \n" + solutionSimp;
            
            JTextArea textArea = new JTextArea(message);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 200));
            JOptionPane.showMessageDialog(this, scrollPane, "Expression régulière", JOptionPane.INFORMATION_MESSAGE);

        });

        btnCredit.addActionListener(e -> {
            String message = "Briag GUEGAN-ROYAN \n Université de Rennes \n L3 2025"; 
            JTextArea textArea = new JTextArea(message);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 200));
            JOptionPane.showMessageDialog(this, scrollPane, "Crédit", JOptionPane.INFORMATION_MESSAGE);

        });;


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void ajouterEtat(){
        String nom = JOptionPane.showInputDialog(this, "Nom du nouvel état :");
        if(nom==null || nom.trim().isEmpty()){
            return;
        }

        boolean estInitial = false;
        if(automate.getEtatInit()==null){
            estInitial = JOptionPane.showConfirmDialog(this, "Est-ce un état initial ?","Etat initial", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
        }
        
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

    public void rafraichirGrapheComplet(){
        // Retirer l'ancien graphComponent
        getContentPane().remove(graphComponent);
        
        // Recréer complètement le graph
        this.graph = new mxGraph();
        this.graph.setCellsMovable(false);     
        this.graph.setCellsEditable(false);     
        this.graph.setCellsResizable(false);    
        this.graph.setCellsDeletable(false);   
        this.graph.setCellsBendable(false);  
        this.graph.setCellsDisconnectable(false);
        
        // Recréer le graphComponent
        this.graphComponent = new mxGraphComponent(graph);
        this.graphComponent.setConnectable(false);
        
        mapCellulesEtat.clear();

        graph.getModel().beginUpdate();
        try {
            for(Etat etat : automate.getEtats()){
                String style = "shape=ellipse;strokeColor=black;";
                if (etat.getEstFinal()) {
                    style += "perimeter=ellipsePerimeter;shape=doubleEllipse;";
                }
                if (etat.getEstInitial()) {
                    style += "fontColor=blue;fontStyle=1";
                }
                Object cellule = graph.insertVertex(graph.getDefaultParent(), null, etat.getNom(), 100, 100, 80, 30, style);
                mapCellulesEtat.put(etat, cellule);
            }

            for(Transition t : automate.getTransitions()){
                Object cellDepart = mapCellulesEtat.get(t.depart());
                Object cellArrivee = mapCellulesEtat.get(t.arrivee());

                if(cellDepart!=null && cellArrivee != null){
                    graph.insertEdge(graph.getDefaultParent(), null, t.symbole(), cellDepart, cellArrivee);
                }
            }

            // Appliquer le layout
            mxIGraphLayout layout = new mxHierarchicalLayout(graph);
            layout.execute(graph.getDefaultParent());
            
        } finally {
            graph.getModel().endUpdate();
        }
        
        // Rajouter le nouveau graphComponent
        getContentPane().add(graphComponent, BorderLayout.CENTER);
        
        // Forcer la validation et le repaint
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    
}
