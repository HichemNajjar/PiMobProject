package com.free.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.free.entities.Certif;
import com.free.entities.Test;
import com.free.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestService {

    public static TestService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Test> listTests;


    private TestService() {
        cr = new ConnectionRequest();
    }

    public static TestService getInstance() {
        if (instance == null) {
            instance = new TestService();
        }
        return instance;
    }

    public ArrayList<Test> getAll() {
        listTests = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/test");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listTests = getList();
                }

                cr.removeResponseListener(this);
            }
        });

        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listTests;
    }

    private ArrayList<Test> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Test test = new Test(
                        (int) Float.parseFloat(obj.get("id").toString()),

                        makeCertif((Map<String, Object>) obj.get("certif")),
                        (String) obj.get("titre"),
                        (String) obj.get("categorie"),
                        (String) obj.get("description"),
                        Float.parseFloat(obj.get("prix").toString())

                );

                listTests.add(test);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listTests;
    }

    public Certif makeCertif(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }
        Certif certif = new Certif();
        certif.setId((int) Float.parseFloat(obj.get("id").toString()));
        certif.setNom((String) obj.get("nom"));
        return certif;
    }

    public int add(Test test) {

        cr = new ConnectionRequest();

        cr.setHttpMethod("POST");
        cr.setUrl(Statics.BASE_URL + "/test/add");

        cr.addArgument("certif", String.valueOf(test.getCertif().getId()));
        cr.addArgument("titre", test.getTitre());
        cr.addArgument("categorie", test.getCategorie());
        cr.addArgument("description", test.getDescription());
        cr.addArgument("prix", String.valueOf(test.getPrix()));


        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultCode = cr.getResponseCode();
                cr.removeResponseListener(this);
            }
        });
        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception ignored) {

        }
        return resultCode;
    }

    public int edit(Test test) {

        cr = new ConnectionRequest();
        cr.setHttpMethod("POST");
        cr.setUrl(Statics.BASE_URL + "/test/edit");
        cr.addArgument("id", String.valueOf(test.getId()));

        cr.addArgument("certif", String.valueOf(test.getCertif().getId()));
        cr.addArgument("titre", test.getTitre());
        cr.addArgument("categorie", test.getCategorie());
        cr.addArgument("description", test.getDescription());
        cr.addArgument("prix", String.valueOf(test.getPrix()));


        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultCode = cr.getResponseCode();
                cr.removeResponseListener(this);
            }
        });
        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception ignored) {

        }
        return resultCode;
    }

    public int delete(int testId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/test/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(testId));

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                cr.removeResponseListener(this);
            }
        });

        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cr.getResponseCode();
    }
}
