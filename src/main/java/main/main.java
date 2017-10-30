/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import DataBase.MongoDB;
import static InputProcessing.SentenceParser.LoadModel;
import java.io.IOException;
import windows.MainWindow;

/**
 *
 * @author Patrice Desrochers
 */
public class main {

    public static void main(String[] args) throws IOException {
        // Prints "Hello, World" to the terminal window.
        MongoDB db = new MongoDB();
        LoadModel();
        MainWindow win = new MainWindow(db);

    }

}
