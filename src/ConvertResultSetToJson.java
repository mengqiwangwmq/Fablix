import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.ResultSet;

public class ConvertResultSetToJson {
    public static JsonArray ConvertResultSetToJson(String[]header, ResultSet rs)throws Exception{
        JsonArray jsonArray=new JsonArray();
        while (rs.next()) {
            JsonObject jsonObject = new JsonObject();
            for (String i : header) {
                jsonObject.addProperty(i, rs.getString(i));
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
