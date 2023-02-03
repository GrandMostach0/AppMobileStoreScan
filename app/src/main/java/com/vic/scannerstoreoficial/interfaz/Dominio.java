package com.vic.scannerstoreoficial.interfaz;

import android.media.Image;

public class Dominio {
    private String title;
    private String pie;
    private int thumbail;

    public Dominio(String title, String pie, int thumbail){
        this.title = title;
        this.pie = pie;
        this.thumbail = thumbail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPie() {
        return pie;
    }

    public void setPie(String pie) {
        this.pie = pie;
    }

    public int getThumbail(){return thumbail;}

    public void setThumbail(int thumbail){this.thumbail = thumbail;}

}
