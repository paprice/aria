/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TypeWord;

import org.bson.Document;

/**
 *
 * @author despa
 */
public interface Word {
    
    public Document CreateDoc();

    public String getType();
    public String getWord();
    public int getPreference();
    public String getDet();
    
}
