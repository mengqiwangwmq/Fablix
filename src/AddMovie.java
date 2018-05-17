import com.google.gson.JsonObject;

import java.sql.*;

public class AddMovie {
    public static JsonObject addMovie(Connection conn, String title, String director, Integer year, String star, String genre) throws SQLException {
        JsonObject jsonObject = new JsonObject();

        Statement stat = conn.createStatement();

        String query = "SELECT id FROM stars ORDER BY id DESC LIMIT 1";
        ResultSet rs = stat.executeQuery(query);
        rs.next();
        String lastId = rs.getString("id");
        String newStarId = "nm" + String.valueOf(Integer.parseInt(lastId.substring(2)) + 1);

        query = "SELECT id FROM movies ORDER BY id DESC LIMIT 1";
        rs = stat.executeQuery(query);
        rs.next();
        lastId = rs.getString("id");
        String newMovieId = "tt" + String.valueOf(Integer.parseInt(lastId.substring(2)) + 1);

        query = "CALL add_movie(?,?,?,?,?,?,?)";
        CallableStatement addStat = conn.prepareCall(query);
        addStat.setString(1, newMovieId);
        addStat.setString(2, title);
        addStat.setString(3, director);
        addStat.setInt(4, year);
        addStat.setString(5, newStarId);
        addStat.setString(6, star);
        addStat.setString(7, genre);
        addStat.execute();

        jsonObject.addProperty("message", "Add succeeded");
        rs.close();
        addStat.close();
        stat.close();
        return jsonObject;
    }
}
