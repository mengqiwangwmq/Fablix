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

@WebServlet(name = "GetTransactionsServlet", urlPatterns = "/api/get-transaction")
public class GetTransactionsSerlvet extends HttpServlet {
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        JsonArray jsonArray = new JsonArray();
        String id = request.getParameter("id").substring(1).split("]")[0];
        String ids[] = id.split(",");

        PrintWriter out = response.getWriter();
        try {
            Connection conn = dataSource.getConnection();
            String query = "SELECT * FROM sales AS s INNER JOIN movies AS m ON s.movieId=m.id WHERE s.id=?";
            PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            HashMap<String,ArrayList<String>> map=new HashMap<>();
            for (String i : ids) {
                statement.setString(1,i);
                ResultSet rs=statement.executeQuery();
                rs.next();
                String movieId=rs.getString("m.id");
                String saleId=rs.getString("s.id");
                if(map.containsKey(movieId)){
                    map.get(movieId).add(saleId);
                }else{
                    ArrayList<String> saleIds=new ArrayList<>();
                    saleIds.add(saleId);
                    map.put(movieId,saleIds);
                }
            }
            for(String i : map.keySet()){
                JsonObject movieObject=new JsonObject();
                movieObject.addProperty("movieId",i);
                JsonArray saleArray=new JsonArray();
                for(String j:map.get(i)){
                    saleArray.add(j);
                }
                movieObject.add("saleId",saleArray);
                jsonArray.add(movieObject);
            }
            statement.close();
            conn.close();
        } catch (Exception e) {
            jsonArray.add(e.getMessage());
        }
        out.write(jsonArray.toString());
        out.close();
    }
}
