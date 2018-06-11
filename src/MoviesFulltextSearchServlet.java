import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "MoviesSearchServlet", urlPatterns = "/api/movies-fulltext-search")
public class MoviesFulltextSearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String pg = request.getParameter("page");
        int page = pg == null ? 1 : Integer.parseInt(pg);
        String npp = request.getParameter("num_per_page");
        int num_per_page = npp == null ? 20 : Integer.parseInt(npp);
        String sort_by = request.getParameter("sort_by");
        if (sort_by == null) sort_by = "Rating";
        String searchStr = request.getParameter("search");
        String[] search = searchStr.split(" ");

        PrintWriter out = response.getWriter();

        try {
            Connection conn = dataSource.getConnection();
            String query = "SELECT count(*) AS num " +
                    "FROM (movies AS m LEFT JOIN ratings AS r ON m.id=r.movieId) " +
                    "WHERE (m.title LIKE ? OR m.title LIKE ?) ";
            int numWords = search.length;
            for (int i = 1; i < numWords; ++i) {
                query += "and (m.title LIKE ? OR m.title LIKE ?) ";
            }

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            for (int i = 0; i < numWords; ++i) {
                preparedStatement.setString(i * 2 + 1, search[i] + "%");
                preparedStatement.setString(i * 2 + 2, "% " + search[i] + "%");
            }
            ResultSet rs = preparedStatement.executeQuery();
            int num;
            rs.next();
            num = rs.getInt("num");
            int numPg = num / num_per_page + (num / num_per_page == 0 ? 0 : 1);

            query = "SELECT m.id AS id, title, year, director, rating " +
                    "FROM (movies AS m LEFT JOIN ratings AS r ON m.id=r.movieId) " +
                    "WHERE (m.title LIKE ? OR m.title LIKE ?) ";
            for (int i = 1; i < numWords; ++i) {
                query += "AND (m.title LIKE ? OR m.title LIKE ?) ";
            }
            ;
            query += "ORDER BY " + sort_by + " DESC " +
                    "LIMIT ? offset ?";
            preparedStatement = conn.prepareStatement(query);
            for (int i = 0; i < numWords; ++i) {
                preparedStatement.setString(i * 2 + 1, search[i] + "%");
                preparedStatement.setString(i * 2 + 2, "% " + search[i] + "%");
            }
            preparedStatement.setInt(numWords * 2 + 1, num_per_page);
            preparedStatement.setInt(numWords * 2 + 2, (page - 1) * num_per_page);

            long startTime = System.nanoTime();
            rs = preparedStatement.executeQuery();
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;

            String path = getServletContext().getRealPath("TS&TJ");
            BufferedWriter bw = new BufferedWriter(new FileWriter(path,true));
            System.out.println("a");
            try {
                bw.write(Long.toString(elapsedTime) + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bw != null)
                        bw.close();
                } catch (IOException ex) {

                    ex.printStackTrace();

                }
            }


            String header[] = {"id", "title", "year", "director", "rating"};

            JsonArray jsonArray = ConvertResultSetToJson.ConvertResultSetToJson(header, rs);

            for (JsonElement i : jsonArray)
                GetMovieGenreStar.GetMovieGenreStar(i.getAsJsonObject(), conn);

            JsonObject responseObject = new JsonObject();
            responseObject.add("content", jsonArray);
            responseObject.addProperty("page", page);
            responseObject.addProperty("num_page", numPg);
            responseObject.addProperty("num_per_page", num_per_page);
            responseObject.addProperty("search", searchStr);
            out.write(responseObject.toString());

            rs.close();
            preparedStatement.close();
            conn.close();
        } catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set reponse status to 500 (Internal Server Error)
            response.setStatus(500);
        }
        out.close();
    }


}
