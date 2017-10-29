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
import org.languagetool.AnalyzedSentence;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.JLanguageTool;
import org.languagetool.language.French;

/**
 *
 * @author Patrice Desrochers
 */
public class WordParser {

    public static List<Document> ExtractAll(POSSample userInput) throws IOException {

        String[] words = userInput.getSentence();
        String[] wordTags = userInput.getTags();
        String[] lemmatize = LemmatizeWord(words);

        List<Document> wordList = new ArrayList<Document>();

        for (int i = 0; i < wordTags.length; i++) {

            if (wordTags[i].equals("NC")) {
                wordList.add(new Document("type", "nc").append("word", lemmatize[i]));
            } else if (wordTags[i].equals("V")) {
                wordList.add(new Document("type", "v").append("word", lemmatize[i]));
            } else if (wordTags[i].equals("ADJ")) {
                wordList.add(new Document("type", "adj").append("word", lemmatize[i]));
            } else if (wordTags[i].equals("ADV")) {
                wordList.add(new Document("type", "adv").append("word", lemmatize[i]));
            } else if (wordTags[i].equals("NPP")) {
                wordList.add(new Document("type", "npp").append("word", lemmatize[i]));
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

    private static String[] LemmatizeWord(String[] word) throws IOException {
        String[] ret = new String[word.length];
        
        JLanguageTool lt = new JLanguageTool(new French());

        for (int i = 0; i < word.length; i++) {
            List<AnalyzedSentence> analyzedSentences = lt.analyzeText(word[i]);
            for (AnalyzedSentence analyzedSentence : analyzedSentences) {
                for (AnalyzedTokenReadings analyzedTokens : analyzedSentence.getTokensWithoutWhitespace()) {
                    if (analyzedTokens.getReadings().size() > 0) {
                        ret[i] = analyzedTokens.getReadings().get(0).getLemma();
                    }
                }
            }
        }
        
        return ret;
    }

}
