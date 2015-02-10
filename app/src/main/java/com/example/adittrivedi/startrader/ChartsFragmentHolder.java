package com.example.adittrivedi.startrader;

/**
 * Created by adittrivedi on 27/01/2015.
 */
public class ChartsFragmentHolder {
    private String stockSymbol, timeHorizon;
    private int sDay, sMonth, sYear, eDay, eMonth, eYear;

    public ChartsFragmentHolder(String stockSymbol, String timeHorizon, int sDay,
                                int sMonth, int sYear, int eDay, int eMonth,
                                int eYear) {
        this.stockSymbol = stockSymbol;
        this.timeHorizon = timeHorizon;
        this.sDay = sDay;
        this.sMonth = sMonth;
        this.sYear = sYear;
        this.eDay = eDay;
        this.eMonth = eMonth;
        this.eYear = eYear;
    }

    public String getStockSymbol(){
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol){
        this.stockSymbol= stockSymbol;
    }

    public String getTimeHorizon(){
        return timeHorizon;
    }

    public void setTimeHorizon(String timeHorizon){
        this.timeHorizon= timeHorizon;
    }

    public int getStartDay(){
        return sDay;
    }

    public void setStartDay(int sDay){
        this.sDay= sDay;
    }

    public int getStartMonth(){
        return sMonth;
    }

    public void setStartMonth(int sMonth){
        this.sMonth= sMonth;
    }

    public int getStartYear(){
        return sYear;
    }

    public void setStartYear(int sYear){
        this.sYear= sYear;
    }

    public int getEndDay(){
        return eDay;
    }

    public void setEndDay(int eDay){
        this.eDay= eDay;
    }

    public int getEndMonth(){
        return eMonth;
    }

    public void setEndMonth(int eMonth){
        this.eMonth= eMonth;
    }

    public int getEndYear(){
        return eYear;
    }

    public void setEndYear(int eYear){
        this.eYear= eYear;
    }
}
