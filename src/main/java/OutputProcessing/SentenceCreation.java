/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OutputProcessing;

import DataBase.User;
import DataBase.MongoDB;
import ConversationHandler.Preference;
import InputProcessing.Sentence;
import TypeWord.Word;
import TypeWord.WordNoPref;
import com.mongodb.Cursor;
import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;
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
        String output = "";
        User user = User.Instance();
        //boolean aimer =;

        //ICI il faudrait faire des IF qui font la sélection de la bonne réponse à donner.
        //GenerateResponse devrait être notre fonction maîtresse qui pointe vers la bonne fonction pour générer la bonne réponse
        //selon le contenu de la dernière phrase. À Discuter.
        if (!isQuestion) {
            if (!WindowsController.wasLastQuestion) {
                output = GenerateQuestionResponse(words, sent);
                WindowsController.wasLastQuestion = true;
            } else if (WindowsController.wasLastQuestion) {
                //output = "D'accord.";
                boolean find = false;
                String newSub = "";
                MongoDB mongo = MongoDB.Instance();

                for (Word w : sent.getSubject()) {

                    if (w.getType().contains("NC")) {
                        String desc = mongo.GetSingleDefinition(w.getWord(), "nc");

                        if (desc != null) {
                            List<Document> docs = mongo.GetSameDesc(desc);

                            int i = 0;
                            while (i < docs.size() && !find) {
                                if (!docs.get(i).getString("word").equals(w.getWord())) {
                                    find = true;
                                    newSub = docs.get(i).getString("word");
                                }
                                i++;
                            }
                        }
                    }

                    /*
                    if (w.getType().equals("nc")){
                        Document d = mongo.GetSingleDefinition(w.getWord(), "nc");
                        desc = d.getString("desc");
                        
                        FindIterable<Document> isFind = nameCommun.find(eq("desc", desc));
                        for (Document d : isFind) {
                            if(d.getString("desc").equals(desc) && !d.getString("word").equals(w.getWord())){
                                find = true;
                                newSub = d.getString("word");
                                break;
                            }
                        }
                    }*/
                }

                if (find) {
                    output = output.concat("Aimes-tu " + newSub + " ?");
                } else {
                    output = output.concat("Y a-t-il d'autres sujets que nous n'avons pas encore abordé qui te passionnent?");
                }

                WindowsController.wasLastQuestion = false;
            } else {
                output = GenerateComparativeResponse(words, user);
            }
        } else {
            output = GeneratePreferenceResponse(words);
        }
        /*
        if (sent.equals("Ça va?")) {
            output = "Je vais bien merci.";
        } else if (sent.equals("Que fais-tu?")) {
            output = "Je ne fais rien de particulier à part discuter avec toi.";
        }
         */

        return output;

    }

    public static String GenerateComparativeResponse(List<Word> words, User user) {
        String output = "";
        int prefU;
        int prefIA;

        for (Word word : words) {
            if (word.getType().equals("nc") || (word.getType().equals("verb") && !word.getWord().equals("aimer"))) {
                prefU = user.getCommonPreference(word.getWord());
                Document pref = Preference.ReturnPref(word.getPreference());
                prefIA = pref.getInteger("scorePref");

                if (prefIA > 0 && prefU > 0) {
                    output = "J'aime " + word.getWord() + " également!";
                } else if (prefIA <= 0 && prefU <= 0) {
                    output = "Je n'aime pas spécialement " + word.getWord() + " non plus!";
                } else {
                    output = "Sur ce coup, je dois dire que mon opinion est à l'inverse de la tienne.";
                }
            }
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

        if (sent.getSubject().size() > 0) {
            switch (sent.getSubject().get(0).getWord().toLowerCase()) {
                case "je":
                    subject = "tu";
                    break;
                case "tu":
                    subject = "je";
                    break;
                default:
                    for (int i = 0; i < sent.getSubject().size(); i++) {
                        subject += sent.getSubject().get(i).getWord() + " ";
                    }
                    break;
            }
        }

        for (int i = 0; i < sent.getVerb().size(); i++) {
            if (i == 0) {
                verb = sent.getVerb().get(i).getWord() + " ";
            } else {
                obj += sent.getVerb().get(i).getWord() + " ";
            }
        }

        for (int i = 0; i < sent.getObject().size(); i++) {
            obj += sent.getObject().get(i).getWord() + " ";
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
