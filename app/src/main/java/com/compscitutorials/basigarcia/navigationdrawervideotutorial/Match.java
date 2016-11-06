package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

/**
 * Created by Matheus on 05/07/2016.
 */
public class Match {
    //Atributos da lista
    private String title;
    private String imguri;
    private String artist;
    private String preview;
    //Constructor
    public Match(){
    }
    public Match(String title, String imguri, String artist, String preview){
        this.title = title;
        this.imguri = imguri;
        this.artist = artist;
        this.preview = preview;
    }
    //Gets and Sets
    public String getTitle() {
        return title;
    }
    public void setTitle(String name) {
        this.title = name;
    }
    public String getImgUri() {
        return imguri;
    }
    public void setImguri(String imguri){
        this.imguri = imguri;
    }
    public String getArtist() { return artist; }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public String getPreview() { return preview; }
    public void setPreview() { this.preview = preview; }
}
