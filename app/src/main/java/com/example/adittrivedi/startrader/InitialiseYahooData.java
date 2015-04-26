package com.example.adittrivedi.startrader;

import org.jsoup.Connection;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by adittrivedi on 12/03/2015.
 */
public class InitialiseYahooData {

    public ArrayList<String> initPricing() {
        ArrayList<String> pricingData = new ArrayList<String>();
        pricingData.add("Ask");
        pricingData.add("Bid");
        pricingData.add("Previous Close");
        pricingData.add("Open");
        return pricingData;
    }

    public ArrayList<String> initDividends() {
        ArrayList<String> dividendsData = new ArrayList<String>();
        dividendsData.add("Dividend Yield");
        dividendsData.add("Dividend per Share");
        dividendsData.add("Dividend Pay Date");
        dividendsData.add("Ex-Dividend Date");
        return dividendsData;
    }

    public ArrayList<String> initDates() {
        ArrayList<String> dateData = new ArrayList<String>();
        dateData.add("Change");
        dateData.add("Change & Percent Change");
        dateData.add("Change in Percent");
        dateData.add("Last Trade Date");
        dateData.add("Last Trade Time (US)");
        return dateData;
    }

    public ArrayList<String> initAverages() {
        ArrayList<String> averageData = new ArrayList<String>();
        averageData.add("Day's Low");
        averageData.add("Day's High");
        averageData.add("Last Trade (With Time)");
        averageData.add("Last Trade (Price Only");
        averageData.add("1 Year Target Price");
        averageData.add("Change From 200 Day Moving Average");
        averageData.add("Percent Change From 200 Day Moving Average");
        averageData.add("Change From 50 Day Moving Average");
        averageData.add("Percent Change From 50 Day Moving Average");
        averageData.add("50 Day Moving Average");
        averageData.add("200 Day Moving Average");
        return averageData;
    }

    public ArrayList<String> initFiftyTwoWeekPricing() {
        ArrayList<String> fiftyTwoWeekData = new ArrayList<String>();
        fiftyTwoWeekData.add("52 Week High");
        fiftyTwoWeekData.add("52 Week Low");
        fiftyTwoWeekData.add("Change from 52 Week Low");
        fiftyTwoWeekData.add("Change from 52 Week High");
        fiftyTwoWeekData.add("Percent Change From 52 week Low");
        fiftyTwoWeekData.add("Percent Change From 52 week High");
        fiftyTwoWeekData.add("52 Week Range");
        return fiftyTwoWeekData;
    }

    public ArrayList<String> initSymbolInfo() {
        ArrayList<String> symbolData = new ArrayList<String>();
        symbolData.add("Market Capitalisation");
        symbolData.add("Float Shares");
        symbolData.add("Name");
        symbolData.add("Stock Exchange");
        symbolData.add("Shares Outstanding");
        return symbolData;
    }

    public ArrayList<String> initVolume() {
        ArrayList<String> volumeData = new ArrayList<String>();
        volumeData.add("Volume");
        volumeData.add("Ask Size");
        volumeData.add("Bid Size");
        volumeData.add("Last Trade Size");
        volumeData.add("Average Daily Volume");
        return volumeData;
    }

    public ArrayList<String> initRatios(){
        ArrayList<String> ratioData = new ArrayList<String>();
        ratioData.add("Earnings per Share");
        ratioData.add("EPS Estimate Current Year");
        ratioData.add("EPS Estimate Next Year");
        ratioData.add("EPS Estimate Next Quarter");
        ratioData.add("Book Value");
        ratioData.add("EBITDA");
        ratioData.add("Price / Sales");
        ratioData.add("Price / Book");
        ratioData.add("P/E Ratio");
        ratioData.add("PEG Ratio");
        ratioData.add("Price / EPS Estimate Current Year");
        ratioData.add("Price / EPS Estimate Next Year");
        ratioData.add("Short Ratio");
        return ratioData;
    }
}
