/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import DataBase.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 *
 * @author despa
 */
public class LoadJson {

    public static List<User> GetUsers() throws IOException {
        List<User> users = new ArrayList<>();

        JSONObject root = GetRoot("./users/users.json");

        if (root.size() > 0) {
            JSONArray rootArray = root.getJSONArray("users");

            for (int i = 0; i < rootArray.size(); i++) {
                JSONObject doc = rootArray.getJSONObject(i);
                users.add(new User(doc.getString("name")));
            }
            return users;
        } else {
            return new ArrayList<>();
        }
        
    }
    
    //To be modified heavily
    public static List<String> GetUserPreferences(String userName) throws IOException {
        List<String> preferences = new ArrayList<>();

        JSONObject root = GetRoot("./users/" + userName + ".json");

        if (root.size() > 0) {
            JSONArray rootArray = root.getJSONArray(userName);

            for (int i = 0; i < rootArray.size(); i++) {
                JSONObject doc = rootArray.getJSONObject(i);
                preferences.add(doc.getString("preference"));
            }
            return preferences;
        } else {
            return new ArrayList<>();
        }
        
    }

    public static JSONObject GetRoot(String file) throws IOException {
        String jsonText = Utf8File.LoadFileIntoString(file);
        JSONObject root = new JSONObject();
        try {
            root = (JSONObject) JSONSerializer.toJSON(jsonText);
        } catch (JSONException e) {
            throw new IOException();
        }
        return root;
    }

}
