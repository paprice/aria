 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Language;

import opennlp.tools.postag.POSSample;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author Patrice Desrochers
 */
public class Words {

    public static List<Document> ExtractImportant(POSSample userInput) {

        String[] words = userInput.getSentence();
        String[] wordTags = userInput.getTags();

        List<Document> important = new ArrayList<Document>();

        for (int i = 0; i < wordTags.length; i++) {
            if (wordTags[i].equals("NPP") || wordTags[i].equals("NC")) {
                important.add(new Document("type", "nc").append("word", words[i]));
            }
        }

        return important;
    }

}
