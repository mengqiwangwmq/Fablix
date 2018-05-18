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

@WebServlet(name = "MoviesSearchServlet", urlPatterns = "/api/movies-search")
public class MoviesSearchServlet extends HttpServlet {
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
        String search=request.getParameter("search");

        PrintWriter out = response.getWriter();

        try {
            Connection conn = dataSource.getConnection();
            Statement statement = conn.createStatement();
            String query = "SELECT COUNT(id) AS num FROM movies";
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            int num=rs.getInt("num");
            int numPg=num/num_per_page;
            query = "SELECT m.id AS id, title, year, director, rating " +
                    "FROM (movies AS m LEFT JOIN ratings AS r ON m.id=r.movieId) " +
                    "WHERE concat(m.title,' ',m.year,' ',m.director, ' ') LIKE ? " +
                    "ORDER BY " + sort_by + " DESC " +
                    "LIMIT ? offset ?";
            PreparedStatement preparedStatement=conn.prepareStatement(query);
            preparedStatement.setString(1,"%"+search+"%");
            preparedStatement.setInt(2,num_per_page);
            preparedStatement.setInt(3,(page-1)*num_per_page);
            rs=preparedStatement.executeQuery();

            String header[] = {"id", "title", "year", "director", "rating"};

            JsonArray jsonArray=ConvertResultSetToJson.ConvertResultSetToJson(header,rs);

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
