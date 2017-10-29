/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windows;

import DataBase.MongoDB;
import static InputProcessing.SentenceParser.Parse;
import static InputProcessing.SentenceParser.PreParse;
import static InputProcessing.WordParser.ExtractAll;
import static OutputProcessing.SentenceCreation.GenerateResponse;
import java.io.IOException;
import java.util.List;
import opennlp.tools.postag.POSSample;
import org.bson.Document;


/*Faire fonctionner les dépendance : 
Aller  dans le dossier Dependencies et click droit sur la dépendance SimpleNLG-EnFr-1.1.jar
Ensuite tu fais manually install artifact
sélectionne le bon jar qui est dans le dossier libs/
 */
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.realiser.Realiser;

/**
 *
 * @author Patrice Desrochers
 */
public class WindowsController {

    private boolean isWaitingDef;
    private String waitingDef;

    public WindowsController() {
        isWaitingDef = false;
    }

    public String AiDecortication(String userInput, MongoDB db) throws IOException {

        String output = "";

        userInput = PreParse(userInput);
        POSSample parsed = Parse(userInput);
        List<Document> important = ExtractAll(parsed);

        db.InsertOrUpdate(important);

        output = CheckDef(important, db, output);
        if (output.equals("")) {
            output = GenerateResponse(important);
        }
        return output;
    }

    private String CheckDef(List<Document> important, MongoDB db, String output) {
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
                    output = GenerateResponse(waitingDef, d.getString("word"));
                    isWaitingDef = false;
                    waitingDef = "";
                }
            }
        }
        return output;
    }
}
