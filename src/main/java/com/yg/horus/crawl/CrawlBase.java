package com.yg.horus.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * Created by jeff on 21. 5. 10.
 */
public class CrawlBase {
    private final static int TIME_OUT = 5000;
    public CrawlBase() {
        this.initSslConnection();
    }

    private void initSslConnection() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
            public X509Certificate[] getAcceptedIssuers(){return new X509Certificate[0];}
            public void checkClientTrusted(X509Certificate[] certs, String authType){}
            public void checkServerTrusted(X509Certificate[] certs, String authType){}
        }};

        try {

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            System.out.println("Custom SSL setting is just initialized ..");

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Document getPageDoc(String url) throws IOException {
        return  Jsoup.connect(url).timeout(TIME_OUT).get();
    }

}
