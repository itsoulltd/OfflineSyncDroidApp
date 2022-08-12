package lab.infoworks.libshared.domain.db.dao;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Converters {
    @TypeConverter
    public static List<Map<String, String>> fromString(String value) {
        Type listType = new TypeToken<ArrayList<HashMap<String, String>>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<Map<String, String>> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
