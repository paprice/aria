/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windows;

import static InputProcessing.WordParser.ExtractAll;
import static OutputProcessing.SentenceCreation.*;
import static ConversationHandler.CurrentConversation.*;
import static InputProcessing.SentenceParser.*;
import DataBase.*;
import Files.WriteJson;
import InputProcessing.Sentence;
import TypeWord.Word;
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
    private String verbDef;
    private List<String> adjDef;
    private String desc;
    private boolean sendResponse;
    private List<String> missingDefs;
    public static boolean wasLastQuestion = false;

    public WindowsController() {
        isWaitingDef = false;
        sendResponse = false;
        missingDefs = new ArrayList<>();
        adjDef = new ArrayList<>();
    }

    public String AiDecortication(String userInput, MongoDB db) {

        String output;
        //boolean isQuestion = userInput.contains("?");

        //Parsing the user input
        userInput = PreParse(userInput);
        POSSample parsed = Parse(userInput,false);
        POSSample withNumber = Parse(userInput, true);
        Sentence s = new Sentence();
        
        List<Word> important = ExtractAll(parsed,withNumber, s);
        
        for(Word w : important){
            System.out.println(w.getWord());
        }
        
        //Sentence s = PartitionnateSentence(parsed);

        //Updating User Preferences
        WriteJson.WritePreferenceData(User.Instance().getName(), User.Instance().getUserPreferences());

        if (!isWaitingDef) {

            setLastUserSentence(s);
            
            //Sending user input to LastConversation
            important = db.InsertOrUpdate(important);
            CheckDef(important, db);

            if (!isWaitingDef) {
                output = GenerateResponse(important/*, isQuestion*/);
            } else {
                output = AskDef(waitingDef);
            }

            
        } else {
            output = InsertDef(important, db);
        }

        return output;
    }

    private void CheckDef(List<Word> important, MongoDB db) {

        for (Word d : important) {
            switch (d.getType()) {
                case "v":
                    verbDef = d.getWord();
                    break;
                case "adj":
                    adjDef.add(d.getWord());
                    break;
                default:
                    int hasType = db.HaveDefinition(d.getWord(), "nc");
                    if (hasType == 1) {
                        missingDefs.add(d.getWord());
                        isWaitingDef = true;
                    } else if (d.getType().equals("nc")) {
                        desc = db.GetSingleDefinition(d.getWord(), "nc");
                    }
                    break;
            }
        }
        AddDescToVerb(desc, db);
        AddDescToAdj(desc, db);
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

                    AddDescToVerb(d.getWord(), db);
                    AddDescToAdj(d.getWord(), db);

                    output += GenerateDefinitionResponse(waitingDef, d.getWord());
                    missingDefs.remove(0);
                    if (missingDefs.isEmpty()) {
                        isWaitingDef = false;
                        waitingDef = "";
                        sendResponse = true;
                        output += AiDecortication(getLastSentence().toString(), db);
                    } else {
                        waitingDef = missingDefs.get(0);
                        output += "\n" + AskDef(waitingDef);
                    }

                }
            }
        }

        return output;
    }

    private void AddDescToVerb(String desc, MongoDB db) {

        Document verb = db.GetDocumentDefinition(verbDef, "v");
        if (verb != null) {
            List<String> descVerb = (List<String>) verb.get("desc");

            if (descVerb == null) {
                descVerb = new ArrayList<>();
            }

            if (!descVerb.contains(desc)) {
                descVerb.add(desc);
                Document upd = new Document("desc", descVerb);
                Document toUpdate = new Document("word", verbDef);

                db.UpdateType(toUpdate, upd, "verb");
            }
            verbDef = "";
        }
    }

    private void AddDescToAdj(String desc, MongoDB db) {

        for (String a : adjDef) {
            Document adj = db.GetDocumentDefinition(a, "adj");

            List<String> descAdj = (List<String>) adj.get("desc");

            if (descAdj == null) {
                descAdj = new ArrayList<>();
            }

            if (!descAdj.contains(desc)) {
                descAdj.add(desc);
                Document upd = new Document("desc", descAdj);
                Document toUpdate = new Document("word", adj.getString("word"));

                db.UpdateType(toUpdate, upd, "adj");
            }

        }

        adjDef = new ArrayList<>();

    }

}
