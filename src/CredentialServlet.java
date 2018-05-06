import com.google.gson.JsonArray;
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

@WebServlet(name = "CredentialServlet", urlPatterns = "/api/creditcard-confirm")
public class CredentialServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String creditcard = request.getParameter("credit_card");
        String firstname = request.getParameter("first_name");
        String lastname = request.getParameter("last_name");
        String expiration = request.getParameter("expire_date");

        PrintWriter out = response.getWriter();
        JsonObject responseJsonObject = new JsonObject();
        JsonArray wrongPart = new JsonArray();
        try{
            Connection conn = dataSource.getConnection();

            String query = "select * from creditcards where id =?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1,creditcard);
            ResultSet rs = statement.executeQuery();


            if(rs.next()){
                int correctCount = 0;
                if(rs.getString("firstname").equals(firstname)){
                    correctCount += 1;
                }else{
                    wrongPart.add("firstname doesn't exist");
                }
                if(rs.getString("lastname").equals(lastname)){
                    correctCount += 1;
                }else{
                    wrongPart.add("lastname doesn't exist");
                }
                if(rs.getString("expiration").equals(expiration)){
                    correctCount += 1;
                }else{
                    wrongPart.add("expiration is wrong");
                }
                if(correctCount == 3){
                    responseJsonObject.addProperty("success","true");
                }else{
                    responseJsonObject.addProperty("success","false");
                }
            }else{
                responseJsonObject.addProperty("success","false");
            }

        }catch (Exception e){
            out.write(e.toString());
        }

        out.write(responseJsonObject.toString());
        out.close();
    }
}
