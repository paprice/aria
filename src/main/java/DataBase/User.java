/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gildo
 */
public class User {
    
    private final String name;
    private List<UserPreference> preferences;
    
    public User(String name){
        this.name = name;
        preferences = new ArrayList<>();
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
    
    
    
}
