package ru.der2shka.service;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.der2shka.exception.DocumentWasNotParsedException;
import ru.der2shka.exception.TableRowsWasNotParsedException;
import ru.der2shka.exception.TableWasNotParsedException;
import ru.der2shka.model.Class;
import ru.der2shka.util.parser.Parser;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParserService {
    private static final Parser parser = Parser.getInstance();

    public List<Class> getClasses(String url) {
        List<Class> classes = new ArrayList<>();

        try {
            Document doc = this.parseDocument(url)
                    .orElseThrow(() ->
                            new DocumentWasNotParsedException("Document was not found!")
                    );

            Elements table = parser.getTable(doc)
                    .orElseThrow(() ->
                            new TableWasNotParsedException("Table was not parsed from document!")
                    );

            Elements rows = parser.getRows(table)
                    .orElseThrow(() ->
                            new TableRowsWasNotParsedException("Table's rows was not parsed from table!")
                    );

            classes = parser.getClasses(rows);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return classes;
    }

    private Optional<Document> parseDocument(String url) {
        Optional<org.jsoup.nodes.Document> document = Optional.empty();

        try {
            document = Optional.of(parser.getDocument(url));
        }
        catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        catch (KeyManagementException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            System.err.println("IOException when connect to " + url);
            ex.printStackTrace();
        }

        return document;
    }
}
