/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OutputProcessing;

import ConversationHandler.Preference;
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
 * @author Gildo Conte
 */
public class SentenceCreation {

    public static String GenerateResponse(List<Document> words, MongoDB db) {
        String verb = "";
        String subject = "";
        String object = "";
        List<Document> def = new ArrayList<>();
        Lexicon lexicon = new XMLLexicon();
        NLGFactory factory = new NLGFactory(lexicon);
        Realiser realiser = new Realiser();
        
        GeneratePreferenceResponse(words, db);

        //ICI il faudrait faire des IF qui font la sélection de la bonne réponse à donner.
        //GenerateResponse devrait être notre fonction maîtresse qui pointe vers la bonne fonction pour générer la bonne réponse
        //selon le contenu de la dernière phrase. À Discuter.
        Document doc = words.get(words.size()-1);
        
        SPhraseSpec ret = factory.createClause();
        ret.setSubject(subject);
        ret.setObject(obj);
        if (!doc.containsValue("?")){
            ret.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        }
        
        return realiser.realiseSentence(ret);

    }

    public static String GeneratePreferenceResponse(List<Document> words, MongoDB db) {
        String verb = "";
        String subject = "";
        String object = "";
        List<Document> def = new ArrayList<>();

        Lexicon lexicon = new XMLLexicon();
        NLGFactory factory = new NLGFactory(lexicon);
        Realiser realiser = new Realiser();

        for (Document doc : words) {
            if (doc.getString("type").equals("nc")) {
                subject = Preference.ReturnPref(doc.getInteger("preference"));
                object = doc.getString("word");
            }
        }

        NPPhraseSpec obj = factory.createNounPhrase("le", object);
        obj.setPlural(true);
        
        
        SPhraseSpec ret = factory.createClause();
        ret.setSubject(subject);
        ret.setObject(obj);

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
