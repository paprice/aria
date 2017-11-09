/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windows;

import static ConversationHandler.CurrentConversation.*;
import DataBase.MongoDB;
import static InputProcessing.SentenceParser.*;
import TypeWord.Word;
import static InputProcessing.WordParser.ExtractAll;
import static OutputProcessing.SentenceCreation.*;
import java.util.ArrayList;
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
        missingDefs = new ArrayList<>();
    }

    public String AiDecortication(String userInput, MongoDB db) {

        String output;
        boolean isQuestion = userInput.contains("?");

        //Parsing the user input
        userInput = PreParse(userInput);
        POSSample parsed = Parse(userInput);
        List<Word> important = ExtractAll(parsed);

        
        
        if (!isWaitingDef) {

            //Sending user input to LastConversation
            important = db.InsertOrUpdate(important);
            CheckDef(important, db);
            if (!isWaitingDef) {
                output = GenerateResponse(important, isQuestion);
            } else {
                output = AskDef(waitingDef);
            }

            setLastUserSentence(userInput);
        } else {
            output = InsertDef(important, db);
        }

        return output;
    }

    private void CheckDef(List<Word> important, MongoDB db) {
        String output = "";

        for (Word d : important) {
            int hasType = db.HaveDefinition(d.getWord(), "nc");
            if (hasType == 1) {
                missingDefs.add(d.getWord());
                isWaitingDef = true;
            }
        }
        if (!missingDefs.isEmpty()) {
            waitingDef = missingDefs.get(0);
        }
    }

    private String AskDef(String toDef) {
        return "Peux-tu définir " + toDef + " dans une catégorie ?";
    }

    private String InsertDef(List<Word> important, MongoDB db) {
        String output = "";
        for (Word d : important) {
            if ("nc".equals(d.getType())) {
                if (!d.getWord().equals(waitingDef)) {
                    Document upd = new Document("desc", d.getWord());
                    Document toUpdate = new Document("word", waitingDef);
                    db.UpdateType(toUpdate, upd, "names");
                    output = GenerateDefinitionResponse(waitingDef, d.getWord());
                    missingDefs.remove(0);
                    if (missingDefs.isEmpty()) {
                        isWaitingDef = false;
                        waitingDef = "";
                        sendResponse = true;
                        output = AiDecortication(getLastSentence(), db);
                    } else {
                        waitingDef = missingDefs.get(0);
                        output += "\n" + AskDef(waitingDef);
                        
                    }

                }
            }
        }

        return output;
    }
}
