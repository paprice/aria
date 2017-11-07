/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConversationHandler;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import TypeWord.Word;

/**
 *
 * @author Gildo Conte
 */
public class CurrentConversation {

    //Contains only verbs and nouns gathered from one to multiple user inputs.
    private static List<String> currentSubjects = new ArrayList<>();

    //Contains the very last sentence sent by the user.
    private static String lastUserSentence = "";

    public static void addSubjectsFromList(List<Word> wordList) {
        String item;
        for (int i = 0; i < wordList.size(); ++i) {
            Document d = wordList.get(i).CreateDoc();
            if (d.containsValue("nc") || d.containsValue("v")) {
                item = (String) d.get("word");
                if (!currentSubjects.contains(item)) {
                    currentSubjects.add(item);
                }
            }
        }
    }

    public static void setLastUserSentence(String sentence) {
        lastUserSentence = sentence;
    }

    public static String getLastSentence() {
        return lastUserSentence;
    }
}
