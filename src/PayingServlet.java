import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@WebServlet(name = "PayingServlet", urlPatterns = "/api/pay")
public class PayingServlet extends HttpServlet {
    @Resource(name = "jdbc/writedb")
    private DataSource dataSource;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        HashMap<String, Integer> items = (HashMap<String, Integer>) session.getAttribute("previousItems");
        JsonArray newTransactions = new JsonArray();
        String id = ((User) session.getAttribute("user")).getUsername();
        String date = simpleDateFormat.format(new Date());

        PrintWriter out = response.getWriter();
        JsonObject jsonObject = new JsonObject();
        try {
            Connection conn = dataSource.getConnection();
            String query = "INSERT INTO sales VALUE (NULL,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, id);
            statement.setString(3, date);
            for (String i : items.keySet()) {
                statement.setString(2, i);
                int amount = items.get(i);
                for (int j = 0; j < amount; ++j) {
                    statement.execute();
                    ResultSet rs = statement.getGeneratedKeys();
                    rs.next();
                    newTransactions.add(rs.getInt(1));
                    rs.close();
                }
            }
            session.removeAttribute("previousItems");
            jsonObject.addProperty("success", "true");
            jsonObject.add("id", newTransactions);
            statement.close();
            conn.close();
        } catch (Exception e) {
            jsonObject.addProperty("success", "false");
            jsonObject.addProperty("error", e.getMessage());
        }
        out.write(jsonObject.toString());
        out.close();
    }
}
