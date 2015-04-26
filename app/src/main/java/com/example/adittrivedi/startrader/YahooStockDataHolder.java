package com.example.adittrivedi.startrader;

/**
 * Created by adittrivedi on 12/03/2015.
 */
public class YahooStockDataHolder {

    private String name;
    private String element;

    public YahooStockDataHolder(String name, String element){
        this.name = name;
        this.element = element;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getElement() {
        return element;
    }
}
