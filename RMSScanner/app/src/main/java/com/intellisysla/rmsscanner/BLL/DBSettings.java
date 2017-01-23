package com.intellisysla.rmsscanner.BLL;

/**
 * Created by alienware on 12/9/2016.
 */

public class DBSettings {
    private String db;
    private String user;
    private String pass;
    private String server;

    public DBSettings(String server, String db, String user, String pass) {
        this.db = db;
        this.pass = pass;
        this.user = user;
        this.server = server;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
