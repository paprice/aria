/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import static InputProcessing.SentenceParser.LoadModel;
import java.io.IOException;
import windows.MainWindow;
import windows.UserLogin;

/**
 *
 * @author Patrice Desrochers
 */
public class main {

    public static void main(String[] args) throws IOException {
        // Prints "Hello, World" to the terminal window.
        LoadModel();

        if (args.length > 0 && args[0].equals("test")) {
            MainWindow win = new MainWindow("user");
        } else {
            UserLogin log = new UserLogin();
        }

    }

}
