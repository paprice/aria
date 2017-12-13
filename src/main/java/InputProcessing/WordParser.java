/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputProcessing;

import ConversationHandler.CurrentConversation;
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

    public static List<Word> ExtractAll(POSSample userInput, POSSample withNumber, Sentence sentence) {

        int preference;
        String[] words = userInput.getSentence();
        String[] wordTags = userInput.getTags();
        String[] lemmatize = LemmatizeWord(words);
        String[] wordTagsWithNumber = withNumber.getTags();
        String chunks[] = SentenceParser.Chunker(userInput);

        List<Word> wordList = new ArrayList<>();

        boolean isAddingSubject = true;
        boolean isAddingVerb = false;

        boolean start = false;

        for (int i = 0; i < wordTags.length; i++) {

            preference = getPreferenceValue(i, lemmatize, wordTags);
            Word temp = null;
            if (wordTags[i].contains("NC") && !wordTags[i].contains("PONCT")) {
                // Common name
                String genre = wordTagsWithNumber[i].substring(wordTagsWithNumber[i].length() - 2, wordTagsWithNumber[i].length() - 1);
                String number = wordTagsWithNumber[i].substring(wordTagsWithNumber[i].length() - 1);
                temp = new Noun("nc", lemmatize[i], preference, lemmatize[i - 1], genre, number);
                wordList.add(temp);
            } else if (wordTags[i].contains("V") && !wordTags[i].contains("ADV")) {
                // Verb
                temp = new Verb("v", lemmatize[i], preference);
                wordList.add(temp);
            } else if (wordTags[i].contains("A") && !wordTags[i].contains("ADV")) {
                // Adjectives
                temp = new WordNoPref("adj", lemmatize[i]);
                wordList.add(temp);
            } else if (wordTags[i].contains("ADV")) {
                // Adverb
                temp = new WordNoPref("adv", lemmatize[i]);
                wordList.add(temp);
            } else if (wordTags[i].contains("NP")) {
                // Proper nouns
                wordList.add(new ProperNoun("npp", lemmatize[i], preference));
            } else if (wordTags[i].contains("CL")) {
                Word replace = null;
                String genre = wordTagsWithNumber[i].substring(wordTagsWithNumber[i].length() - 2, wordTagsWithNumber[i].length() - 1);
                String number = wordTagsWithNumber[i].substring(wordTagsWithNumber[i].length() - 1);

                Sentence sent = CurrentConversation.getLastSentence();

                for (Word w : sent.getSubject()) {
                    if (w.getType().equals("nc")) {
                        if (/*w.getKind() == genre &&*/w.getNumber().equals(number)) {
                            replace = w;
                        }
                    }
                }
                if (replace == null) {
                    for (Word w : sent.getObject()) {
                        if (w.getType().equals("nc")) {
                            if (/*w.getKind() == genre &&*/w.getNumber().equals(number)) {
                                replace = w;
                            }
                        }
                    }
                }
                if (replace == null) {
                    temp = new WordNoPref("cls", lemmatize[i]);
                    wordList.add(temp);
                } else {
                    temp = new Noun(replace.getType(), replace.getWord(), replace.getPreference(), replace.getDet(), replace.getKind(), replace.getNumber());
                    wordList.add(temp);
                }

            } else {
                temp = new WordNoPref("det", words[i]);
            }

            if (chunks[i].substring(0, 1).equals("B")) {
                if (start) {
                    if (isAddingSubject) {
                        isAddingSubject = false;
                        isAddingVerb = true;
                        sentence.addVerb(temp.clone());
                    } else if (isAddingVerb) {
                        if (!wordTags[i].contains("V")) {
                            isAddingVerb = false;
                        }
                        sentence.addObject(temp.clone());
                    } else {
                        sentence.addObject(temp.clone());
                    }
                } else {
                    if (isAddingSubject) {
                        sentence.addSubject(temp.clone());
                        start = true;
                    } else if (isAddingVerb) {
                        sentence.addVerb(temp.clone());
                    } else {
                        sentence.addObject(temp.clone());
                    }
                }
            } else {
                if (isAddingSubject) {
                    if (!wordTags[i].contains("V")) {
                        sentence.addSubject(temp.clone());
                    } else {
                        sentence.addVerb(temp.clone());
                        isAddingSubject = false;
                        isAddingVerb = true;
                    }
                } else if (isAddingVerb) {
                    if (wordTags[i].contains("V")) {
                        sentence.addVerb(temp.clone());
                    } else {
                        sentence.addObject(temp.clone());
                    }
                } else {
                    sentence.addObject(temp.clone());
                }
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
                Logger.getLogger(WordParser.class
                        .getName()).log(Level.SEVERE, null, ex);
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

        if (!wordTags[pos].equals("NC") && !wordTags[pos].equals("V")) {
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
