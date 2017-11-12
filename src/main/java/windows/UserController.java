/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windows;

import DataBase.LocalUser;
import java.util.List;

/**
 *
 * @author despa
 */
public class UserController {

    private LocalUser users;

    public UserController() {
        users = new LocalUser();
    }

    public List<String> ListUser() {

        return users.getUserName();
    }

    public void CreateUser(String user) {
        users.CreateNewUser(user);
    }

    public void RemoveUser(String user) {
        users.RemoveUser(user);
    }

}
