/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

/**
 *
 * @author gildo
 */
public class UserPreference {
    private final String word;
    private int value;
    
    public UserPreference(String word, int value){
        this.word = word;
        this.value = value;
    }
    
    public void increment(){
        ++value;
    }
    
    public void decrement(){
        --value;
    }
    
    public String getWord(){
        return word;
    }
    
    public int getValue(){
        return value;
    }
}
