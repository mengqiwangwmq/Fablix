import com.google.gson.JsonObject;

import java.sql.*;

public class AddMovie {
    static final String query = "CALL add_movie(?,?,?,?,?)";

    public static JsonObject addMovie(Connection conn, String title, String director, Integer year, String star, String genre) throws SQLException {
        JsonObject jsonObject = new JsonObject();

        CallableStatement addStat = conn.prepareCall(query);
        addStat.setString(1, title);
        addStat.setString(2, director);
        if (year == null) addStat.setNull(3, Types.INTEGER);
        else addStat.setInt(3, year);
        if (star == null) addStat.setNull(4, Types.VARCHAR);
        else addStat.setString(4, star);
        if (genre == null) addStat.setNull(5, Types.VARCHAR);
        else addStat.setString(5, genre);
        addStat.execute();

        jsonObject.addProperty("message", "Add succeeded");
        addStat.close();
        return jsonObject;
    }
}
