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
    private List<String> Users;

    public LocalUsers() {
        try {
            Users = LoadJson.GetUsers();
        } catch (IOException ex) {
            Users = new ArrayList<>();
            WriteJson.WriteUserData(nbUser, Users);
        }
        nbUser = Users.size();
    }

    public List<String> getUsers() {
        return Users;
    }

    public boolean CreateNewUser(String u) {
        if (!Users.contains(u)) {
            nbUser++;
            Users.add(u);
            WriteJson.WriteUserData(nbUser, Users);
            return true;
        }
        return false;
    }

    public boolean RemoveUser(String userName) {
        boolean ret = false;
        for (String s : Users){
            if (s.equals(userName)){
                Users.remove(s);
                ret = true;
                break;
            }
        }
        if (ret) {
            nbUser--;
            WriteJson.WriteUserData(nbUser, Users);
        }
        return ret;
    }

}
