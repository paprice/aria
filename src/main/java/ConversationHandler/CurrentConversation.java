/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConversationHandler;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author Gildo Conte
 */
public class CurrentConversation {
    
    //Contains only verbs and nouns gathered from one to multiple user inputs.
    private static List<String> currentSubjects = new ArrayList<>();
    
    //Contains the very last sentence sent by the user.
    private static String lastUserSentence = "";
    
    public static void addSubjectsFromList (List<Document> wordList){
        String item;
        for (int i = 0; i < wordList.size(); ++i){
            if (wordList.get(i).containsValue("nc") || wordList.get(i).containsValue("v")){
                item = (String) wordList.get(i).get("word");
                if (!currentSubjects.contains(item)){
                    currentSubjects.add(item);
                }
            }
        }
    }
    
    public static void setLastUserSentence (String sentence){
        lastUserSentence = sentence;
    }
}
