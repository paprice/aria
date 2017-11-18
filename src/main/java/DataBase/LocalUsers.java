/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import Files.LoadJson;
import Files.WriteJson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author despa
 */
public class LocalUsers {

    private int nbUser;
    private List<String> UserNames;

    public LocalUsers() {
        try {
            UserNames = LoadJson.GetUsers();
        } catch (IOException ex) {
            UserNames = new ArrayList<>();
            WriteJson.WriteUserData(nbUser, UserNames);
        }

        nbUser = UserNames.size();

    }

    public List<String> getUserName() {
        return UserNames;
    }

    public boolean CreateNewUser(String user) {
        if (!UserNames.contains(user)) {
            nbUser++;
            UserNames.add(user);
            WriteJson.WriteUserData(nbUser, UserNames);
            return true;
        }
        return false;
    }

    public boolean RemoveUser(String user) {
        boolean ret = UserNames.remove(user);
        if (ret) {
            nbUser--;
            WriteJson.WriteUserData(nbUser, UserNames);
        }
        return ret;
    }

}
