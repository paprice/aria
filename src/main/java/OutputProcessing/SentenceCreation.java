/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OutputProcessing;

import ConversationHandler.Preference;
import InputProcessing.Sentence;
import TypeWord.Word;
import TypeWord.WordNoPref;
import java.util.List;
import org.bson.Document;
import simplenlg.features.Feature;
import simplenlg.features.Gender;
import simplenlg.features.InterrogativeType;
import simplenlg.features.LexicalFeature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Person;

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
import windows.WindowsController;

/**
 *
 * @author Patrice Desrochers
 * @author Gildo Conte
 */
public class SentenceCreation {

    public static String typeQuestion;

    private static Lexicon lexicon;
    private static NLGFactory factory;
    private static Realiser realiser;

    public static void InitializeRealiser() {
        lexicon = new XMLLexicon();
        factory = new NLGFactory(lexicon);
        realiser = new Realiser();
    }

    public static String GenerateResponse(List<Word> words, boolean isQuestion, Sentence sent) {
        String output;

        //ICI il faudrait faire des IF qui font la sélection de la bonne réponse à donner.
        //GenerateResponse devrait être notre fonction maîtresse qui pointe vers la bonne fonction pour générer la bonne réponse
        //selon le contenu de la dernière phrase. À Discuter.
        if (!isQuestion) {
            if (!WindowsController.wasLastQuestion) {
                output = GenerateQuestionResponse(words, sent);
                WindowsController.wasLastQuestion = true;
            } else {
                output = "D'accord";
                WindowsController.wasLastQuestion = false;
            }
        } else {
            output = GeneratePreferenceResponse(words);
        }

        return output;

    }

    public static String GeneratePreferenceResponse(List<Word> words) {
        String verb = "";
        String subject = "";
        Word object = new WordNoPref("test", "test");
        String ono = "";
        String det = "";
        String comp = "";
        boolean neg = false;

        for (Word word : words) {
            if (word.getType().equals("nc")) {
                Document pref = Preference.ReturnPref(word.getPreference());
                object = word;
                subject = pref.getString("subject");
                verb = pref.getString("verb");
                ono = pref.getString("ono");
                det = pref.getString("det");
                neg = pref.getBoolean("neg");
                comp = pref.getString("comp");
            }
        }

        NPPhraseSpec obj = factory.createNounPhrase(det, object.getWord());
        if (object.getNumber().equals("p")) {
            obj.setPlural(true);
        }

        NPPhraseSpec sub = factory.createNounPhrase(subject);
        sub.setFeature(Feature.PERSON, Person.FIRST);
        sub.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
        sub.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
        sub.setFeature(Feature.PRONOMINAL, true);

        VPPhraseSpec ve = factory.createVerbPhrase(verb);
        ve.setFeature(Feature.NEGATED, neg);
        ve.addComplement(comp);

        SPhraseSpec ret = factory.createClause();
        ret.addFrontModifier(ono);
        ret.setSubject(sub);
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

    private static String GenerateQuestionResponse(List<Word> words, Sentence sent) {
        String verb = "";
        String subject = "";
        String obj = "";
        String adj = "";

        switch (sent.getSubject().get(0).toLowerCase()) {
            case "je":
                subject = "tu";
                break;
            case "tu":
                subject = "je";
                break;
            default:
                for (int i = 0; i < sent.getSubject().size(); i++) {
                    subject += sent.getSubject().get(i) + " ";
                }
                break;
        }

        for (int i = 0; i < sent.getVerb().size(); i++) {
            if (i == 0) {
                verb = sent.getVerb().get(i) + " ";
            } else {
                obj += sent.getVerb().get(i) + " ";
            }
        }

        for (int i = 0; i < sent.getObject().size(); i++) {
            obj += sent.getObject().get(i) + " ";
        }

        SPhraseSpec ret = factory.createClause();
        ret.setSubject(subject);
        ret.setVerb(verb);
        ret.setObject(obj);

        int ran = (int) (Math.random() * (6 - 1));

        switch (ran) {
            case 6:
                ret.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW);
                break;
            case 5:
                ret.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHERE);
                break;
            default:
                ret.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
                break;
        }

        return realiser.realiseSentence(ret);
    }

}
