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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@WebServlet(name = "AddSaleServlet", urlPatterns = "/api/add-sale")
public class AddSaleServlet extends HttpServlet {
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    private static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        HashMap<String, Integer> items = (HashMap<String, Integer>) session.getAttribute("previousItems");
        String id = (String) session.getAttribute("user");
        String date=simpleDateFormat.format(new Date());

        PrintWriter out = response.getWriter();
        JsonObject jsonObject = new JsonObject();
        try {
            Connection conn = dataSource.getConnection();
            String query = "INSERT INTO sales VALUE (NULL,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1,id);
            statement.setString(3,date);
            for(String i:items.keySet()){
                statement.setString(2,i);
                int amount=items.get(i);
                for(int j=0;j<amount;++j){
                    statement.executeQuery();
                }
            }
            session.removeAttribute("previousItems");
            jsonObject.addProperty("success", "true");
            statement.close();
            conn.close();
        } catch (Exception e) {
            jsonObject.addProperty("success", "false");
            jsonObject.addProperty("error", e.getMessage());
        }
        out.write(jsonObject.toString());
    }
}
