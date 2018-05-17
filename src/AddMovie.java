import com.google.gson.JsonObject;

import java.sql.*;

public class AddMovie {
    public static JsonObject addMovie(Connection conn, String title, String director, Integer year, String star, String genre) throws SQLException {
        JsonObject jsonObject = new JsonObject();

        String query = "CALL add_movie(?,?,?,?,?)";
        CallableStatement addStat = conn.prepareCall(query);
        addStat.setString(1, title);
        addStat.setString(2, director);
        addStat.setInt(3, year);
        addStat.setString(4, star);
        addStat.setString(5, genre);
        addStat.execute();

        jsonObject.addProperty("message", "Add succeeded");
        addStat.close();
        return jsonObject;
    }
}
