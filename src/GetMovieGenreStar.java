import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetMovieGenreStar {
    public static JsonObject GetMovieGenreStar(JsonObject movieJson, Connection conn) throws Exception {
        try {
            String movieId = movieJson.get("id").getAsString();
            String query = "SELECT g.name AS genre " +
                    "FROM genres_in_movies AS gm INNER JOIN genres AS g ON gm.genreId=g.id " +
                    "WHERE gm.movieId=? ";
            PreparedStatement genreStatement = conn.prepareStatement(query);
            query = "SELECT s.name AS star, s.id AS starId " +
                    "FROM stars_in_movies AS sm INNER JOIN stars AS s ON sm.starId=s.id " +
                    "WHERE sm.movieId=? ";
            PreparedStatement starStatement = conn.prepareStatement(query);

            genreStatement.setString(1, movieId);
            ResultSet rs = genreStatement.executeQuery();
            JsonArray movie_genre = new JsonArray();
            while (rs.next())
                movie_genre.add(rs.getString("genre"));
            movieJson.add("genre", movie_genre);

            starStatement.setString(1, movieId);
            rs = starStatement.executeQuery();
            JsonArray star = new JsonArray();
            JsonArray starId=new JsonArray();
            while (rs.next()) {
                star.add(rs.getString("star"));
                starId.add(rs.getString("starId"));
            }
            movieJson.add("star", star);
            movieJson.add("starId",starId);
        } catch (Exception e) {
            movieJson.addProperty("error", e.getMessage());
            throw e;
        }
        return movieJson;
    }
}
