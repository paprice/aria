/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OutputProcessing;

import ConversationHandler.Preference;
import DataBase.MongoDB;
import java.util.List;
import org.bson.Document;
import simplenlg.features.Feature;

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

    public static String GenerateResponse(List<Document> words, MongoDB db) {
        String output;

        output = GeneratePreferenceResponse(words, db);

        //ICI il faudrait faire des IF qui font la sélection de la bonne réponse à donner.
        //GenerateResponse devrait être notre fonction maîtresse qui pointe vers la bonne fonction pour générer la bonne réponse
        //selon le contenu de la dernière phrase. À Discuter.
        return output;

    }

    public static String GeneratePreferenceResponse(List<Document> words, MongoDB db) {
        String verb = "";
        String subject = "";
        String object = "";
        String ono = "";
        boolean neg = false;

        Lexicon lexicon = new XMLLexicon();
        NLGFactory factory = new NLGFactory(lexicon);
        Realiser realiser = new Realiser();

        for (Document doc : words) {
            if (doc.getString("type").equals("nc")) {
                Document pref = Preference.ReturnPref(doc.getInteger("preference"));
                object = doc.getString("word");
                subject = pref.getString("subject");
                verb = pref.getString("verb");
                ono = pref.getString("ono");
                neg = pref.getBoolean("neg");
            }
        }


        NPPhraseSpec obj = factory.createNounPhrase("le", object);
        obj.setPlural(true);

        SPhraseSpec ret = factory.createClause();
        ret.addFrontModifier(ono);
        ret.setSubject(subject);
        ret.setVerb(verb);
        ret.setObject(obj);
        ret.setFeature(Feature.NEGATED, neg);        

        return realiser.realiseSentence(ret);

    }

    public static String GenerateDefinitionResponse(String toDef, String def) {
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
