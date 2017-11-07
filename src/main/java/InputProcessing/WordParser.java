/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputProcessing;

import TypeWord.WordNoPref;
import TypeWord.Noun;
import TypeWord.Word;
import static ConversationHandler.CurrentConversation.addSubjectsFromList;
import java.io.IOException;
import opennlp.tools.postag.POSSample;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.languagetool.AnalyzedSentence;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.JLanguageTool;
import org.languagetool.language.French;

/**
 *
 * @author Patrice Desrochers
 * @author Gildo Conte
 */
public class WordParser {

    public static List<Word> ExtractAll(POSSample userInput) {

        int preference;
        String[] words = userInput.getSentence();
        String[] wordTags = userInput.getTags();
        String[] lemmatize = LemmatizeWord(words);

        List<Word> wordList = new ArrayList<>();

        for (int i = 0; i < wordTags.length; i++) {

            preference = getPreferenceValue(i, lemmatize, wordTags);
            String genre = wordTags[i].substring(wordTags[i].length() - 2, wordTags[i].length() - 1);
            String number = wordTags[i].substring(wordTags[i].length() - 1);

            if (wordTags[i].contains("NC")) {
                // Common name
                wordList.add(new Noun("nc",lemmatize[i],preference,genre,number));
                //wordList.add(new Document("type", "nc").append("word", lemmatize[i]).append("preference", preference).append("genre", genre));
            } else if (wordTags[i].contains("V") && !wordTags[i].contains("ADV")) {
                // Verb
                wordList.add(new Noun("v",lemmatize[i],preference,genre,number));
                //wordList.add(new Document("type", "v").append("word", lemmatize[i]).append("preference", preference));
            } else if (wordTags[i].contains("A") && !wordTags[i].contains("ADV")) {
                // Adjectives
                wordList.add(new WordNoPref("adj",lemmatize[i]));
                //wordList.add(new Document("type", "adj").append("word", lemmatize[i]));
            } else if (wordTags[i].contains("ADV")) {
                // Adverb
                wordList.add(new WordNoPref("adv",lemmatize[i]));
                //wordList.add(new Document("type", "adv").append("word", lemmatize[i]));
            } else if (wordTags[i].contains("NP")) {
                // Proper nouns
                wordList.add(new Noun("npp",lemmatize[i],preference,genre,number));
                //wordList.add(new Document("type", "npp").append("word", lemmatize[i]).append("preference", preference));
            } else if (wordTags[i].contains("CL")) {
                // Pronouns
                wordList.add(new WordNoPref("cls",lemmatize[i]));
                //wordList.add(new Document("type", "cls").append("word", lemmatize[i]));
            }
            /*
            
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
                case "CLS":
                    wordList.add(new Document("type", "cls").append("word", lemmatize[i]));
                    break;
                default:
                    break;
            }*/
        }
        addSubjectsFromList(wordList);

        return wordList;
    }

    private static String[] LemmatizeWord(String[] word) {
        String[] ret = new String[word.length];

        JLanguageTool lt = new JLanguageTool(new French());

        for (int i = 0; i < word.length; i++) {
            List<AnalyzedSentence> analyzedSentences = null;
            try {
                analyzedSentences = lt.analyzeText(word[i]);
            } catch (IOException ex) {
                Logger.getLogger(WordParser.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    private static int getPreferenceValue(int i, String[] words, String[] wordTags) {

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
            if (j == 1) {
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
