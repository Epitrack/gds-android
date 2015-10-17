package com.epitrack.guardioes.model;

import android.graphics.Bitmap;

public class User {

    // TODO: This is for stub only.

    private int image;
    private String nick;
    private String email;
    private String password;
    private String client = "api";
    private String dob;
    private String gender;
    private String app_token = "d41d8cd98f00b204e9800998ecf8427e";
    private double lon;
    private double lat;
    private String race;
    private String platform = "android";
    private String type;
    private String zip;
    private String id;
    private String picture;
    private String user_token;
    private String tw;
    private String fb;
    private String gl;
    private int imageResource;
    private Bitmap bitmapImage;
    private String avatar;

    public User() {

    }

    public User(int image, String nick, String email) {
        this.image = image;
        this.nick = nick;
        this.email = email;
    }

    public User(int image, String nick, String email, String id, String dob, String race, String gender) {
        this.image = image;
        this.nick = nick;
        this.email = email;
        this.id = id;
        this.dob = dob;
        this.race = race;
        this.gender = gender;
    }

    public User(int image, String nick, String email, String id, String dob, String race, String gender, String picture) {
        this.image = image;
        this.nick = nick;
        this.email = email;
        this.id = id;
        this.dob = dob;
        this.race = race;
        this.gender = gender;
        this.picture = picture;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getApp_token() {
        return app_token;
    }

    public void setApp_token(String app_token) {
        this.app_token = app_token;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public String getGl() {
        return gl;
    }

    public void setGl(String gl) {
        this.gl = gl;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getTw() {
        return tw;
    }

    public void setTw(String tw) {
        this.tw = tw;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
