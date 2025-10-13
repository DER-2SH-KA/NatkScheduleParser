package ru.der2shka.util.parser;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.Optional;

public class Parser {
    private static Parser instance;

    @Getter
    @Setter
    private Integer timeOut = 0;

    private static final String userAgent =
            "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";

    public static Parser getInstance() {
        if (Objects.isNull(instance)) {
            instance = new Parser();
        }

        return instance;
    }

    /**
     * Get {@link Document} object from website.
     * @param url {@link String} url object.
     * @return {@link Document} object.
     * @throws IOException if connection don't return result.
     * @throws NoSuchAlgorithmException if SSL algorithm {@code TLS} wasn't found.
     * @throws KeyManagementException sam hz.
     * **/
    public Document getDocument(String url) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
        }, new SecureRandom());


        return Jsoup.connect(url)
                .ignoreHttpErrors(true)
                .userAgent(userAgent)
                .sslContext( sc)
                .timeout(timeOut)
                .referrer("https://ya.ru")
                .get();
    }

    /**
     * Get Table element from {@link Document}.
     * @param doc {@link Document} object.
     * @return {@link Elements} of table.
     * **/
    public Optional<Elements> getElementOfTable(Document doc) {
        Elements table = doc.select("tbody");

        return Optional.of(table);
    }
}
