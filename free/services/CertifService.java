package com.free.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.free.entities.Certif;
import com.free.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CertifService {

    public static CertifService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Certif> listCertifs;


    private CertifService() {
        cr = new ConnectionRequest();
    }

    public static CertifService getInstance() {
        if (instance == null) {
            instance = new CertifService();
        }
        return instance;
    }

    public ArrayList<Certif> getAll() {
        listCertifs = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/certif");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listCertifs = getList();
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

        return listCertifs;
    }

    private ArrayList<Certif> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Certif certif = new Certif(
                        (int) Float.parseFloat(obj.get("id").toString()),

                        (String) obj.get("nom"),
                        (String) obj.get("descrip"),
                        (String) obj.get("image")

                );

                listCertifs.add(certif);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listCertifs;
    }

    public int add(Certif certif) {

        MultipartRequest cr = new MultipartRequest();
        cr.setFilename("file", "Certif.jpg");


        cr.setHttpMethod("POST");
        cr.setUrl(Statics.BASE_URL + "/certif/add");

        try {
            cr.addData("file", certif.getImage(), "image/jpeg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        cr.addArgumentNoEncoding("image", certif.getImage());

        cr.addArgumentNoEncoding("nom", certif.getNom());
        cr.addArgumentNoEncoding("descrip", certif.getDescrip());
        cr.addArgumentNoEncoding("image", certif.getImage());


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

    public int edit(Certif certif, boolean imageEdited) {

        MultipartRequest cr = new MultipartRequest();
        cr.setFilename("file", "Certif.jpg");

        cr.setHttpMethod("POST");
        cr.setUrl(Statics.BASE_URL + "/certif/edit");
        cr.addArgumentNoEncoding("id", String.valueOf(certif.getId()));

        if (imageEdited) {
            try {
                cr.addData("file", certif.getImage(), "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cr.addArgumentNoEncoding("image", certif.getImage());
        }

        cr.addArgumentNoEncoding("nom", certif.getNom());
        cr.addArgumentNoEncoding("descrip", certif.getDescrip());
        cr.addArgumentNoEncoding("image", certif.getImage());


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

    public int delete(int certifId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/certif/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(certifId));

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
