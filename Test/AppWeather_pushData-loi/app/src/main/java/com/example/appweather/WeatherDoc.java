package com.example.appweather;

import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class WeatherDoc {
    public WeatherDoc(String WOEID, String IM) {
        WeatherDisplay disp = new WeatherDisplay();

        try {
            Document doc = generateXML(WOEID, IM);
            disp.getConditions(doc);
        } catch (IOException ex) {
            Logger.getLogger(WeatherDoc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static Document generateXML(String code, String IM) throws IOException {
        String url = null;
        String XmlData = null;

        url = "http://xml.weather.yahoo.com/forecastrss?w=" + code + "&u=" + IM;
        URL xmlUrl = new URL(url);
        InputStream in = xmlUrl.openStream();

        Document doc = parse(in);

        return doc;
    }

    public static Document parse(InputStream is) {
        Document doc = null;

        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setValidating(false);
            domFactory.setNamespaceAware(false);
            DocumentBuilder builder = domFactory.newDocumentBuilder();

            doc = builder.parse(is);
        } catch (Exception ex) {
            System.err.println("unable to load XML: " + ex);
        }
        return doc;
    }
}
