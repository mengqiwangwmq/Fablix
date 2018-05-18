import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class DOMForCasts124 extends DefaultHandler {
    static ArrayList<Star> myStars;
    static ArrayList<Movie> myMovies = new ArrayList<>();
    String title;
    String director;
    Integer year;
    String star;
    String genre;
    Node tmp;

    Document dom;

    public static void main(String[] args) {

        //create an instance
        DOMForCasts124 dpe = new DOMForCasts124();

        //call run example
        dpe.executeDOMForCasts124();

        String loginUser = "root";
        String loginPasswd = "wmq951126";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        Connection connection;
        Statement statement;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // create database connection
            connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            for (Movie m : myMovies) {
                try {

                    AddMovie.addMovie(connection, m.title, m.director, m.year, m.stars_in_movies.get(0).name, m.genres_in_movies.get(0).name);
                } catch (Exception e) {
                    System.out.println(e.toString());
                    continue;
                }
            }
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public DOMForCasts124() {
        myStars = new ArrayList<>();
    }

    public void executeDOMForCasts124() {
        parseXmlFile();

        //get each employee element and create a Employee object
        parseDocument();

        //Iterate through the list and print the data
        //printData();
    }

    private void parseXmlFile() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("casts124.xml");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void parseDocument() {
        //get the root elememt
        Element docEle = dom.getDocumentElement();
        NodeList nlof_dirfilms = docEle.getElementsByTagName("dirfilms");
        if (nlof_dirfilms != null && nlof_dirfilms.getLength() > 0) {
            for (int count = 0; count < nlof_dirfilms.getLength(); count++) {
                Element dirfilms = (Element) nlof_dirfilms.item(count);
                director = getTextValue(dirfilms, "is");
                NodeList nlof_filmc = dirfilms.getElementsByTagName("filmc");
                if (nlof_filmc != null && nlof_filmc.getLength() > 0) {
                    for (int i = 0; i < nlof_filmc.getLength(); i++) {

                        //get the employee element
                        Element filmc = (Element) nlof_filmc.item(i);
                        Movie movieofCats124 = new Movie();
                        movieofCats124.director = director;
                        NodeList nlof_m = filmc.getElementsByTagName("m");
                        if (nlof_m != null && nlof_m.getLength() > 0) {
                            for (int j = 0; j < nlof_m.getLength(); j++) {
                                Element m = (Element) nlof_m.item(j);
                                movieofCats124.id = getTextValue(m, "f");
                                movieofCats124.title = getTextValue(m, "t");
                                Star starofCasts124 = new Star();
                                starofCasts124.name = getTextValue(m, "a");
                                movieofCats124.stars_in_movies.add(starofCasts124);
                                Genre genreofCasts124 = new Genre();
                                genreofCasts124.name = getTextValue(m, "p");
                                movieofCats124.genres_in_movies.add(genreofCasts124);
                            }
                        }
                        myMovies.add(movieofCats124);

                        //get the Employee object


                        //add it to list

                    }
                }
            }
        }

        //get a nodelist of <employee> elements

    }


    /**
     * I take a xml element and the tag name, look for the tag and get
     * the text content
     * i.e for <employee><name>John</name></employee> xml snippet if
     * the Element points to employee node and tagName is name I will return John
     *
     * @param ele
     * @param tagName
     * @return
     */
    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            Node tmp1 = el.getFirstChild();
            if (tmp1 == null) {
                return null;
            }
            textVal = tmp1.getNodeValue();
        }

        return textVal;
    }

    /**
     * Calls getTextValue and returns a int value
     *
     * @param ele
     * @param tagName
     * @return
     */
    private Integer getIntValue(Element ele, String tagName) {
        String tmp = getTextValue(ele, tagName);
        if (tmp == null) return null;
        Integer tmp2;
        //in production application you would catch the exception
        try {
            tmp2 = Integer.parseInt(tmp);
        } catch (Exception e) {
            return null;
        }
        return Integer.parseInt(tmp);
    }

    /**
     * Iterate through the list and print the
     * content to console
     */
    private void printData() {

        System.out.println("No of Employees '" + myStars.size() + "'.");

        Iterator<Star> it = myStars.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }


}