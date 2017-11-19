/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windows;

import DataBase.LocalUsers;
import DataBase.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author despa
 */
public class UserController {

    private LocalUsers users;

    public UserController() {
        users = new LocalUsers();
    }

    public List<String> getUserNamesList() {
        List<String> userNames = new ArrayList<>();
        for (String s : users.getUsers()){
            userNames.add(s);
        }
        return userNames;
    }

    public void CreateUser(String s) {
        users.CreateNewUser(s);
    }

    public void RemoveUser(String user) {
        users.RemoveUser(user);
    }

}
