package com.free.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.free.entities.Question;
import com.free.entities.Test;
import com.free.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionService {

    public static QuestionService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Question> listQuestions;


    private QuestionService() {
        cr = new ConnectionRequest();
    }

    public static QuestionService getInstance() {
        if (instance == null) {
            instance = new QuestionService();
        }
        return instance;
    }

    public ArrayList<Question> getAll() {
        listQuestions = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/question");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listQuestions = getList();
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

        return listQuestions;
    }

    private ArrayList<Question> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Question question = new Question(
                        (int) Float.parseFloat(obj.get("id").toString()),

                        makeTest((Map<String, Object>) obj.get("test")),
                        (String) obj.get("question"),
                        (int) Float.parseFloat(obj.get("note").toString()),
                        (String) obj.get("reponse")

                );

                listQuestions.add(question);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listQuestions;
    }

    public Test makeTest(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }
        Test test = new Test();
        test.setId((int) Float.parseFloat(obj.get("id").toString()));
        test.setTitre((String) obj.get("titre"));
        return test;
    }

    public int add(Question question) {

        cr = new ConnectionRequest();

        cr.setHttpMethod("POST");
        cr.setUrl(Statics.BASE_URL + "/question/add");

        cr.addArgument("test", String.valueOf(question.getTest().getId()));
        cr.addArgument("question", question.getQuestion());
        cr.addArgument("note", String.valueOf(question.getNote()));
        cr.addArgument("reponse", question.getReponse());


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

    public int edit(Question question) {

        cr = new ConnectionRequest();
        cr.setHttpMethod("POST");
        cr.setUrl(Statics.BASE_URL + "/question/edit");
        cr.addArgument("id", String.valueOf(question.getId()));

        cr.addArgument("test", String.valueOf(question.getTest().getId()));
        cr.addArgument("question", question.getQuestion());
        cr.addArgument("note", String.valueOf(question.getNote()));
        cr.addArgument("reponse", question.getReponse());


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

    public int delete(int questionId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/question/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(questionId));

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
