import com.google.gson.JsonArray;
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

@WebServlet(name="SingleMovieServlet",urlPatterns = "/api/single-movie")
public class SingleMovieServlet extends HttpServlet {
    private static final long serialVersionUID=2L;

    @Resource(name = "jbdc/moviedb")
    private DataSource dataSource;

    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String id=request.getParameter("id");

        PrintWriter out=response.getWriter();

        try{
            Connection conn=dataSource.getConnection();



        }catch(Exception e){
            out.write(e.getMessage());
        }
    }
}
