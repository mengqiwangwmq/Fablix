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

@WebServlet(name = "GenresServlet", urlPatterns = "/api/genres")
public class GenresServlet extends HttpServlet {
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
            String query = "SELECT COUNT(id) AS num FROM genres";
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            query = "SELECT id, name " +
                    "FROM genres ";
            rs = statement.executeQuery(query);

            String header[] = {"id", "name"};

            JsonArray jsonArray = new JsonArray();

            while (rs.next()) {
                JsonObject jsonObject = new JsonObject();
                for (String i : header) {
                    jsonObject.addProperty(i, rs.getString(i));
                }
                jsonArray.add(jsonObject);
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
