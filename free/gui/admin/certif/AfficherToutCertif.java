package com.free.gui.admin.certif;


import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.free.entities.Certif;
import com.free.services.CertifService;
import com.free.utils.Statics;

import java.util.ArrayList;

public class AfficherToutCertif extends Form {

    Form previous;

    Resources theme = UIManager.initFirstTheme("/theme");

    public static Certif currentCertif = null;
    Button addBtn;

    TextField searchTF;
    ArrayList<Component> componentModels;


    public AfficherToutCertif(Form previous) {
        super("Certifs", new BoxLayout(BoxLayout.Y_AXIS));
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


        ArrayList<Certif> listCertifs = CertifService.getInstance().getAll();
        componentModels = new ArrayList<>();

        searchTF = new TextField("", "Chercher certif par Nom");
        searchTF.addDataChangedListener((d, t) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    this.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            for (Certif certif : listCertifs) {
                if (certif.getNom().toLowerCase().startsWith(searchTF.getText().toLowerCase())) {
                    Component model = makeCertifModel(certif);
                    this.add(model);
                    componentModels.add(model);
                }
            }
            this.revalidate();
        });
        this.add(searchTF);


        if (listCertifs.size() > 0) {
            for (Certif certif : listCertifs) {
                Component model = makeCertifModel(certif);
                this.add(model);
                componentModels.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentCertif = null;
            new AjouterCertif(this).show();
        });

    }

    Label nomLabel, descripLabel, imageLabel;

    ImageViewer imageIV;


    private Container makeModelWithoutButtons(Certif certif) {
        Container certifModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        certifModel.setUIID("containerRounded");


        nomLabel = new Label("Nom : " + certif.getNom());
        nomLabel.setUIID("labelDefault");

        descripLabel = new Label("Descrip : " + certif.getDescrip());
        descripLabel.setUIID("labelDefault");

        imageLabel = new Label("Image : " + certif.getImage());
        imageLabel.setUIID("labelDefault");

        if (certif.getImage() != null) {
            String url = Statics.CERTIF_IMAGE_URL + certif.getImage();
            Image image = URLImage.createToStorage(
                    EncodedImage.createFromImage(theme.getImage("default.jpg").fill(1100, 500), false),
                    url,
                    url,
                    URLImage.RESIZE_SCALE
            );
            imageIV = new ImageViewer(image);
        } else {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));
        }
        imageIV.setFocusable(false);

        certifModel.addAll(
                imageIV,
                nomLabel, descripLabel
        );

        return certifModel;
    }

    Button editBtn, deleteBtn;
    Container btnsContainer;

    private Component makeCertifModel(Certif certif) {

        Container certifModel = makeModelWithoutButtons(certif);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonWhiteCenter");
        editBtn.addActionListener(action -> {
            currentCertif = certif;
            new ModifierCertif(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonWhiteCenter");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce certif ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = CertifService.getInstance().delete(certif.getId());

                if (responseCode == 200) {
                    currentCertif = null;
                    dlg.dispose();
                    certifModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du certif. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);


        certifModel.add(btnsContainer);

        return certifModel;
    }

}