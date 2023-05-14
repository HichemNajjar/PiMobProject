package com.free.gui.admin.test;


import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.free.entities.Test;
import com.free.services.TestService;
import com.free.utils.Statics;

import java.util.ArrayList;
import java.util.Collections;

public class AfficherToutTest extends Form {

    Form previous;

    public static Test currentTest = null;
    Button addBtn;


    PickerComponent sortPicker;
    ArrayList<Component> componentModels;

    public AfficherToutTest(Form previous) {
        super("Tests", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        addGUIs();
        addActions();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    private void addGUIs() {
        addBtn = new Button("Ajouter");
        addBtn.setUIID("buttonWhiteCenter");
        this.add(addBtn);


        ArrayList<Test> listTests = TestService.getInstance().getAll();

        componentModels = new ArrayList<>();

        sortPicker = PickerComponent.createStrings("Certif", "Titre", "Categorie", "Description", "Prix").label("Trier par");
        sortPicker.getPicker().setSelectedString("");
        sortPicker.getPicker().addActionListener((l) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    this.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            Statics.compareVar = sortPicker.getPicker().getSelectedString();
            Collections.sort(listTests);
            for (Test test : listTests) {
                Component model = makeTestModel(test);
                this.add(model);
                componentModels.add(model);
            }
            this.revalidate();
        });
        this.add(sortPicker);

        if (listTests.size() > 0) {
            for (Test test : listTests) {
                Component model = makeTestModel(test);
                this.add(model);
                componentModels.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentTest = null;
            new AjouterTest(this).show();
        });

    }

    Label certifLabel, titreLabel, categorieLabel, descriptionLabel, prixLabel;


    private Container makeModelWithoutButtons(Test test) {
        Container testModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        testModel.setUIID("containerRounded");


        certifLabel = new Label("Certif : " + test.getCertif());
        certifLabel.setUIID("labelDefault");

        titreLabel = new Label("Titre : " + test.getTitre());
        titreLabel.setUIID("labelDefault");

        categorieLabel = new Label("Categorie : " + test.getCategorie());
        categorieLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : " + test.getDescription());
        descriptionLabel.setUIID("labelDefault");

        prixLabel = new Label("Prix : " + test.getPrix());
        prixLabel.setUIID("labelDefault");

        certifLabel = new Label("Certif : " + test.getCertif().getNom());
        certifLabel.setUIID("labelDefault");


        testModel.addAll(

                certifLabel, titreLabel, categorieLabel, descriptionLabel, prixLabel
        );

        return testModel;
    }

    Button editBtn, deleteBtn;
    Container btnsContainer;

    private Component makeTestModel(Test test) {

        Container testModel = makeModelWithoutButtons(test);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonWhiteCenter");
        editBtn.addActionListener(action -> {
            currentTest = test;
            new ModifierTest(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonWhiteCenter");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce test ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = TestService.getInstance().delete(test.getId());

                if (responseCode == 200) {
                    currentTest = null;
                    dlg.dispose();
                    testModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du test. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);


        testModel.add(btnsContainer);

        return testModel;
    }

}