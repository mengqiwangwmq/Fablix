import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AddStar {
    public static JsonObject addStar(Connection conn, String name,Integer birthYear) throws Exception{
        JsonObject jsonObject=new JsonObject();
        PreparedStatement exitsStat;
        ResultSet rs;

        if (birthYear == null) {
            String query = "SELECT * FROM stars WHERE name=?";
            exitsStat = conn.prepareStatement(query);
            exitsStat.setString(1, name);

            rs = exitsStat.executeQuery();
            if (rs.next()) {
                jsonObject.addProperty("status", "fail");
                jsonObject.addProperty("message", "Star " + name + " exists.");
            } else {
                jsonObject.addProperty("status", "success");
                jsonObject.addProperty("message","Add star "+name+" succeeded.");
                query = "SELECT SUBSTRING(max(id),3) AS id FROM stars";
                Statement stat = conn.createStatement();
                rs = stat.executeQuery(query);
                rs.next();
                String newId = "nm" + String.valueOf(rs.getInt("id")+1);
                query = "INSERT INTO stars VALUES (?,?,NULL)";
                PreparedStatement addStat = conn.prepareStatement(query);
                addStat.setString(1, newId);
                addStat.setString(2, name);
                addStat.execute();
            }
        } else {
            String query = "SELECT * FROM stars WHERE name=? AND birthYear=?";
            exitsStat = conn.prepareStatement(query);
            exitsStat.setString(1, name);
            exitsStat.setInt(2, birthYear);
            rs = exitsStat.executeQuery();
            if (rs.next()) {
                jsonObject.addProperty("status", "fail");
                jsonObject.addProperty("message", "Star " + name + " with birth year " + birthYear + " exists.");
            } else {
                jsonObject.addProperty("status", "success");
                jsonObject.addProperty("message","Add star "+name+" succeeded.");
                query = "SELECT concat('nm',convert(convert(substring(max(id),3),UNSIGNED)+1,CHAR(8))) AS id FROM stars";
                Statement stat = conn.createStatement();
                rs = stat.executeQuery(query);
                rs.next();
                String newId = rs.getString("id");
                query = "INSERT INTO stars VALUE (?,?,?)";
                PreparedStatement addStat = conn.prepareStatement(query);
                addStat.setString(1, newId);
                addStat.setString(2, name);
                addStat.setInt(3, birthYear);
                addStat.execute();
            }
        }
        rs.close();
        exitsStat.close();
        return jsonObject;
    }
}
