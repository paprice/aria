/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windows;

import static Language.LanguageProcessing.Parse;
import static Language.Words.ExtractImportant;
import java.io.IOException;
import opennlp.tools.postag.POSSample;

/**
 *
 * @author despa
 */
public class WindowsController {
 
    public static String AiDecortication( String userInput) throws IOException{

        POSSample parsed = Parse(userInput);
        String[] important = ExtractImportant(parsed);
        
        return parsed.toString();
    }
    
    
}
