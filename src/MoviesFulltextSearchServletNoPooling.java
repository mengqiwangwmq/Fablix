import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "MoviesFulltextSearchServletNoPooling", urlPatterns = "/api/movies-fulltext-search-no-pooling")
public class MoviesFulltextSearchServletNoPooling extends HttpServlet {
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
            long startTime = System.nanoTime();

            // change this to your own mysql username and password
            String loginUser = "root";
            String loginPasswd = "2018_Satomi";
            String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
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
            long start1 = System.nanoTime();
            ResultSet rs = preparedStatement.executeQuery();
            long elapse1 = System.nanoTime() - start1;
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

            query += "ORDER BY " + sort_by + " DESC " +
                    "LIMIT ? offset ?";
            preparedStatement = conn.prepareStatement(query);
            for (int i = 0; i < numWords; ++i) {
                preparedStatement.setString(i * 2 + 1, search[i] + "%");
                preparedStatement.setString(i * 2 + 2, "% " + search[i] + "%");
            }
            preparedStatement.setInt(numWords * 2 + 1, num_per_page);
            preparedStatement.setInt(numWords * 2 + 2, (page - 1) * num_per_page);

            long start2 = System.nanoTime();
            rs = preparedStatement.executeQuery();
            long elapse2 = elapse1+System.nanoTime() - start2;
            long[] tmpE2=new long[1];
            tmpE2[0]=elapse2;


            String header[] = {"id", "title", "year", "director", "rating"};

            JsonArray jsonArray = ConvertResultSetToJson.ConvertResultSetToJson(header, rs);

            for (JsonElement i : jsonArray)
                GetMovieGenreStar.GetMovieGenreStarWithTime(i.getAsJsonObject(), conn,tmpE2);

            elapse2=tmpE2[0];
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            String TSPath = getServletContext().getRealPath("TS_NP");
            BufferedWriter TSBW = new BufferedWriter(new FileWriter(TSPath, true));
            String TJPath = getServletContext().getRealPath("TJ_NP");
            BufferedWriter TJBW = new BufferedWriter(new FileWriter(TJPath, true));
            System.out.println("a");
            try {
                TSBW.write(Long.toString(elapsedTime) + "\n");
                TJBW.write(Long.toString(elapse2)+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (TSBW != null)
                        TSBW.close();
                    if (TJBW!=null)
                        TJBW.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

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
