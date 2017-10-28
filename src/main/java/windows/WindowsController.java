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
    private String WaitingDef;

    public WindowsController() {
        isWaitingDef = false;
    }

    public String AiDecortication(String userInput, MongoDB db) throws IOException {

        String output = "";

        userInput = PreParse(userInput);
        POSSample parsed = Parse(userInput);
        List<Document> important = ExtractAll(parsed);

        db.InsertOrUpdate(important);

        if (!isWaitingDef) {
            for (Document d : important) {
                int hasType = db.FindType(d.getString("word"), "nc");
                if (hasType == 1) {
                    isWaitingDef = true;
                    output = "Que veux dire " + d.getString("word") + "?";
                    WaitingDef = d.getString("word");
                }
            }

            Lexicon lexicon = new simplenlg.lexicon.french.XMLLexicon();
            NLGFactory factory = new NLGFactory(lexicon);
            Realiser realiser = new Realiser();
            /*NPPhraseSpec theMan = factory.createNounPhrase("le", "homme");
            NPPhraseSpec theCrowd = factory.createNounPhrase("le", "foule");

            SPhraseSpec greeting
                    = factory.createClause(theMan, "saluer", theCrowd);

            SPhraseSpec p = factory.createClause();
             */
            if (!isWaitingDef) {
                boolean entree = false;
                for (Document d : important) {
                    if (!entree) {
                        entree = true;
                        output = d.getString("word");
                    }
                }
            }
        } else {
            for (Document d : important) {
                Document upd = new Document("desc", d.getString("word"));
                Document toUpdate = new Document("word", WaitingDef);
                db.UpdateType(toUpdate, upd, "names");
                output = "Merci";
                isWaitingDef = false;
                WaitingDef = "";
            }
        }
        return output;
    }
}
