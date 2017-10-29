/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConversationHandler;

/**
 *
 * @author Gildo
 */
public enum Preference {
    HATE("Beurk! Je déteste "),
    DISLIKE("Ouf... Je n'aime pas "),
    NEUTRAL("Hmmm. Je ne sais pas quoi penser sur "),
    LIKE("Oui! Moi j'aime "),
    LOVE("Ooooh! J'ADORE ");
    
    public String pref;
    
    Preference (String pref)
    {
        this.pref = pref;
    }
}