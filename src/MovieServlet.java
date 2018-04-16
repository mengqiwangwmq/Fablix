import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// this annotation maps this Java Servlet Class to a URL
@WebServlet("/movies")
public class MovieServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public MovieServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // change this to your own mysql username and password
        String loginUser = "root";
        String loginPasswd = "2018_Satomi";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        // set response mime type
        response.setContentType("text/html");

        // get the printwriter for writing response
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        out.println("<head><title>Fabflix</title></head>");


        try {
            Class.forName("com.mysql.jdbc.Driver");
            // create database connection
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // declare statement
            Statement statement = connection.createStatement();
            // prepare query
            String query = "SELECT m.id AS Id, Title, Year, Director, Rating " +
                    "FROM movies AS m, ratings AS r " +
                    "WHERE m.id=r.movieId " +
                    "ORDER BY rating DESC " +
                    "LIMIT 10";
            // execute query
            ResultSet resultSet = statement.executeQuery(query);

            out.println("<body>");
            out.println("<h1>Movies</h1>");

            out.println("<table border>");

            // add table header row
            String header[] = {"Title", "Year", "Director", "Rating", "Genre", "Star"};
            out.println("<tr>");
            for (String i : header)
                out.println("<td>" + i + "</td>");
            out.println("</tr>");

            // add a row for every movie result
            ArrayList<Map> movie = new ArrayList<>();
            while (resultSet.next()) {
                Map tmpMap = new HashMap<String, String>();
                tmpMap.put("Id", resultSet.getString("Id"));
                for (int i = 0; i < 4; ++i)
                    tmpMap.put(header[i], resultSet.getString(header[i]));
                movie.add(tmpMap);
            }
            for(Map i:movie){
                String movieId= (String) i.get("Id");
                query = "SELECT g.name AS genre " +
                        "FROM genres_in_movies AS gm, genres AS g " +
                        "WHERE gm.genreId=g.id AND gm.movieId=\"" + movieId + "\"";
                ResultSet genreResult = statement.executeQuery(query);
                StringBuilder genre = new StringBuilder();
                while (genreResult.next())
                    genre.append(genreResult.getString("genre")).append("<br/>");
                i.put("Genre",genre.toString());
                query = "SELECT s.name AS star " +
                        "FROM stars_in_movies AS sm, stars AS s " +
                        "WHERE sm.starId=s.id AND sm.movieId=\"" + movieId + "\"";
                ResultSet starResult = statement.executeQuery(query);
                StringBuilder star = new StringBuilder();
                while (starResult.next())
                    star.append(starResult.getString("star")).append("<br/>");
                i.put("Star",star.toString());

                // get a movies from result set
                out.println("<tr>");
                for(String j :header)
                    out.println("<td>" + (String)i.get(j) + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");

            out.println("</body>");

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            /*
             * After you deploy the WAR file through tomcat manager webpage,
             *   there's no console to see the print messages.
             * Tomcat append all the print messages to the file: tomcat_directory/logs/catalina.out
             *
             * To view the last n lines (for example, 100 lines) of messages you can use:
             *   tail -100 catalina.out
             * This can help you debug your program after deploying it on AWS.
             */
            e.printStackTrace();

            out.println("<body>");
            out.println("<p>");
            out.println("Exception in doGet: " + e.toString());
            out.println("</p>");
            out.print("</body>");
        }

        out.println("</html>");
        out.close();

    }


}