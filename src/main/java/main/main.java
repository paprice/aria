/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import DataBase.MongoDB;
import windows.MainWindow;


/**
 *
 * @author Patrice Desrochers
 */
public class main {

    public static MongoDB db;

    public static void main(String[] args) {
        // Prints "Hello, World" to the terminal window.
        db = new MongoDB();
        db.getCollection("names", db.GetDatabase());
        MainWindow win = new MainWindow();

    }

}
