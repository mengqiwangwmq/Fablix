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

//
@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // Retrieve parameter id from url request.
        //String id = request.getParameter("id");

        PrintWriter out = response.getWriter();

        response.setContentType("application/json"); // Response mime type

        try {


            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();
        }catch(Exception e){
            out.write(e.toString());
        }

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */
        if (username.equals("anteater") && password.equals("123456")) {
            // Login success:

            // set this user into the session
            request.getSession().setAttribute("user", new User(username));

            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");

            response.getWriter().write(responseJsonObject.toString());
        } else {
            // Login fail
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            if (!username.equals("anteater")) {
                responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
            } else if (!password.equals("123456")) {
                responseJsonObject.addProperty("message", "incorrect password");
            }
            response.getWriter().write(responseJsonObject.toString());
        }
    }
}
