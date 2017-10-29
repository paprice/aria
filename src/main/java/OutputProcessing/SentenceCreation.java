/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OutputProcessing;

import DataBase.MongoDB;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/*Faire fonctionner les dépendance : 
Aller  dans le dossier Dependencies et click droit sur la dépendance SimpleNLG-EnFr-1.1.jar
Ensuite tu fais manually install artifact
sélectionne le bon jar qui est dans le dossier libs/
 */
import simplenlg.framework.*;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.french.XMLLexicon;
import simplenlg.phrasespec.*;
import simplenlg.realiser.*;

/**
 *
 * @author Patrice Desrochers
 */
public class SentenceCreation {

    public static String GenerateResponse(List<Document> words, MongoDB db) throws FileNotFoundException, IOException {
        String verb = "";
        String subject = "je";
        String object = "";
        List<Document> def = new ArrayList<>();

        Lexicon lexicon = new XMLLexicon();
        NLGFactory factory = new NLGFactory(lexicon);
        Realiser realiser = new Realiser();

        for (Document doc : words) {
            if (doc.getString("type").equals("v")) {
                verb = doc.getString("word");
            }
            else if (doc.getString("type").equals("nc")) {
                def = db.FindDesc(doc.getString("word"));
            }
        }

        int count = 0;

        for (Document d : def) {
            int c = d.getInteger("count");
            if (c > count) {
                object = d.getString("word");
            }
        }

        SPhraseSpec ret = factory.createClause(subject, verb, object);

        return realiser.realiseSentence(ret);

    }

    public static String GenerateResponse(String toDef, String def) {
        String output;

        Lexicon lexicon = new XMLLexicon();
        NLGFactory factory = new NLGFactory(lexicon);
        Realiser realiser = new Realiser();

        NPPhraseSpec defi = factory.createNounPhrase("un", def);
        SPhraseSpec ret = factory.createClause("Merci, maitenant je sais que " + toDef, "être", defi);

        output = realiser.realiseSentence(ret);

        return output;
    }

}
