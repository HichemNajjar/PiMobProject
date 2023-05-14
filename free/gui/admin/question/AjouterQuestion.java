package com.free.gui.admin.question;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.free.entities.Question;
import com.free.entities.Test;
import com.free.services.QuestionService;
import com.free.services.TestService;

import java.util.ArrayList;

public class AjouterQuestion extends Form {


    TextField questionTF;
    TextField noteTF;
    TextField reponseTF;
    Label questionLabel;
    Label noteLabel;
    Label reponseLabel;


    ArrayList<Test> listTests;
    PickerComponent testPC;
    Test selectedTest = null;


    Button manageButton;

    Form previous;

    public AjouterQuestion(Form previous) {
        super("Ajouter", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        addGUIs();
        addActions();


        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        String[] testStrings;
        int testIndex;
        testPC = PickerComponent.createStrings("").label("Test");
        listTests = TestService.getInstance().getAll();
        testStrings = new String[listTests.size()];
        testIndex = 0;
        for (Test test : listTests) {
            testStrings[testIndex] = test.getTitre();
            testIndex++;
        }
        if (listTests.size() > 0) {
            testPC.getPicker().setStrings(testStrings);
            testPC.getPicker().addActionListener(l -> selectedTest = listTests.get(testPC.getPicker().getSelectedStringIndex()));
        } else {
            testPC.getPicker().setStrings("");
        }


        questionLabel = new Label("Question : ");
        questionLabel.setUIID("labelDefault");
        questionTF = new TextField();
        questionTF.setHint("Tapez le question");


        noteLabel = new Label("Note : ");
        noteLabel.setUIID("labelDefault");
        noteTF = new TextField();
        noteTF.setHint("Tapez le note");


        reponseLabel = new Label("Reponse : ");
        reponseLabel.setUIID("labelDefault");
        reponseTF = new TextField();
        reponseTF.setHint("Tapez le reponse");


        manageButton = new Button("Ajouter");
        manageButton.setUIID("buttonWhiteCenter");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(


                questionLabel, questionTF,
                noteLabel, noteTF,
                reponseLabel, reponseTF,
                testPC,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        manageButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                int responseCode = QuestionService.getInstance().add(
                        new Question(


                                selectedTest,
                                questionTF.getText(),
                                (int) Float.parseFloat(noteTF.getText()),
                                reponseTF.getText()
                        )
                );
                if (responseCode == 200) {
                    Dialog.show("Succés", "Question ajouté avec succes", new Command("Ok"));
                    showBackAndRefresh();
                } else {
                    Dialog.show("Erreur", "Erreur d'ajout de question. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            }
        });
    }

    private void showBackAndRefresh() {
        ((AfficherToutQuestion) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {


        if (questionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Question vide", new Command("Ok"));
            return false;
        }


        if (noteTF.getText().equals("")) {
            Dialog.show("Avertissement", "Note vide", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(noteTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", noteTF.getText() + " n'est pas un nombre valide (note)", new Command("Ok"));
            return false;
        }


        if (reponseTF.getText().equals("")) {
            Dialog.show("Avertissement", "Reponse vide", new Command("Ok"));
            return false;
        }


        if (selectedTest == null) {
            Dialog.show("Avertissement", "Veuillez choisir un test", new Command("Ok"));
            return false;
        }


        return true;
    }
}