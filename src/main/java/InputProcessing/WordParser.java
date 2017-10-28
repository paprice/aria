 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputProcessing;

import opennlp.tools.postag.POSSample;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author Patrice Desrochers
 */
public class WordParser {

    public static List<Document> ExtractAll(POSSample userInput) {

        String[] words = userInput.getSentence();
        String[] wordTags = userInput.getTags();

        List<Document> wordList = new ArrayList<Document>();

        for (int i = 0; i < wordTags.length; i++) {
            if (wordTags[i].equals("NC")) {
                wordList.add(new Document("type", "nc").append("word", words[i]));
            }
            else if (wordTags[i].equals("V")) {
                wordList.add(new Document("type", "v").append("word", words[i]));
            }
            else if (wordTags[i].equals("ADJ")) {
                wordList.add(new Document("type", "adj").append("word", words[i]));
            }
            else if (wordTags[i].equals("ADV")) {
                wordList.add(new Document("type", "adv").append("word", words[i]));
            }
            else if (wordTags[i].equals("NPP")) {
                wordList.add(new Document("type", "npp").append("word", words[i]));
            }
        }
        return wordList;
    }
    
    private static String ParseNoun (String noun){
        int last = noun.length() - 1;
        int sndLast = noun.length() - 2;
        
        //Case singular
        switch (noun.charAt(last)){
            
            //Case plural 's'
            case 's' :
                //skip if invariable "is"
                if (noun.endsWith("ois") || noun.endsWith("vis") || noun.endsWith("llis") || noun.endsWith("dis") || noun.endsWith("pis") ||
                        noun.endsWith("quis") || noun.endsWith("uis") || noun.endsWith("dais") || noun.endsWith("rais") || noun.endsWith("chis") ||
                        noun.endsWith("outis") || noun.endsWith("ssis") || noun.endsWith("bis") || noun.endsWith("stis") || noun.endsWith("mmis") ||
                        noun.endsWith("lais") || noun.endsWith("otis")){
                    return noun;
                }
                //skip if invariable exception
                else if (noun.length() == 3 || noun.equals("souris") || noun.equals("mépris") || noun.equals("indécis") || noun.equals("vernis")){
                    return noun;
                }
                //default
                else {
                    return noun.substring(0, noun.length() - 1);
                }
            
            //Case plural 'x'
            case 'x' :
                //if "is"
                if (noun.endsWith("ois") || noun.endsWith("vis") || noun.endsWith("llis") || noun.endsWith("dis") || noun.endsWith("pis") ||
                        noun.endsWith("quis") || noun.endsWith("uis") || noun.endsWith("dais") || noun.endsWith("rais") || noun.endsWith("chis") ||
                        noun.endsWith("outis") || noun.endsWith("ssis") || noun.endsWith("bis") || noun.endsWith("stis") || noun.endsWith("mmis") ||
                        noun.endsWith("lais") || noun.endsWith("otis")){
                    return noun;
                }
                //skip if invariable exception
                else if (noun.length() == 3 || noun.equals("souris") || noun.equals("mépris") || noun.equals("indécis") || noun.equals("vernis")){
                    return noun;
                }
                //default
                else {
                    return noun.substring(0, noun.length() - 1);
                }
            
            //Case singular
            default : return noun;
        }
    }

}
