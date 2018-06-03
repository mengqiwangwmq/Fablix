import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashSet;
import java.util.Stack;

//import javax.activation.DataSource;

// import java.util.ArrayList;
public class DOMForMain {
    static Document dom;

    static final String user = "root";
    static final String passwd = "wmq951126";
    static final String url = "jdbc:mysql://localhost:3306/moviedb";
    static Connection conn;

    public static void main(String[] arcs) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("mains243.xml");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        //get the root elememt
        dom.getDocumentElement().normalize();
        Element docEle = dom.getDocumentElement();

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, user, passwd);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        //get a nodelist of <employee> elements
        NodeList directors = docEle.getElementsByTagName("directorfilms");
        for (int i = 0; i < directors.getLength(); i++) {
            String director = null;
            Element el = (Element) directors.item(i);
            Element director_info = (Element) el.getElementsByTagName("director").item(0);
            try {
                director = getSubElementValue(director_info, "dirname");
            } catch (Exception e) {
                continue;
            }
            NodeList films = el.getElementsByTagName("films");
            HashSet<String> genres = new HashSet<>();
            String title = null;
            Integer year = null;
            NodeList film = ((Element) films.item(0)).getElementsByTagName("film");
            for (int j = 0; j < film.getLength(); ++j) {
                Element elj = (Element) film.item(j);
                try {
                    title = getSubElementValue(elj, "t");
                } catch (Exception e) {
                    continue;
                }
                String yearS = getSubElementValue(elj, "year");
                try {
                    year = Integer.parseInt(yearS);
                } catch (Exception e) {
                    year = null;
                }
                NodeList cat = elj.getElementsByTagName("cat");
                for (int k = 0; k < cat.getLength(); ++k) {
                    try {
                        genres.add(cat.item(k).getFirstChild().getNodeValue());
                    } catch (Exception e) {
                        continue;
                    }
                }
                for (String genre : genres) {
                    try {
                        AddMovie.addMovie(conn, title, director, year, null, genre);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        continue;
                    }
                }
            }
        }
        try {
            conn.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private static String getSubElementValue(Element element, String tagName) {
        return element.getElementsByTagName(tagName).item(0).getFirstChild().getNodeValue();
    }
}
