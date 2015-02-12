package com.example.adittrivedi.startrader;

/**
 * Created by adittrivedi on 11/02/2015.
 */
public class YahooFeedsData {
    private String title, link, date;

    public YahooFeedsData (String tile, String link, String date){
        this.title = tile;
        this.link = link;
        this.date = date;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getLink(){
        return link;
    }

    public void setLink(String link){
        this.link = link;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

}
