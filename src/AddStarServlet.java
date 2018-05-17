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

            PreparedStatement exitsStat;
            ResultSet rs;

            if (birthYear == null) {
                String query = "SELECT * FROM stars WHERE name=?";
                exitsStat = conn.prepareStatement(query);
                exitsStat.setString(1, name);

                rs = exitsStat.executeQuery();
                if (rs.next()) {
                    jsonObject.addProperty("status", "fail");
                    jsonObject.addProperty("message", "Star " + name + " exists.");
                } else {
                    jsonObject.addProperty("status", "success");
                    jsonObject.addProperty("message","Add star "+name+" succeeded.");
                    query = "SELECT id FROM stars ORDER BY id DESC limit 1";
                    Statement stat = conn.createStatement();
                    rs = stat.executeQuery(query);
                    rs.next();
                    String lastId = rs.getString("id");
                    String newId = "nm" + String.valueOf(Integer.parseInt(lastId.substring(2)) + 1);
                    query = "INSERT INTO stars VALUES (?,?,NULL)";
                    PreparedStatement addStat = conn.prepareStatement(query);
                    addStat.setString(1, newId);
                    addStat.setString(2, name);
                    addStat.execute();
                }
            } else {
                String query = "SELECT * FROM stars WHERE name=? AND birthYear=?";
                exitsStat = conn.prepareStatement(query);
                exitsStat.setString(1, name);
                exitsStat.setInt(2, birthYear);
                rs = exitsStat.executeQuery();
                if (rs.next()) {
                    jsonObject.addProperty("status", "fail");
                    jsonObject.addProperty("message", "Star " + name + " with birth year " + birthYear + " exists.");
                } else {
                    jsonObject.addProperty("status", "success");
                    jsonObject.addProperty("message","Add star "+name+" succeeded.");
                    query = "SELECT id FROM stars ORDER BY id DESC limit 1";
                    Statement stat = conn.createStatement();
                    rs = stat.executeQuery(query);
                    rs.next();
                    String lastId = rs.getString("id");
                    String newId = "nm" + String.valueOf(Integer.parseInt(lastId.substring(2)) + 1);
                    query = "INSERT INTO stars VALUE (?,?,?)";
                    PreparedStatement addStat = conn.prepareStatement(query);
                    addStat.setString(1, newId);
                    addStat.setString(2, name);
                    addStat.setInt(3, birthYear);
                    addStat.execute();
                }
            }
            rs.close();
            exitsStat.close();
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
