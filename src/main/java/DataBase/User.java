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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gildo
 */
public class User {

    private final String name;
    private List<UserPreference> preferences;
    private static User user;

    private User(String name) {
        this.name = name;
        preferences = new ArrayList<>();
    }

    public static User Create(String name) {
        if (user == null) {
            user = new User(name);
            List<UserPreference> userPreferences;
            try {
                userPreferences = LoadJson.GetUserPreferences(name);
            } catch (IOException ex) {
                WriteJson.WritePreferenceData(name, user.preferences);
                userPreferences = new ArrayList<>();
            }
            if (userPreferences.size() > 0) {
                for (UserPreference p : userPreferences) {
                    user.addPreference(p);
                }
            }
        }
        return user;
    }

    public static User Instance() {
        if (user == null) {
            System.out.println("User instance not created!");
            return null;
        }
        return user;
    }

    public String getName() {
        return name;
    }
    
    public List<UserPreference> getUserPreferences(){
        return preferences;
    }

    public void addPreference(UserPreference preference) {
        int key = 0;
        for (UserPreference p : preferences){
            if (p.getWord().equals(preference.getWord())){
                break;
            }
            ++key;
        }
        if (key == preferences.size()){
            preferences.add(preference);
        } else {
            UserPreference newPref = preferences.remove(key);
            if (preference.getValue() == 1){
                newPref.increment();
            } else {
                newPref.decrement();
            }
            preferences.add(newPref);
        }
        
    }

    public String getFavoriteSubject() {
        int highest = 0;
        for (int i = 0; i < preferences.size(); ++i) {
            if (preferences.get(i).getValue() > highest) {
                highest = i;
            }
        }
        return preferences.get(highest).getWord();
    }

    public String getDespisedSubject() {
        int lowest = Integer.MAX_VALUE;
        for (int i = 0; i < preferences.size(); ++i) {
            if (preferences.get(i).getValue() < lowest) {
                lowest = i;
            }
        }
        return preferences.get(lowest).getWord();
    }

    public int getCommonPreference(String word) {
        for (UserPreference p : preferences) {
            if (p.getWord().equals(word)) {
                return p.getValue();
            }
        }
        return 0;
    }

}
