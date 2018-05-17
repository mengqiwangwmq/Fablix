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
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

@WebServlet(name = "MetadataServlet", urlPatterns = "/employee/api/get-metadata")
public class MetadataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        JsonObject jsonObject=new JsonObject();
        PrintWriter out=response.getWriter();

        try {
            Connection conn = dataSource.getConnection();
            DatabaseMetaData metaData = conn.getMetaData();

            Stack<StringBuilder> tables = new Stack<>();
            ResultSet rs = metaData.getTables(null, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                tables.push(new StringBuilder().append(rs.getString("TABLE_NAME")));
            }

            StringBuilder tmp=new StringBuilder();
            for (StringBuilder i : tables) {
                rs = metaData.getColumns(null, null, i.toString(), "%");
                if (rs.next()) {
                    i.append(": ").append(rs.getString("COLUMN_NAME"));
                    i.append(" (").append(rs.getString("TYPE_NAME")).append(")");
                }
                while (rs.next()) {
                    i.append(", ").append(rs.getString("COLUMN_NAME"));
                    i.append(" (").append(rs.getString("TYPE_NAME")).append(")");
                }
                i.append("<br>");
                tmp.append(i);
            }
            jsonObject.addProperty("message",tmp.toString());

            rs.close();
            conn.close();
        } catch (Exception e) {
            jsonObject.addProperty("message",e.getMessage());
        }
        out.write(jsonObject.toString());
    }
}
