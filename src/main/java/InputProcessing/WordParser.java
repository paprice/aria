/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputProcessing;

import static ConversationHandler.CurrentConversation.addSubjectsFromList;
import java.io.IOException;
import opennlp.tools.postag.POSSample;
import java.util.ArrayList;
import java.util.List;
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

    public static List<Document> ExtractAll(POSSample userInput, boolean question) throws IOException {

        int preference = 0;
        String[] words = userInput.getSentence();
        String[] wordTags = userInput.getTags();
        String[] lemmatize = LemmatizeWord(words);

        List<Document> wordList = new ArrayList<>();

        for (int i = 0; i < wordTags.length; i++) {

            preference = getPreferenceValue(i, lemmatize, wordTags);

            switch (wordTags[i]) {
                case "NC":
                    wordList.add(new Document("type", "nc").append("word", lemmatize[i]).append("preference", preference));
                    break;
                case "V":
                    wordList.add(new Document("type", "v").append("word", lemmatize[i]).append("preference", preference));
                    break;
                case "ADJ":
                    wordList.add(new Document("type", "adj").append("word", lemmatize[i]));
                    break;
                case "ADV":
                    wordList.add(new Document("type", "adv").append("word", lemmatize[i]));
                    break;
                case "NPP":
                    wordList.add(new Document("type", "npp").append("word", lemmatize[i]).append("preference", preference));
                    break;
                default:
                    break;
            }
        }
        addSubjectsFromList(wordList);

        return wordList;
    }

    private static String[] LemmatizeWord(String[] word) throws IOException {
        String[] ret = new String[word.length];

        JLanguageTool lt = new JLanguageTool(new French());

        for (int i = 0; i < word.length; i++) {
            List<AnalyzedSentence> analyzedSentences = lt.analyzeText(word[i]);
            for (AnalyzedSentence analyzedSentence : analyzedSentences) {
                for (AnalyzedTokenReadings analyzedTokens : analyzedSentence.getTokensWithoutWhitespace()) {
                    if (analyzedTokens.getReadings().size() > 0) {
                        if (analyzedTokens.getReadings().get(0).getLemma() != null) {
                            ret[i] = analyzedTokens.getReadings().get(0).getLemma();
                        }
                    }
                }
            }
        }
        return ret;
    }

    private static int getPreferenceValue(int i, String[] words, String[] wordTags) throws IOException {

        //Normal sentence
        for (int j = 0; j < i; ++j) {
            //Cases for decreasing preference
            if (wordTags[j].equals("V") && (words[j].equals("détester") || words[j].equals("haïr"))) {
                return -1;
            }

            if (wordTags[j].equals("V") && ((j < words.length - 1 && words[j].equals("aimer") && words[j + 1].equals("pas"))
                    || (j < words.length - 2 && words[j].equals("aimer") && words[j + 2].equals("pas")))) {
                return -1;
            }

            //Cases for raising preference
            if (wordTags[j].equals("V")
                    && (words[j].equals("aimer") || words[j].equals("adorer") || words[j].equals("préférer") || words[j].equals("idolâtrer"))) {
                return 1;
            }
        }

        //Inverted sentence
        for (int j = words.length - 1; j > i; --j) {
            //Cases for decreasing preference
            if(j == 1){
                int k = 0;
            }
            
            if (wordTags[j].equals("V") && (words[j].equals("déplaire") || words[j].equals("dégoûter"))) {
                return -1;
            }

            if (((words[j].equals("plaire") && words[j + 1].equals("pas")))
                    || (j < words.length - 2 && (words[j].equals("plaire") && words[j + 2].equals("pas")))) {
                return -1;
            }

            //Cases for raising preference
            if (wordTags[j].equals("V") && words[j].equals("plaire")) {
                return 1;
            }
        }
        return 0;
    }

}
