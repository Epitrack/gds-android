package com.epitrack.guardioes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Parcel(Parcel.Serialization.BEAN)
public class User {

    private int image;
    private String nick;
    private String email;
    private String password;
    private String client = "api";
    private String path;
    private String dob;
    private String gender;
    private String app_token = "d41d8cd98f00b204e9800998ecf8427e";
    private double lon;
    private double lat;
    private String race;
    private String platform = "android";
    private String type;
    private String id;
    private String user_token;
    private String tw;
    private String fb;
    private String gl;
    private List<String> hashtags = new ArrayList<>();
    private String relationship;
    private String versionBuild;
    private String gcmToken;
    private String country;
    private String state;
    private int profile;
    private int energy = 10;
    private int level = 1;
    private Map<Integer, Boolean> pieceMap = new HashMap<>();

    public User() {

        pieceMap.put(0, false);
        pieceMap.put(1, false);
        pieceMap.put(2, false);
        pieceMap.put(3, false);
        pieceMap.put(4, false);
        pieceMap.put(5, false);
        pieceMap.put(6, false);
        pieceMap.put(7, false);
        pieceMap.put(8, false);
    }

    public User(String id) {
        this.id = id;
    }

    public User(String nick, String email, String id, String dob, String race, String gender) {
        this.nick = nick;
        this.email = email;
        this.id = id;
        this.dob = dob;
        this.race = race;
        this.gender = gender;
    }

    public User(String nick, String email, String id, String dob, String race, String gender, String relationship) {
        this.nick = nick;
        this.email = email;
        this.id = id;
        this.dob = dob;
        this.race = race;
        this.gender = gender;
        this.relationship = relationship;
    }

    public User(String nick, String email, String id, String dob, String race, String gender, int image) {
        this.nick = nick;
        this.email = email;
        this.id = id;
        this.dob = dob;
        this.race = race;
        this.gender = gender;
        this.image = image;
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

    @JsonIgnore
    public String getAppToken() {
        return app_token;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserToken() {
        return user_token;
    }

    public void setUserToken(String user_token) {
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

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(final List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getVersionBuild() {
        return versionBuild;
    }

    public void setVersionBuild(String versionBuild) {
        this.versionBuild = versionBuild;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public int getEnergy() {
        return energy;
    }

    public void addEnergy(final int amount) {
        this.energy += amount;

        if (energy > 10) {
            this.energy = 10;
        }
    }

    public void setEnergy(final int energy) {
        this.energy = energy;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(final int level) {
        this.level = level;
    }

    public Map<Integer, Boolean> getPieceMap() {
        return pieceMap;
    }

    public void setPieceMap(final Map<Integer, Boolean> pieceMap) {
        this.pieceMap = pieceMap;
    }

    public void resetPieceMap() {

        final Map<Integer, Boolean> pieceMap = new LinkedHashMap<>(9);
        pieceMap.put(0, false);
        pieceMap.put(1, false);
        pieceMap.put(2, false);
        pieceMap.put(3, false);
        pieceMap.put(4, false);
        pieceMap.put(5, false);
        pieceMap.put(6, false);
        pieceMap.put(7, false);
        pieceMap.put(8, false);

        setPieceMap(pieceMap);
    }

    @Override
    public String toString() {
        return "User{" +
                "image=" + image +
                ", nick='" + nick + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", client='" + client + '\'' +
                ", path='" + path + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", app_token='" + app_token + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                ", race='" + race + '\'' +
                ", platform='" + platform + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", user_token='" + user_token + '\'' +
                ", tw='" + tw + '\'' +
                ", fb='" + fb + '\'' +
                ", gl='" + gl + '\'' +
                ", hashtags=" + hashtags +
                ", relationship='" + relationship + '\'' +
                ", versionBuild='" + versionBuild + '\'' +
                ", gcmToken='" + gcmToken + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", profile=" + profile +
                ", energy=" + energy +
                ", level=" + level +
                ", pieceMap=" + pieceMap +
                '}';
    }
}
