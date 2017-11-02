/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OutputProcessing;

import ConversationHandler.Preference;
import java.util.List;
import org.bson.Document;
import simplenlg.features.Feature;
import simplenlg.features.InterrogativeType;

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

    private static Lexicon lexicon;
    private static NLGFactory factory;
    private static Realiser realiser;

    public static void InitializeRealiser() {
        lexicon = new XMLLexicon();
        factory = new NLGFactory(lexicon);
        realiser = new Realiser();
    }

    public static String GenerateResponse(List<Document> words, boolean isQuestion) {
        String output;

        //ICI il faudrait faire des IF qui font la sélection de la bonne réponse à donner.
        //GenerateResponse devrait être notre fonction maîtresse qui pointe vers la bonne fonction pour générer la bonne réponse
        //selon le contenu de la dernière phrase. À Discuter.
        if (!isQuestion) {
            output = GenerateQuestionResponse(words);
        } else {
            output = GeneratePreferenceResponse(words);
        }

        return output;

    }

    public static String GeneratePreferenceResponse(List<Document> words) {
        String verb = "";
        String subject = "";
        String object = "";
        String ono = "";
        String det = "";
        String comp = "";
        boolean neg = false;

        for (Document doc : words) {
            if (doc.getString("type").equals("nc")) {
                Document pref = Preference.ReturnPref(doc.getInteger("preference"));
                object = doc.getString("word");
                subject = pref.getString("subject");
                verb = pref.getString("verb");
                ono = pref.getString("ono");
                det = pref.getString("det");
                neg = pref.getBoolean("neg");
                comp = pref.getString("comp");
            }
        }

        NPPhraseSpec obj = factory.createNounPhrase(det, object);
        obj.setPlural(true);

        VPPhraseSpec ve = factory.createVerbPhrase(verb);
        ve.setFeature(Feature.NEGATED, neg);
        ve.addComplement(comp);

        SPhraseSpec ret = factory.createClause();
        ret.addFrontModifier(ono);
        ret.setSubject(subject);
        ret.setVerb(ve);
        ret.setObject(obj);

        return realiser.realiseSentence(ret);

    }

    public static String GenerateDefinitionResponse(String toDef, String def) {
        String output;

        NPPhraseSpec defi = factory.createNounPhrase("un", def);
        SPhraseSpec ret = factory.createClause("Merci, maitenant je sais que " + toDef, "être", defi);

        output = realiser.realiseSentence(ret);

        return output;
    }

    private static String GenerateQuestionResponse(List<Document> words) {
        String verb = "";
        String subject = "";
        String object = "";
        NPPhraseSpec obj = factory.createNounPhrase();
        
        
        for (Document doc : words) {
            if (doc.getString("type").equals("v")) {
                verb = doc.getString("word");
            } else if (doc.getString("type").equals("nc")) {
                object = doc.getString("word");
            }

            String pronom = doc.getString("cls");
            if (pronom != null) {
                if (pronom.equals("je")) {
                    subject = "tu";
                } else if (pronom.equals("tu")) {
                    subject = "je";
                }
                obj = factory.createNounPhrase("le", object);
                obj.setPlural(true);
            } else {
                subject = "il";
            }
        }

        SPhraseSpec ret = factory.createClause();
        ret.setSubject(subject);
        ret.setVerb(verb);
        ret.setObject(obj);
        ret.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);

        return realiser.realiseSentence(ret);
    }

}
