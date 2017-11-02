/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windows;

import static ConversationHandler.CurrentConversation.*;
import DataBase.MongoDB;
import static InputProcessing.SentenceParser.*;
import static InputProcessing.WordParser.ExtractAll;
import static OutputProcessing.SentenceCreation.*;
import java.util.List;
import opennlp.tools.postag.POSSample;
import org.bson.Document;

/**
 *
 * @author Patrice Desrochers
 * @author Gildo Conte
 */
public class WindowsController {

    private boolean isWaitingDef;
    private String waitingDef;
    private boolean sendResponse;
    private List<String> missingDefs;
    
    
    
    public WindowsController() {
        isWaitingDef = false;
        sendResponse = false;
        missingDefs = null;
    }

    public String AiDecortication(String userInput, MongoDB db) {

        String output;
        boolean isQuestion = userInput.contains("?");

        //Parsing the user input
        userInput = PreParse(userInput);
        POSSample parsed = Parse(userInput);
        List<Document> important = ExtractAll(parsed, isQuestion);

        //Sending user input to LastConversation
        important = db.InsertOrUpdate(important);

        output = CheckDef(important, db);
        if (output.equals("")) {
            output = GenerateResponse(important, isQuestion);
        }

        if (sendResponse) {
            sendResponse = false;
            output += "\n" + AiDecortication(getLastSentence(), db);

        }

        setLastUserSentence(userInput);

        return output;
    }

    private String CheckDef(List<Document> important, MongoDB db) {
        String output = "";

        if (!isWaitingDef) {
            for (Document d : important) {
                int hasType = db.HaveDefinition(d.getString("word"), "nc");
                if (hasType == 1) {
                    isWaitingDef = true;
                    output = "Peux-tu définir " + d.getString("word") + " dans une catégorie ?";
                    waitingDef = d.getString("word");
                }
            }
        } else {
            for (Document d : important) {
                if ("nc".equals(d.getString("type"))) {
                    Document upd = new Document("desc", d.getString("word"));
                    Document toUpdate = new Document("word", waitingDef);
                    db.UpdateType(toUpdate, upd, "names");
                    output = GenerateDefinitionResponse(waitingDef, d.getString("word"));
                    isWaitingDef = false;
                    waitingDef = "";
                    sendResponse = true;
                }
            }
        }
        return output;
    }
}
