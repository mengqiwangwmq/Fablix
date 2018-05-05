import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

@WebServlet(name = "SingleMoviesServlet", urlPatterns = "/api/single-movie")
public class SingleMovieServlet extends HttpServlet {
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

        String id = request.getParameter("id");

        PrintWriter out = response.getWriter();

        try {
            Connection conn = dataSource.getConnection();
            Statement statement = conn.createStatement();
            String query = "SELECT m.id AS id, title, year, director, rating " +
                    "FROM (movies AS m INNER JOIN ratings AS r ON m.id=r.movieId) " +
                    "WHERE m.id='" + id + "'";
            ResultSet rs = statement.executeQuery(query);

            String header[] = {"id", "title", "year", "director", "rating"};

            JsonObject jsonObject = ConvertResultSetToJson.ConvertResultSetToJson(header, rs).get(0).getAsJsonObject();


            GetMovieGenreStar.GetMovieGenreStar(jsonObject, conn);

            out.write(jsonObject.toString());

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
