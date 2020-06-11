package com.nasande.pharmaciemayamaya;

import java.util.ArrayList;

public class Node {
    ArrayList<Type> type;
    private ArrayList<Title> title;
    private ArrayList<Fichier> field_image;
    private ArrayList<Body> body;






    public Node(ArrayList<Title> title, ArrayList<Fichier> field_image,ArrayList<Body> body) {

        ArrayList<Type> type = new ArrayList<>();
        type.add(0,new Type("article"));


        this.title = title;


        this.type = type;



        this.field_image = field_image;

        this.body = body;
    }



    public ArrayList<Title> getTitle() {
        return title;
    }

    public void setTitle(ArrayList<Title> title) {
        this.title = title;
    }

    public ArrayList<Fichier> getField_image() {
        return field_image;
    }

    public ArrayList<Body> getBody() {
        return body;
    }

    public void setBody(ArrayList<Body> body) {
        this.body = body;
    }

    public void setField_Image(ArrayList<Fichier> field_image) {
        this.field_image = field_image;
    }
}
