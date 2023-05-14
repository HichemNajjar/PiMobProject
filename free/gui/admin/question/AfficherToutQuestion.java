package com.free.gui.admin.question;


import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.free.entities.Question;
import com.free.services.QuestionService;

import java.util.ArrayList;

public class AfficherToutQuestion extends Form {

    Form previous;

    public static Question currentQuestion = null;
    Button addBtn;

    TextField searchTF;
    ArrayList<Component> componentModels;


    public AfficherToutQuestion(Form previous) {
        super("Questions", new BoxLayout(BoxLayout.Y_AXIS));
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


        ArrayList<Question> listQuestions = QuestionService.getInstance().getAll();
        componentModels = new ArrayList<>();

        searchTF = new TextField("", "Chercher question par Question");
        searchTF.addDataChangedListener((d, t) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    this.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            for (Question question : listQuestions) {
                if (question.getQuestion().toLowerCase().startsWith(searchTF.getText().toLowerCase())) {
                    Component model = makeQuestionModel(question);
                    this.add(model);
                    componentModels.add(model);
                }
            }
            this.revalidate();
        });
        this.add(searchTF);


        if (listQuestions.size() > 0) {
            for (Question question : listQuestions) {
                Component model = makeQuestionModel(question);
                this.add(model);
                componentModels.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentQuestion = null;
            new AjouterQuestion(this).show();
        });

    }

    Label  questionLabel, noteLabel, reponseLabel;


    private Container makeModelWithoutButtons(Question question) {
        Container questionModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        questionModel.setUIID("containerRounded2");

        questionLabel = new Label("Question : " + question.getQuestion());
        questionLabel.setUIID("labelDefault");

        noteLabel = new Label("Note : " + question.getNote());
        noteLabel.setUIID("labelDefault");

        reponseLabel = new Label("Reponse : " + question.getReponse());
        reponseLabel.setUIID("labelDefault");


        questionModel.addAll(
                questionLabel, noteLabel, reponseLabel
        );

        return questionModel;
    }

    Button editBtn, deleteBtn;
    Container btnsContainer;

    private Component makeQuestionModel(Question question) {

        Container questionModel = makeModelWithoutButtons(question);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonWhiteCenter");
        editBtn.addActionListener(action -> {
            currentQuestion = question;
            new ModifierQuestion(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonWhiteCenter");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce question ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = QuestionService.getInstance().delete(question.getId());

                if (responseCode == 200) {
                    currentQuestion = null;
                    dlg.dispose();
                    questionModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du question. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);


        questionModel.add(btnsContainer);

        return questionModel;
    }

}