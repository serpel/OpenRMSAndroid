package com.intellisysla.rmsscanner.DAL;

/**
 * Created by alienware on 12/9/2016.
 */

public class Department {

    private int id;
    private String name;

    public Department(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Department){
            Department d = (Department) o;
            if(d.getName().equals(name) && d.getId() == id) return true;
        }
        return false;
    }
}
