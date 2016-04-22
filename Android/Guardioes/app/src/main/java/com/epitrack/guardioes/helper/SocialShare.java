package com.epitrack.guardioes.helper;

/**
 * @author Miqueias Lopes on 03/11/15.
 */
public class SocialShare {

    private static SocialShare instance;
    private boolean isShared;

    private SocialShare() {

    }

    public static synchronized SocialShare getInstance() {

        if (instance == null) {
            instance = new SocialShare();
            return instance;
        } else {
            return instance;
        }
    }

    public boolean isShared() {
        return isShared;
    }

    public void setIsShared(boolean isShared) {
        this.isShared = isShared;
    }
}
