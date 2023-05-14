package com.free.gui;

import com.codename1.ui.Button;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;
import com.free.gui.admin.AccueilAdmin;
import com.free.gui.user.AfficherToutCertif;

public class Login extends Form {

    public static Form loginForm;

    public Login() {
        super("Connexion", new BoxLayout(BoxLayout.Y_AXIS));
        loginForm = this;
        addGUIs();
    }

    private void addGUIs() {


        Button frontendBtn = new Button("Front");
        frontendBtn.addActionListener(l -> new AfficherToutCertif(this).show());
        this.add(frontendBtn);


        Button backendBtn = new Button("Back");
        backendBtn.addActionListener(l -> new AccueilAdmin(this).show());

        this.add(backendBtn);
    }

}
