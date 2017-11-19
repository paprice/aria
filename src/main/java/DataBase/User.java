/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import Files.LoadJson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gildo
 */
public class User {
    
    private final String name;
    private List<UserPreference> preferences;
    private static User user;
    
    private User(String name){
        this.name = name;
        preferences = new ArrayList<>();
    }
    
    public static User Create(String name) throws IOException{
        if (user == null){
            user = new User(name);
            List<UserPreference> userPreferences = LoadJson.GetUserPreferences(name);
            if (userPreferences.size() > 0)
            {
                for (UserPreference p : userPreferences){
                    user.addPreference(p);
                }
            }
        }
        return user;
    }
    
    public static User Instance(){
        if (user == null){
            System.out.println("User instance not created!");
            return null;
        }
        return user;
    }
    
    public String getName(){
        return name;
    }
    
    public void addPreference(UserPreference preference){
        preferences.add(preference);
    }
    
    public String getFavoriteSubject(){
        int highest = 0;
        for (int i = 0; i < preferences.size(); ++i){
            if (preferences.get(i).getValue() > highest){
                highest = i;
            }
        }
        return preferences.get(highest).getWord();
    }
    
    public String getDespisedSubject(){
        int lowest = Integer.MAX_VALUE;
        for (int i = 0; i < preferences.size(); ++i){
            if (preferences.get(i).getValue() < lowest){
                lowest = i;
            }
        }
        return preferences.get(lowest).getWord();
    }
    
    public int getCommonPreference(String word){
        for (UserPreference p : preferences){
            if (p.getWord().equals(word)){
                return p.getValue();
            }
        }
        return 0;
    }
    
    
    
}
