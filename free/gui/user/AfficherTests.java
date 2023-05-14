package com.free.gui.user;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.free.entities.Question;
import com.free.entities.Test;
import com.free.services.QuestionService;
import com.free.services.TestService;
import com.free.utils.Statics;

import java.util.ArrayList;
import java.util.Collections;

public class AfficherTests extends Form {

    public static Question currentQuestion = null;

    Form previous;
    public static Test currentTest = null;
    PickerComponent sortPicker;
    ArrayList<Component> componentModels;

    public AfficherTests(Form previous) {
        super("Tests du certif : " + AfficherToutCertif.currentCertif, new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        addGUIs();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        this.refreshTheme();
    }

    ArrayList<Question> listQuestions;

    private void addGUIs() {
        ArrayList<Test> listTests = TestService.getInstance().getAll();
        listQuestions = QuestionService.getInstance().getAll();

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
                if (AfficherToutCertif.currentCertif.getId() == test.getCertif().getId()) {
                    Component model = makeTestModel(test);
                    this.add(model);
                    componentModels.add(model);
                }
            }
            this.revalidate();
        });
        this.add(sortPicker);

        if (listTests.size() > 0) {
            for (Test test : listTests) {
                if (AfficherToutCertif.currentCertif.getId() == test.getCertif().getId()) {
                    Component model = makeTestModel(test);
                    this.add(model);
                    componentModels.add(model);
                }
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    Label certifLabel, titreLabel, categorieLabel, descriptionLabel, prixLabel;


    private Component makeTestModel(Test test) {

        Container testModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        testModel.setUIID("containerRounded");

        certifLabel = new Label(String.valueOf(test.getCertif()));
        certifLabel.setUIID("labelCenter");

        titreLabel = new Label("Titre : " + test.getTitre());
        titreLabel.setUIID("labelDefault");

        categorieLabel = new Label("Categorie : " + test.getCategorie());
        categorieLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : " + test.getDescription());
        descriptionLabel.setUIID("labelDefault");

        prixLabel = new Label("Prix : " + test.getPrix());
        prixLabel.setUIID("labelDefault");

        testModel.addAll(certifLabel, titreLabel, categorieLabel, descriptionLabel, prixLabel);


        testModel.add(new Label("Questions : "));
        for (Question question : listQuestions) {
            if (question.getTest().getId() == question.getId()) {
                testModel.add(makeQuestionModel(question));
            }
        }

        return testModel;
    }

    private Component makeQuestionModel(Question question) {

        Container questionModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        questionModel.setUIID("containerRounded");

        Label testLabel, questionLabel, noteLabel, reponseLabel;

        testLabel = new Label("Test : " + question.getTest());
        testLabel.setUIID("labelDefault");

        questionLabel = new Label("Question : " + question.getQuestion());
        questionLabel.setUIID("labelDefault");

        noteLabel = new Label("Note : " + question.getNote());
        noteLabel.setUIID("labelDefault");

        reponseLabel = new Label("Reponse : " + question.getReponse());
        reponseLabel.setUIID("labelDefault");

        questionModel.addAll(testLabel, questionLabel, noteLabel, reponseLabel);

        return questionModel;
    }

}