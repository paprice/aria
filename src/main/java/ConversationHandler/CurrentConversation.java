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
import DataBase.MongoDB;
import static edu.berkeley.nlp.lm.util.Logger.i;

/**
 *
 * @author Gildo Conte
 */
public class CurrentConversation {

    //Contains only verbs and nouns gathered from one to multiple user inputs.
    private static List<String> currentSubjects = new ArrayList<>();
    private static List<Integer> preferenceList = new ArrayList<>();

    //Contains the very last sentence sent by the user.
    private static String lastUserSentence = "";

    public static void addSubjectsFromList(List<Word> wordList) {
        String item;
        String type;
        for (int i = 0; i < wordList.size(); ++i) {
            Document d = wordList.get(i).CreateDoc();
            if (d.containsValue("nc") || d.containsValue("v")) {
                item = (String) d.get("word");
                type = (String) d.get("type");
                //There will be no dynamic updating of a word's preference value in the currentSubjects to save execution time
                if (!currentSubjects.contains(item)) {
                    currentSubjects.add(item);
                    preferenceList.add(new Integer(new MongoDB().GetPreference(item, type)));
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
    
    public static String getFavoredCurrentSubject() {
        int highest = 0;
        for (Integer i : preferenceList) {
            if (preferenceList.get(i) > highest) {
                highest = preferenceList.get(i);
            }
        }
        return currentSubjects.get(highest);
    }
}
