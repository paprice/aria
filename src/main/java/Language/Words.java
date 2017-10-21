/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Language;

import opennlp.tools.postag.POSSample;
import java.util.ArrayList;

/**
 *
 * @author despa
 */
public class Words {

    public static ArrayList<String> ExtractImportant(POSSample userInput) {

        String[] words = userInput.getSentence();
        String[] wordTags = userInput.getTags();

        ArrayList<String> important = new ArrayList<String>();

        for (int i = 0; i < wordTags.length; i++) {
            if (wordTags[i].equals("NPP") || wordTags[i].equals("NC")) {
                important.add(words[i]);
            }
        }

        return important;
    }

}
