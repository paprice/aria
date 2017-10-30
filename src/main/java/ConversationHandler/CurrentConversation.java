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
 * @author Gildo
 */
public class CurrentConversation {
    
    private static List<String> currentSubjects = new ArrayList<>();
    private static String lastUserSentence = "";
    
    public static void addSubjectsFromList (List<Document> wordList){
        String item;
        for (int i = 0; i < wordList.size(); ++i){
            if (wordList.get(i).containsValue("nc")){
                item = (String) wordList.get(i).get("word");
                if (!currentSubjects.contains(item)){
                    currentSubjects.add(item);
                }
            }
        }
    }
}
