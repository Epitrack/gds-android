package com.epitrack.guardioes.model;

import android.graphics.Bitmap;

import org.json.JSONArray;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.menu.profile.Avatar;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.io.File;

import butterknife.Bind;

public class User {

    // TODO: This is for stub only.
    private int mData;

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
    private String imageResource;
    private Bitmap bitmapImage;
    private String avatar;
    private JSONArray hashtags;
    private int widthImageProfile;
    private int heightImageProfile;
    private Uri uri;
    private String relationship;
    private String versionBuild;
    private String gcmToken;
    private String file;

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

    public User(int image, String nick, String email, String id, String dob, String race, String gender, String picture, String relationship) {
        this.image = image;
        this.nick = nick;
        this.email = email;
        this.id = id;
        this.dob = dob;
        this.race = race;
        this.gender = gender;
        this.picture = picture;
        this.relationship = relationship;
    }

    public User(int image, String nick, String email, String id, String dob, String race, String gender, String picture, String relationship, String file) {
        this.image = image;
        this.nick = nick;
        this.email = email;
        this.id = id;
        this.dob = dob;
        this.race = race;
        this.gender = gender;
        this.picture = picture;
        this.relationship = relationship;
        this.file = file;
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

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
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

    public JSONArray getHashtags() {
        return hashtags;
    }

    public void setHashtags(JSONArray hashtags) {
        this.hashtags = hashtags;
    }

    public int getWidthImageProfile() {
        return widthImageProfile;
    }

    public void setWidthImageProfile(int widthImageProfile) {
        this.widthImageProfile = widthImageProfile;
    }

    public int getHeightImageProfile() {
        return heightImageProfile;
    }

    public void setHeightImageProfile(int heightImageProfile) {
        this.heightImageProfile = heightImageProfile;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
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

    public void clean() {
        this.image = 0;
        this.nick = "";
        this.email = "";
        this.password = "";
        this.client = "api";
        this.dob = "";
        this.gender = "";
        this.app_token = "d41d8cd98f00b204e9800998ecf8427e";
        this.lon = 0;
        this.lat = 0;
        this.race = "";
        this.platform = "android";
        this.type = "";
        this.zip = "";
        this.id = "";
        this.picture = "";
        this.user_token = "";
        this.tw = "";
        this.fb = "";
        this.gl = "";
        this.imageResource = "";
        this.bitmapImage = null;
        this.avatar = "";
        this.hashtags = null;
        this.widthImageProfile = 0;
        this.heightImageProfile = 0;
        this.uri = null;
        this.relationship = "";
        this.versionBuild = "";
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }


    public CircularImageView getImageProfile(CircularImageView imageView, User user) {

        if (user == null) {
            user = SingleUser.getInstance();
        }

        if (user.getFile() == null) {
            user.setFile("");
        }

        if (user.getPicture().equals("0") && !user.getFile().equals("")) {

            File file = new File(user.getFile());

            if (!file.exists()) {
                Uri uri = Uri.parse(user.getFile());
                imageView.setImageURI(uri);
                Drawable drawable = imageView.getDrawable();
                imageView.setImageDrawable(drawable);
            }
        } else if (user.getPicture().equals("0")){
            if (user.getGender().equals("F")) {
                switch (user.getRace()) {
                    case "branco":
                        imageView.setImageResource(Avatar.getBy(Integer.parseInt("8")).getLarge());
                        break;
                    case "preto":
                        imageView.setImageResource(Avatar.getBy(Integer.parseInt("1")).getLarge());
                        break;
                    case "pardo":
                        imageView.setImageResource(Avatar.getBy(Integer.parseInt("2")).getLarge());
                        break;
                    case "amarelo":
                        imageView.setImageResource(Avatar.getBy(Integer.parseInt("7")).getLarge());
                        break;
                    case "indigena":
                        imageView.setImageResource(Avatar.getBy(Integer.parseInt("8")).getLarge());
                        break;
                }
            } else {
                switch (user.getGender()) {
                    case "branco":
                        imageView.setImageResource(Avatar.getBy(Integer.parseInt("11")).getLarge());
                        break;
                    case "preto":
                        imageView.setImageResource(Avatar.getBy(Integer.parseInt("5")).getLarge());
                        break;
                    case "pardo":
                        imageView.setImageResource(Avatar.getBy(Integer.parseInt("4")).getLarge());
                        break;
                    case "amarelo":
                        imageView.setImageResource(Avatar.getBy(Integer.parseInt("10")).getLarge());
                        break;
                    case "indigena":
                        imageView.setImageResource(Avatar.getBy(Integer.parseInt("4")).getLarge());
                        break;
                }
            }
        } else {
            imageView.setImageResource(Avatar.getBy(Integer.parseInt(user.getPicture())).getLarge());
        }


        return imageView;
    }

}
