/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Language;

import java.lang.reflect.Array;
import opennlp.tools.postag.POSSample;
import java.lang.String;

/**
 *
 * @author despa
 */
public class Words {

    public static String[] ExtractImportant(POSSample userInput) {
        

        String[] words = userInput.getSentence();
        String[] wordTags = userInput.getTags();

        

    }

}
