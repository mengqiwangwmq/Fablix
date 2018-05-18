import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
// import java.util.ArrayList;
public class DOMForActors63 extends DefaultHandler {
    static ArrayList<Star> myStars;
    Document dom;
    public static void main(String[] args) {

        //create an instance
        DOMForActors63 dpe = new DOMForActors63();

        //call run example
        dpe.executeDOMForActors63();

        String loginUser = "root";
        String loginPasswd = "wmq951126";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";


        try {
            Class.forName("com.mysql.jdbc.Driver");
            // create database connection
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

            Statement statement = connection.createStatement();
            String query = "";
            int i = 9423080;
            for (Star s : myStars) {
                AddStar.addStar(connection,s.name,s.birthYear);

            }



        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }
    public DOMForActors63(){
        myStars = new ArrayList<>();
    }
    public void executeDOMForActors63(){
        parseXmlFile();

        //get each employee element and create a Employee object
        parseDocument();

        //Iterate through the list and print the data
        //printData();
    }
    private void parseXmlFile(){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("actors63.xml");
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

        //get a nodelist of <employee> elements
        NodeList nl = docEle.getElementsByTagName("actor");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {

                //get the employee element
                Element el = (Element) nl.item(i);

                //get the Employee object
                Star e = getStar(el);

                //add it to list
                myStars.add(e);
            }
        }
    }

    /**
     * I take an employee element and read the values in, create
     * an Employee object and return it
     *
     * @param empEl
     * @return
     */
    private Star getStar(Element empEl) {

        //for each <employee> element get text or int values of
        //name ,id, age and name
        String name = getTextValue(empEl, "stagename");
        //String id = getIntValue(empEl, "");
        Integer birthYear = getIntValue(empEl, "dob");

        //String type = empEl.getAttribute("type");

        //Create a new Employee with the value read from the xml nodes
        Star e = new Star(name, birthYear);

        return e;
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
            if(tmp1==null){
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
        String tmp=getTextValue(ele, tagName);
        if(tmp==null) return null;
        Integer tmp2;
        //in production application you would catch the exception
        try {
             tmp2 = Integer.parseInt(tmp);
        }
        catch (Exception e){
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
