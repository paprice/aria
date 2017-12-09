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
    
    //Contains critical information pertaining to the last user sentence
    private static Context context;

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
                    preferenceList.add(MongoDB.Instance().GetPreference(item, type));
                }
            }
        }
        setContextFromList(wordList);
    }

    public static void setLastUserSentence(String sentence) {
        lastUserSentence = sentence;
    }

    public static String getLastSentence() {
        return lastUserSentence;
    }
    
    public static String getAIFavoredCurrentSubject() {
        int highest = 0;
        for (Integer i : preferenceList) {
            if (preferenceList.get(i) > highest) {
                highest = preferenceList.get(i);
            }
        }
        return currentSubjects.get(highest);
    }
    
    public static String getUserFavoredCurrentSubject() {
        //TBD
        return null;
    }
    
    public static void setContextFromList(List<Word> wordList) {
        Document doc;
        boolean verb = false;
        
        //Empty verification
        if (wordList.isEmpty()){return;}
        
        //Question
        doc = wordList.get(wordList.size() - 1).CreateDoc();
        if (doc.containsValue("?")) {
            context = Context.QUESTION;
            return;
        }
        
        //Answer
        for (int i = 0; i < wordList.size(); ++i) {
            doc = wordList.get(i).CreateDoc();
            if (doc.containsValue("oui") || doc.containsValue("non")) {
                context = Context.REPONSE;
                return;
            }
            if (!verb && doc.containsValue("v")) {
                verb = true;
            }
        }
        if (!verb){
            context = Context.REPONSE;
            return;
        }
        
        //Statement
        context = Context.AFFIRMATION;
        
    }
}
