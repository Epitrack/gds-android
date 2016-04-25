package com.epitrack.guardioes.model;

/**
 * @author Miqueias Lopes on 11/01/16.
 */
public class ProfileImage {

    private static ProfileImage instance;

    private String avatar;
    private String path;

    private ProfileImage() {

    }

    public static synchronized ProfileImage getInstance() {

        if (instance == null) {
            instance = new ProfileImage();
            return instance;
        } else {
            return instance;
        }
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
