import com.google.gson.JsonObject;
import org.jasypt.util.password.StrongPasswordEncryptor;

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


@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        PrintWriter out = response.getWriter();
        JsonObject responseJsonObject = new JsonObject();

        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);

        try {
            if (!RecaptchaVerifyUtils.verify(gRecaptchaResponse)) {
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "Recapthca fail");
            } else {
                // Get a connection from dataSource
                Connection conn = dataSource.getConnection();

                String query = "SELECT * FROM customers WHERE email=?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    if (new StrongPasswordEncryptor().checkPassword(password, rs.getString("password"))) {
                        // Login success:

                        // set this user into the session
                        request.getSession().setAttribute("user", new User(rs.getString("id")));

                        responseJsonObject.addProperty("status", "success");
                        responseJsonObject.addProperty("message", "success");
                    } else {
                        //Login fail wrong password
                        responseJsonObject.addProperty("status", "fail");
                        responseJsonObject.addProperty("message", "incorrect password");
                    }
                } else {
                    // Login fail no username
                    responseJsonObject.addProperty("status", "fail");
                    responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
                }
                rs.close();
                statement.close();
                conn.close();
            }
        } catch (Exception e) {
            out.write(e.toString());
        }
        out.write(responseJsonObject.toString());
        out.close();
    }
}
