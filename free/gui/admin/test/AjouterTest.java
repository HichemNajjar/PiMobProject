package com.free.gui.admin.test;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.free.entities.Certif;
import com.free.entities.Test;
import com.free.services.CertifService;
import com.free.services.TestService;

import java.util.ArrayList;

public class AjouterTest extends Form {


    TextField titreTF;
    TextField categorieTF;
    TextField descriptionTF;
    TextField prixTF;
    Label titreLabel;
    Label categorieLabel;
    Label descriptionLabel;
    Label prixLabel;


    ArrayList<Certif> listCertifs;
    PickerComponent certifPC;
    Certif selectedCertif = null;


    Button manageButton;

    Form previous;

    public AjouterTest(Form previous) {
        super("Ajouter", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        addGUIs();
        addActions();


        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        String[] certifStrings;
        int certifIndex;
        certifPC = PickerComponent.createStrings("").label("Certif");
        listCertifs = CertifService.getInstance().getAll();
        certifStrings = new String[listCertifs.size()];
        certifIndex = 0;
        for (Certif certif : listCertifs) {
            certifStrings[certifIndex] = certif.getNom();
            certifIndex++;
        }
        if (listCertifs.size() > 0) {
            certifPC.getPicker().setStrings(certifStrings);
            certifPC.getPicker().addActionListener(l -> selectedCertif = listCertifs.get(certifPC.getPicker().getSelectedStringIndex()));
        } else {
            certifPC.getPicker().setStrings("");
        }


        titreLabel = new Label("Titre : ");
        titreLabel.setUIID("labelDefault");
        titreTF = new TextField();
        titreTF.setHint("Tapez le titre");


        categorieLabel = new Label("Categorie : ");
        categorieLabel.setUIID("labelDefault");
        categorieTF = new TextField();
        categorieTF.setHint("Tapez le categorie");


        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextField();
        descriptionTF.setHint("Tapez le description");


        prixLabel = new Label("Prix : ");
        prixLabel.setUIID("labelDefault");
        prixTF = new TextField();
        prixTF.setHint("Tapez le prix");


        manageButton = new Button("Ajouter");
        manageButton.setUIID("buttonWhiteCenter");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(


                titreLabel, titreTF,
                categorieLabel, categorieTF,
                descriptionLabel, descriptionTF,
                prixLabel, prixTF,
                certifPC,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        manageButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                int responseCode = TestService.getInstance().add(
                        new Test(


                                selectedCertif,
                                titreTF.getText(),
                                categorieTF.getText(),
                                descriptionTF.getText(),
                                Float.parseFloat(prixTF.getText())
                        )
                );
                if (responseCode == 200) {
                    Dialog.show("Succés", "Test ajouté avec succes", new Command("Ok"));
                    showBackAndRefresh();
                } else {
                    Dialog.show("Erreur", "Erreur d'ajout de test. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            }
        });
    }

    private void showBackAndRefresh() {
        ((AfficherToutTest) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {


        if (titreTF.getText().equals("")) {
            Dialog.show("Avertissement", "Titre vide", new Command("Ok"));
            return false;
        }


        if (categorieTF.getText().equals("")) {
            Dialog.show("Avertissement", "Categorie vide", new Command("Ok"));
            return false;
        }


        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Description vide", new Command("Ok"));
            return false;
        }


        if (prixTF.getText().equals("")) {
            Dialog.show("Avertissement", "Prix vide", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(prixTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", prixTF.getText() + " n'est pas un nombre valide (prix)", new Command("Ok"));
            return false;
        }


        if (selectedCertif == null) {
            Dialog.show("Avertissement", "Veuillez choisir un certif", new Command("Ok"));
            return false;
        }


        return true;
    }
}