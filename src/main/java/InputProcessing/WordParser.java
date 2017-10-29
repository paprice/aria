/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputProcessing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.postag.POSSample;
import java.util.ArrayList;
import java.util.List;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import org.bson.Document;

/**
 *
 * @author Patrice Desrochers
 */
public class WordParser {

    public static List<Document> ExtractAll(POSSample userInput) throws IOException {

        String[] words = userInput.getSentence();
        String[] wordTags = userInput.getTags();

        List<Document> wordList = new ArrayList<Document>();

        Lemmatize(words, wordTags);

        for (int i = 0; i < wordTags.length; i++) {

            if (wordTags[i].equals("NC")) {
                wordList.add(new Document("type", "nc").append("word", parseNoun(words[i])));
            } else if (wordTags[i].equals("V")) {
                wordList.add(new Document("type", "v").append("word", words[i]));
            } else if (wordTags[i].equals("ADJ")) {
                wordList.add(new Document("type", "adj").append("word", words[i]));
            } else if (wordTags[i].equals("ADV")) {
                wordList.add(new Document("type", "adv").append("word", words[i]));
            } else if (wordTags[i].equals("NPP")) {
                wordList.add(new Document("type", "npp").append("word", words[i]));
            }
        }
        return wordList;
    }

    private static String parseNoun(String noun) {
        int last = noun.length() - 1;
        int sndLast = noun.length() - 2;

        //Case singular
        switch (noun.charAt(last)) {

            //Case plural 's'
            case 's':
                //skip if invariable "is"
                if (noun.endsWith("ois") || noun.endsWith("vis") || noun.endsWith("llis") || noun.endsWith("dis") || noun.endsWith("pis")
                        || noun.endsWith("quis") || noun.endsWith("uis") || noun.endsWith("dais") || noun.endsWith("rais") || noun.endsWith("chis")
                        || noun.endsWith("outis") || noun.endsWith("ssis") || noun.endsWith("bis") || noun.endsWith("stis") || noun.endsWith("mmis")
                        || noun.endsWith("lais") || noun.endsWith("otis")) {
                    return noun;
                } //skip if invariable exception
                else if (noun.length() == 3 || noun.equals("souris") || noun.equals("mépris") || noun.equals("indécis") || noun.equals("vernis")) {
                    return noun;
                } //default
                else {
                    return noun.substring(0, last);
                }

            //Case plural 'x'
            case 'x':
                //skip if singular form "aux"
                if (noun.endsWith("aux") && noun.length() == 4) {
                    return noun;
                } //if exception form "yeux"
                else if (noun.equals("yeux")) {
                    return "oeil";
                } //if exception form "ail" to "aux"
                else if (noun.equals("coraux") || noun.equals("fermaux") || noun.equals("gemmaux") || noun.equals("soupiraux")
                        || noun.equals("vantaux") || noun.equals("ventaux") || noun.equals("vitraux")) {
                    return noun.substring(0, sndLast) + "il";
                } //if "oux" form
                else if (noun.endsWith("oux") && noun.length() > 4) {
                    return noun.substring(0, last);
                } //if "eux" form
                else if (noun.endsWith("eux")) {
                    return noun.substring(0, last);
                } //if "eaux" form
                else if (noun.endsWith("eaux")) {
                    return noun.substring(0, last);
                } //if "au" to "aux" form
                else if (noun.endsWith("yaux") || noun.endsWith("baux") || noun.endsWith("iaux")) {
                    return noun.substring(0, last);
                } //if "al" to "aux" form
                else if (noun.endsWith("aux")) {
                    return noun.substring(0, sndLast) + "l";
                } //default skip
                else {
                    return noun;
                }

            //Case singular
            default:
                return noun;
        }
    }

    private static void Lemmatize(String[] words, String[] wordTags) throws FileNotFoundException, IOException {
        LemmatizerModel model = null;
        InputStream modelIn; 
        modelIn = new FileInputStream("models/fr.hfst.ol");

        model = new LemmatizerModel(modelIn);
        LemmatizerME lemmatizer = new LemmatizerME(model);

    }
}
