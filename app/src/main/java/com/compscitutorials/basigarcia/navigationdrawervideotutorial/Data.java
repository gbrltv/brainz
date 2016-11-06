package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

/**
 * Created by Matheus on 04/06/2016.
 */
public class Data {
    private String description;
    private String artist;
    private String imagePath;
    private String previewPath;
    private String id;

    public Data(String imagePath, String description, String artist, String previewPath, String id) {
        this.imagePath = imagePath;
        this.description = description;
        this.artist = artist;
        this.previewPath = previewPath;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getPreviewPath() {
        return previewPath;
    }

    public String getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }
}
