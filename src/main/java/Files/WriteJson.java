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
import java.io.IOException;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WriteJson {

    public static void WriteData(int nb, List<String> user) {
        JSONObject outputFile = new JSONObject();
        JSONArray users = new JSONArray();

        outputFile.accumulate("number", nb);

        for (String s : user) {
            JSONObject o = new JSONObject();
            o.accumulate("name", s);
            o.accumulate("path", "./users/" + s + ".json");
            users.add(o);
        }

        outputFile.accumulate("users", users);

        try {
            Utf8File.SaveStringIntoFile("./users/users.json", outputFile.toString(2));
        } catch (IOException ex) {

        }

    }

}
