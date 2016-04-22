package com.epitrack.guardioes.helper;

/**
 * @author Igor Morais
 */
public enum Extension {

    JPG(".jpg"), PNG(".png"), BITMAP(".bmp"), MP3(".mp3"), MP4(".mp4"), GP(".3gp"), JSON(".json"), TXT(".txt");

    private final String extension;

    Extension(final String extension) {
        this.extension = extension;
    }

    public final String getExtension() {
        return extension;
    }
}
