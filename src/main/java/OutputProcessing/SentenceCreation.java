/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OutputProcessing;

import java.util.List;
import org.bson.Document;

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

    public static String GenerateResponse(List<Document> word) {
        String output = "";

        output = "Le support n'est pas encore implémenter";

        return output;
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
