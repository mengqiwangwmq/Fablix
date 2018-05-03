import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;

@WebServlet(name="MoviesServlet",urlPatterns = "/api/movies")
public class MoviesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        try {
            Connection conn = dataSource.getConnection();
            Statement statement = conn.createStatement();
            String query = "SELECT m.id AS id, title, year, director, rating " +
                    "FROM movies AS m INNER JOIN ratings AS r ON m.id=r.movieId " +
                    "ORDER BY rating DESC LIMIT 20";
            ResultSet rs = statement.executeQuery(query);

            String header[] = {"id", "title", "year", "director", "rating"};

            JsonArray jsonArray = new JsonArray();

            while (rs.next()) {
                JsonObject jsonObject = new JsonObject();
                for (String i : header) {
                    jsonObject.addProperty(i, rs.getString(i));
                }
                jsonArray.add(jsonObject);
            }

            query = "SELECT g.name AS genre " +
                    "FROM genres_in_movies AS gm INNER JOIN genres AS g ON gm.genreId=g.id " +
                    "WHERE gm.movieId=? ";
            PreparedStatement genreStatement=conn.prepareStatement(query);
            query="SELECT s.name AS star " +
                    "FROM stars_in_movies AS sm INNER JOIN stars AS s ON sm.starId=s.id " +
                    "WHERE sm.movieId=? ";
            PreparedStatement starStatement=conn.prepareStatement(query);
            for (JsonElement i : jsonArray) {
                JsonObject jsonObject = i.getAsJsonObject();
                String movieId=jsonObject.get("id").getAsString();

                genreStatement.setString(1,movieId);
                rs = genreStatement.executeQuery();
                JsonArray genre=new JsonArray();
                while(rs.next())
                    genre.add(rs.getString("genre"));
                jsonObject.add("genre",genre);

                starStatement.setString(1,movieId);
                rs=starStatement.executeQuery();
                JsonArray star=new JsonArray();
                while (rs.next())
                    star.add(rs.getString("star"));
                jsonObject.add("star",star);
            }
            out.write(jsonArray.toString());

            rs.close();
            statement.close();
            conn.close();
        } catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set reponse status to 500 (Internal Server Error)
            response.setStatus(500);
        }
        out.close();
    }


}
