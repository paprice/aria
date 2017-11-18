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
        for (User u : users.getUsers()){
            userNames.add(u.getName());
        }
        return userNames;
    }

    public void CreateUser(User u) {
        users.CreateNewUser(u);
    }

    public void RemoveUser(String user) {
        users.RemoveUserByName(user);
    }

}
