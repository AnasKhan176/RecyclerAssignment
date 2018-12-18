package com.tekken.loadmoresample;

        import java.io.Serializable;

public class Model implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String id;


    public Model() {

    }
    public Model(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }


}