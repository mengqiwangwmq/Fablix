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

@WebServlet(name = "DashboardLoginServlet", urlPatterns = "/fabflix/login")
public class DashboardLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        String username=request.getParameter("username");

        PrintWriter out=response.getWriter();
        JsonObject jsonObject=new JsonObject();
        try{
            Connection conn=dataSource.getConnection();

            String query="SELECT * FROM employees WHERE email=?";
            PreparedStatement stat=conn.prepareStatement(query);
            stat.setString(1,username);
            ResultSet rs=stat.executeQuery();
            if(rs.next()){
                if(rs.getString("password").equals(request.getParameter("password"))) {
                    jsonObject.addProperty("status", "success");
                    request.getSession().setAttribute("employee",new User(rs.getString("email")));
                }
                else{
                    jsonObject.addProperty("status","fail");
                    jsonObject.addProperty("message","Invalid Password.");
                }
            }else{
                jsonObject.addProperty("status","fail");
                jsonObject.addProperty("message","Username "+username+" does not exist.");
            }

        }catch (Exception e){
            jsonObject.addProperty("status","fail");
            jsonObject.addProperty("message",e.getMessage());
        }
        out.write(jsonObject.toString());
        out.close();
    }


}
