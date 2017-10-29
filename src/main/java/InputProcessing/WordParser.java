/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputProcessing;

import static ConversationHandler.CurrentConversation.addSubjectsFromList;
import static InputProcessing.SentenceParser.Chunker;
import java.io.IOException;
import opennlp.tools.postag.POSSample;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static List<Document> ExtractAll(POSSample userInput) throws IOException {

        String[] words = userInput.getSentence();
        String[] wordTags = userInput.getTags();
        String[] lemmatize = LemmatizeWord(words);

        List<Document> wordList = new ArrayList<>();

        for (int i = 0; i < wordTags.length; i++) {

            switch (wordTags[i]) {
                case "NC":
                    wordList.add(new Document("type", "nc").append("word", lemmatize[i]));
                    break;
                case "V":
                    wordList.add(new Document("type", "v").append("word", lemmatize[i]));
                    break;
                case "ADJ":
                    wordList.add(new Document("type", "adj").append("word", lemmatize[i]));
                    break;
                case "ADV":
                    wordList.add(new Document("type", "adv").append("word", lemmatize[i]));
                    break;
                case "NPP":
                    wordList.add(new Document("type", "npp").append("word", lemmatize[i]));
                    break;
                default:
                    break;
            }
        }
        addSubjectsFromList(wordList);
        
        String chunk[] = Chunker(words,wordTags);
        
        System.out.println(Arrays.toString(chunk));
        
        
        
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
                        ret[i] = analyzedTokens.getReadings().get(0).getLemma();
                    }
                }
            }
        }
        
        return ret;
    }

}
