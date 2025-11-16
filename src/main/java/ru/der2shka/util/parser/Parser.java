package ru.der2shka.util.parser;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.der2shka.model.Class;
import ru.der2shka.model.Subject;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.*;

public class Parser {
    private static Parser instance;

    @Getter
    @Setter
    private Integer timeOut = 0;

    @NonNls
    private static final String userAgent =
            "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";
    @NonNls
    private static final String classOfDate = "background1";

    public static @NotNull Parser getInstance() {
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
    public Document getDocument(@NotNull String url)
            throws IOException, NoSuchAlgorithmException, KeyManagementException
    {
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
    public Optional<Elements> getTable(@NotNull Document doc) {
        Elements table = doc.select("tbody");

        return Optional.of(table);
    }

    /**
     * Get Rows element from {@link Elements} of table.
     * @param table {@link Elements} object of table.
     * @return {@link Elements} of rows.
     * **/
    public Optional<Elements> getRows(@NotNull Elements table) {
        Elements rows = table.select("tr");

        return Optional.of(rows);
    }

    /**
     * Get Class objects list.
     * @param rows rows from table.
     * @return {@code List<Class>} list of classes.
     * **/
    public @NotNull List<Class> getClasses(@NotNull Elements rows) {
        List<Class> classes = new ArrayList<>();

        if (rows.isEmpty()) return classes;

        boolean firstDateFounded = false;
        String tempDate = "";

        for (Element row : rows) {
            if (row.hasClass(classOfDate) && !firstDateFounded) {
                tempDate = row.getElementsByTag("td").text();
                firstDateFounded = true;
            }
            else if (row.hasClass(classOfDate) && firstDateFounded) {
                break;
            }

            if (!row.hasClass(classOfDate)) {

                Class newClass;

                Elements tds = row.getElementsByTag("td");

                if (tds.size() == 1) {
                    String subjectName = tds.text();

                    Subject subject = new Subject(
                            -1,
                            "",
                            subjectName,
                            "",
                            ""
                    );

                    newClass = new Class(tempDate, subject);
                }
                else {
                    Integer seqNum = Integer.parseInt(tds.getFirst().text());
                    String timePeriod = tds.get(1).text();

                    Elements spans = tds.select("span");

                    String nameOfSubject = spans.getFirst().text();
                    String teacherFIO = spans.get(1)
                            .getElementsByTag("a")
                            .getFirst()
                            .text();
                    String address = spans.get(2).text();

                    Subject subject = new Subject(
                            seqNum,
                            timePeriod,
                            nameOfSubject,
                            teacherFIO,
                            address
                    );

                    newClass = new Class(tempDate, subject);
                }

                classes.add(newClass);
            }
        }

        return classes;
    }
}
