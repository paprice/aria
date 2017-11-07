/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OutputProcessing;

import ConversationHandler.Preference;
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

/**
 *
 * @author Patrice Desrochers
 * @author Gildo Conte
 */
public class SentenceCreation {

    private static Lexicon lexicon;
    private static NLGFactory factory;
    private static Realiser realiser;
    private static boolean wasLastQuestion = false;

    public static void InitializeRealiser() {
        lexicon = new XMLLexicon();
        factory = new NLGFactory(lexicon);
        realiser = new Realiser();
    }

    public static String GenerateResponse(List<Word> words, boolean isQuestion) {
        String output;

        //ICI il faudrait faire des IF qui font la sélection de la bonne réponse à donner.
        //GenerateResponse devrait être notre fonction maîtresse qui pointe vers la bonne fonction pour générer la bonne réponse
        //selon le contenu de la dernière phrase. À Discuter.
        if (!isQuestion) {
            if (!wasLastQuestion) {
                output = GenerateQuestionResponse(words);
                wasLastQuestion = true;
            } else {
                output = "D'accord";
                wasLastQuestion = false;
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

    private static String GenerateQuestionResponse(List<Word> words) {
        String verb = "";
        String subject = "";
        Word object = new WordNoPref("", "");
        String adj = "";

        for (Word word : words) {
            switch (word.getType()) {
                case "v":
                    verb = word.getWord();
                    break;
                case "nc":
                    object = word;
                    break;
                case "cls":
                    switch (word.getWord()) {
                        case "je":
                            subject = "tu";
                            break;
                        case "tu":
                            subject = "je";
                            break;
                        default:
                            subject = "il";
                            break;
                    }
                    break;
                case "adj":
                    adj = word.getWord();
                default:
                    break;
            }
        }

        NPPhraseSpec obj;

        if (subject.equals("")) {
            subject = "il";
            obj = factory.createNounPhrase(adj);
        } else {

            obj = factory.createNounPhrase("le", object.getWord());
            if (object.getNumber().equals("p")) {
                obj.setPlural(true);
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
