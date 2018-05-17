import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "AddMovieFeatureServlet", urlPatterns = "/employee/api/add-feature")
public class AddMovieFeatureServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        JsonObject jsonObject = new JsonObject();
        PrintWriter out = response.getWriter();

        String title = request.getParameter("title");
        Integer year = Integer.parseInt(request.getParameter("year"));
        String director = request.getParameter("director");
        String star = request.getParameter("star");
        String genre = request.getParameter("genre");

        try {
            Connection conn = dataSource.getConnection();

            String query = "SELECT * FROM movies WHERE title=? AND year=? AND director=?";
            PreparedStatement existsStat = conn.prepareStatement(query);
            existsStat.setString(1,title);
            existsStat.setInt(2,year);
            existsStat.setString(3,director);
            ResultSet rs = existsStat.executeQuery();
            if (rs.next()) {
                jsonObject=AddMovie.addMovie(conn,title,director,year,star,genre);
            } else {
                jsonObject.addProperty("message","Movie "+title+" ("+year+", "+director+") does not exists.");
                rs.close();
            }
            rs.close();
            existsStat.close();
            conn.close();
        } catch (Exception e) {
            jsonObject.addProperty("message", e.getMessage());
        }
        out.write(jsonObject.toString());
        out.close();
    }
}
