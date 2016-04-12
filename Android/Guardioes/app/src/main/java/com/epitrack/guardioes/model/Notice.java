package com.epitrack.guardioes.model;

public class Notice {

    private String title;
    private String source;
    private String publicationDate;
    private String link;
    private int drawable;
    private String like;
    private String clock;

    public final String getTitle() {
        return title;
    }

    public final void setTitle(final String title) {
        this.title = title;
    }

    public final String getSource() {
        return source;
    }

    public final void setSource(final String source) {
        this.source = source;
    }

    public final String getPublicationDate() {
        return publicationDate;
    }

    public final void setPublicationDate(final String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getClock() {
        return clock;
    }

    public void setClock(String clock) {
        this.clock = clock;
    }
}
