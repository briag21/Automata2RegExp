package com.automata2regexp.ui_graphique;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import com.automata2regexp.automate.Automate;
import com.automata2regexp.automate.Etat;
import com.automata2regexp.automate.Transition;

public class FenetreModification extends JDialog{
    private Automate automate;
    private FenetrePrincipale fenetrePrincipale;
    
    private DefaultListModel<Etat> modelListEtat; //coulisse
    private DefaultListModel<Transition> modelListTransition;

    private JList<Etat> listEtats; //vue user
    private JList<Transition> listTransitions;

    public FenetreModification(JFrame parent, Automate automate, FenetrePrincipale fenetrePrincipale){
        super(parent, "Modifier l'automate", true); //true == tant que cette fenetre n'est pas fermée l'intéraction avec la fenetre principale est interdite
        this.automate = automate;
        this.fenetrePrincipale = fenetrePrincipale;

        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        modelListEtat = new DefaultListModel<>(); //modèle de liste qui permet les opérations
        modelListTransition = new DefaultListModel<>();

        listEtats = new JList<>(modelListEtat); //l'afficheur de liste surveille donc maintenant le modèle de liste pour affichage
        listTransitions = new JList<>(modelListTransition);

        JPanel panelEtats = creerPanelList("Etats", listEtats);
        JPanel panelTransitions = creerPanelList("Transitions", listTransitions);

        JButton btnSupprEtat = new JButton("Supprimer état sélectionné");
        btnSupprEtat.addActionListener(e-> actionSupprimerEtat());
        panelEtats.add(btnSupprEtat, BorderLayout.SOUTH);

        JButton btnSupprTransition = new JButton("Supprimer transition sélectionné");
        btnSupprTransition.addActionListener(e-> actionSupprimerTransition());
        panelTransitions.add(btnSupprTransition, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelEtats, panelTransitions);
        splitPane.setResizeWeight(0.5);
        this.add(splitPane, BorderLayout.CENTER);
        
        refresh_list();
    }

    private JPanel creerPanelList(String titre, JList<?> list){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder());
        panel.add(new JLabel(titre, SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        return panel;
    }

    private void refresh_list(){

        modelListEtat.clear();
        for(Etat etat : automate.getEtats()){
            modelListEtat.addElement(etat);
        }

        modelListTransition.clear();
        for(Transition transition : automate.getTransitions()){
            modelListTransition.addElement(transition);
        }

    }

    private void actionSupprimerTransition(){
        Transition transitionSelectionne = listTransitions.getSelectedValue();
        if(transitionSelectionne == null){
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une transition à supprimer", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        automate.supprimerTransition(transitionSelectionne);
        refresh_list();

        fenetrePrincipale.rafraichirGrapheComplet(); // à implémenter
    }

    private void actionSupprimerEtat(){
        Etat etatSelectionne = listEtats.getSelectedValue();
        if(etatSelectionne == null){
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un état à supprimer", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean estUtilise = false;
        for(Transition t : automate.getTransitions()){
            if(t.depart().equals(etatSelectionne) || (t.arrivee().equals(etatSelectionne))){
                estUtilise = true;
                break;
            }
        }

        if(estUtilise){
            JOptionPane.showMessageDialog(this, "Impossible de supprimer un état impliqué dans une transition", "Erreur", JOptionPane.ERROR_MESSAGE);
        }else{
            automate.supprimerEtat(etatSelectionne);
            refresh_list();
            fenetrePrincipale.rafraichirGrapheComplet();
        }


    }

    
}
