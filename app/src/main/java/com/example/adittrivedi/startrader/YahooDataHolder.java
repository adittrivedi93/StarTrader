package com.example.adittrivedi.startrader;

/**
 * Created by adittrivedi on 28/01/2015.
 */
public class YahooDataHolder {
    private String date;
    private double open, high, low, close, volume, adjustedClose;

    public YahooDataHolder (String date, double open, double high, double low, double close,
                            double volume, double adjustedClose){
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.adjustedClose = adjustedClose;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public double getOpen(){
        return open;
    }

    public void setOpen(double open){
        this.open = open;
    }

    public double getHigh(){
        return high;
    }

    public void setHigh(double high){
        this.high = high;
    }

    public double getLow(){
        return low;
    }

    public void setLow(double low){
        this.low = low;
    }

    public double getClose(){
        return close;
    }

    public void setClose(double close){
        this.close = close;
    }

    public double getVolume(){
        return volume;
    }

    public void setVolume(double volume){
        this.volume = volume;
    }

    public double getAdjustedClose(){
        return adjustedClose;
    }

    public void setAdjustedClose(double adjustedClose){
        this.adjustedClose = adjustedClose;
    }
}
