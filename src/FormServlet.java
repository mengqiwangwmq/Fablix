import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * A servlet that takes input from a html <form> and talks to MySQL moviedb,
 * generates output as a html <table>
 */

// Declaring a WebServlet called FormServlet, which maps to url "/form"
@WebServlet(name = "FormServlet", urlPatterns = "/form")
public class FormServlet extends HttpServlet {

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;


    // Use http GET
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Building page head with title
        out.println("<html><head><title>MovieDB: Found Records</title></head>");

        // Building page body
        out.println("<body><h1>MovieDB: Found Records</h1>");


        try {

            // Create a new connection to database
            Connection dbCon = dataSource.getConnection();

            // Declare a new statement
            Statement statement = dbCon.createStatement();

            // Retrieve parameter "name" from the http request, which refers to the value of <input name="name"> in index.html
            String name = request.getParameter("name");
            String []tokens = name.split(" ");
            // Generate a SQL query

            String myQuery = "";
            for(int i = 0;i<tokens.length;i++){
                tokens[i] = "%"+tokens[i]+"%";
            }
            for(int i = 0; i<tokens.length;i++){
                if(i == tokens.length-1){
                    myQuery += String.format("concat(title,year,director,name) like '%s'",tokens[i]);
                    break;
                }
                myQuery += String.format("concat(title,year,director,name) like '%s' or ",tokens[i]);
            }
            String query = String.format("SELECT distinct title, year, director from movies, stars_in_movies, stars" +
                            " where (%s)" +
                            " and movies.id = stars_in_movies.movieId and stars.id = stars_in_movies.starId"
                    ,myQuery);



            // Perform the query
            ResultSet rs = statement.executeQuery(query);
            JsonArray Map = new JsonArray();
            //ArrayList<Map> Map = new ArrayList<>();
            // Create a html <table>
            out.println("<table border>");

            // Iterate through each row of rs and create a table row <tr>
            out.println("<tr><td>Title</td><td>Year</td><td>Director</td><td>Stars</td><td>Genres</td></tr>");
            while (rs.next()) {
                JsonObject tmpMap = new JsonObject();
                tmpMap.addProperty("title", rs.getString("title"));
                tmpMap.addProperty("year", rs.getString("year"));
                tmpMap.addProperty("director", rs.getString("director"));
                Map.add(tmpMap);
            }
            //out.println(Map.toString());
            for(JsonElement i: Map){
                String title = i.getAsJsonObject().get("title").getAsString();
                query = String.format("select stars.name as s from stars, stars_in_movies, movies " +
                            "where movies.title = '%s' and movies.id = stars_in_movies.movieId and " +
                            "stars_in_movies.starId = stars.id",title);
                ResultSet starResult =  statement.executeQuery(query);
                JsonArray star = new JsonArray();
                while (starResult.next()){
                        star.add(starResult.getString("s"));
                }
                i.getAsJsonObject().add("stars",star);
                query = String.format("select genres.name as g from genres, genres_in_movies, movies " +
                        "where movies.title = '%s' and movies.id = genres_in_movies.movieId and " +
                        "genres_in_movies.genreId = genres.id",title);
                ResultSet genreResult = statement.executeQuery(query);
                JsonArray genre = new JsonArray();
                while (genreResult.next()){
                    genre.add(genreResult.getString("g"));
                }
                i.getAsJsonObject().add("genres",genre);
                //out.println(String.format("<tr><td>%s</td><td>%s</td><td>%S</td><td>%S</td><td>%s</td></tr>",(String)i.get("title"),(String)i.get("year"),
                        //(String)i.get("director"),(String)i.get("stars"),(String)i.get("genres")));
            }
            out.println(Map.toString());
            out.println("</table>");


            // Close all structures
            rs.close();
            statement.close();
            dbCon.close();

        } catch (Exception ex) {

            // Output Error Massage to html
            out.println(String.format("<html><head><title>MovieDB: Error</title></head>\n<body><p>SQL error in doGet: %s</p></body></html>", ex.getMessage()));
            return;
        }
        out.close();
    }
}
