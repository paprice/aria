/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputProcessing;

import static ConversationHandler.CurrentConversation.addSubjectsFromList;
import DataBase.*;
import TypeWord.*;
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
            
            if (wordTags[i].contains("NC") && !wordTags[i].contains("PONCT")) {
                // Common name
                //String genre = wordTags[i].substring(wordTags[i].length() - 2, wordTags[i].length() - 1);
                //String number = wordTags[i].substring(wordTags[i].length() - 1);
                wordList.add(new Noun("nc", lemmatize[i], preference, lemmatize[i - 1]));
            } else if (wordTags[i].contains("V") && !wordTags[i].contains("ADV")) {
                // Verb
                wordList.add(new Verb("v", lemmatize[i], preference));
            } else if (wordTags[i].contains("A") && !wordTags[i].contains("ADV")) {
                // Adjectives
                wordList.add(new WordNoPref("adj", lemmatize[i]));
            } else if (wordTags[i].contains("ADV")) {
                // Adverb
                wordList.add(new WordNoPref("adv", lemmatize[i]));
            } else if (wordTags[i].contains("NP")) {
                // Proper nouns
                wordList.add(new ProperNoun("npp", lemmatize[i], preference));
            } else if (wordTags[i].contains("CL")) {
                // Pronouns
                wordList.add(new WordNoPref("cls", lemmatize[i]));
            }
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

    private static int getPreferenceValue(int pos, String[] words, String[] wordTags) {

        if (!wordTags[pos].equals("NC") && !wordTags[pos].equals("V")){
            return 0;
        }
        //Normal sentence
        for (int j = 0; j < pos; ++j) {
            //Cases for decreasing preference
            if (wordTags[j].equals("V") && (words[j].equals("détester") || words[j].equals("haïr"))) {
                User.Instance().addPreference(new UserPreference(words[pos], -1));
                return -1;
            }

            if (wordTags[j].equals("V") && ((j < words.length - 1 && words[j].equals("aimer") && words[j + 1].equals("pas"))
                    || (j < words.length - 2 && words[j].equals("aimer") && words[j + 2].equals("pas")))) {
                User.Instance().addPreference(new UserPreference(words[pos], -1));
                return -1;
            }

            //Cases for raising preference
            if (wordTags[j].equals("V")
                    && (words[j].equals("aimer") || words[j].equals("adorer") || words[j].equals("préférer") || words[j].equals("idolâtrer"))) {
                User.Instance().addPreference(new UserPreference(words[pos], 1));
                return 1;
            }
        }

        //Inverted sentence
        for (int j = words.length - 1; j > pos; --j) {
            //Cases for decreasing preference
            if (j == 1) {
                int k = 0;
            }

            if (wordTags[j].equals("V") && (words[j].equals("déplaire") || words[j].equals("dégoûter"))) {
                User.Instance().addPreference(new UserPreference(words[pos], -1));
                return -1;
            }

            if (wordTags[j].equals("V") && (((words[j].equals("plaire") && words[j + 1].equals("pas")))
                    || (j < words.length - 2 && (words[j].equals("plaire") && words[j + 2].equals("pas"))))) {
                User.Instance().addPreference(new UserPreference(words[pos], -1));
                return -1;
            }
            //Cases for raising preference
            if (wordTags[j].equals("V") && words[j].equals("plaire")) {
                User.Instance().addPreference(new UserPreference(words[pos], 1));
                return 1;
            }
        }
        return 0;
    }

}
