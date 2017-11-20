/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

/**
 *
 * @author despa
 */
import DataBase.User;
import DataBase.UserPreference;
import java.io.IOException;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WriteJson {

    public static void WriteUserData(int nb, List<String> users) {
        JSONObject outputFile = new JSONObject();
        JSONArray jUsers = new JSONArray();

        outputFile.accumulate("number", nb);

        for (String u : users) {
            JSONObject o = new JSONObject();
            o.accumulate("name", u);
            jUsers.add(o);
        }

        outputFile.accumulate("users", jUsers);

        try {
            Utf8File.SaveStringIntoFile("./users/users.json", outputFile.toString(2));
        } catch (IOException ex) {

        }

    }

    public static void WritePreferenceData(int nb, String userName, List<UserPreference> preferences) {
        JSONObject outputFile = new JSONObject();
        JSONArray prefArray = new JSONArray();

        outputFile.accumulate("number", nb);

        for (UserPreference p : preferences) {
            JSONObject o = new JSONObject();
            o.accumulate("word", p.getWord());
            o.accumulate("value", p.getValue());
            prefArray.add(o);
        }

        outputFile.accumulate(userName, prefArray);

        try {
            Utf8File.SaveStringIntoFile("./users/" + userName + ".json", outputFile.toString(2));
        } catch (IOException ex) {

        }
    }

}
