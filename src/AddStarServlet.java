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

@WebServlet(name = "AddStarServlet", urlPatterns = "/employee/api/add-star")
public class AddStarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        JsonObject jsonObject = new JsonObject();

        String name = request.getParameter("name");
        String birthYearS = request.getParameter("birthYear");
        Integer birthYear;
        try {
            birthYear = birthYearS == null ? null : Integer.valueOf(birthYearS);
        } catch (Exception e) {
            birthYear = null;
        }

        try {
            Connection conn = dataSource.getConnection();
            jsonObject = AddStar.addStar(conn, name, birthYear);
            conn.close();
        } catch (Exception e) {
            jsonObject.addProperty("status", "fail");
            jsonObject.addProperty("message", e.getMessage());
        }
        PrintWriter out = response.getWriter();
        out.write(jsonObject.toString());
        out.close();
    }
}
