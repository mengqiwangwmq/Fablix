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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "MoviesAlphabetServlet", urlPatterns = "/api/movies-alphabet")
public class MoviesAlphabetServlet extends HttpServlet {
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

        String pg = request.getParameter("page");
        int page = Integer.parseInt(pg);
        int num_per_page = Integer.parseInt(request.getParameter("num_per_page"));
        String sort_by = request.getParameter("sort_by");
        String alphabet = request.getParameter("alphabet");

        PrintWriter out = response.getWriter();

        try {
            Connection conn = dataSource.getConnection();
            Statement statement = conn.createStatement();

            String query = "SELECT COUNT(*) AS num FROM movies WHERE title like'" + alphabet + "%'";
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            int num=rs.getInt("num");
            int numPg=num/num_per_page;

            query = "SELECT m.id AS id, title, year, director, rating " +
                    "FROM movies AS m INNER JOIN ratings AS r ON m.id=r.movieId "+
                    "WHERE m.title LIKE '" + alphabet + "%' " +
                    "ORDER BY " + sort_by + " DESC " +
                    "LIMIT " + String.valueOf(num_per_page) + " offset " + String.valueOf(page*num_per_page);
            rs = statement.executeQuery(query);

            String header[] = {"id", "title", "year", "director", "rating"};

            JsonArray jsonArray =ConvertResultSetToJson.ConvertResultSetToJson(header,rs);

            for (JsonElement i : jsonArray)
                GetMovieGenreStar.GetMovieGenreStar(i.getAsJsonObject(), conn);

            JsonObject responseObject = new JsonObject();
            responseObject.add("content", jsonArray);
            responseObject.addProperty("page", page);
            responseObject.addProperty("num_page", numPg);
            responseObject.addProperty("num_per_page", num_per_page);
            out.write(responseObject.toString());

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