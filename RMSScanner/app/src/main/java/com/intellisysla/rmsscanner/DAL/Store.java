package com.intellisysla.rmsscanner.DAL;

/**
 * Created by alienware on 12/11/2016.
 */

public class Store {

    private String ip;
    private String db;
    private String user;
    private String password;
    private String name;
    private String location;
    private String username;

    public Store(String ip, String db, String user, String password, String name, String username, String location) {
        this.ip = ip;
        this.db = db;
        this.user = user;
        this.password = password;
        this.name = name;
        this.location = location;
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Store){
            Store d = (Store) o;
            if(d.getName().equals(name)) return true;
        }
        return false;
    }

}
