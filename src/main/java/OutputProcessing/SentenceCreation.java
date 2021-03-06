/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OutputProcessing;

import ConversationHandler.Context;
import DataBase.User;
import DataBase.MongoDB;
import ConversationHandler.Preference;
import ConversationHandler.CurrentConversation;
import InputProcessing.Sentence;
import TypeWord.*;
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

    public static String GenerateResponse(List<Word> words/*, Sentence sent*/) {
        Sentence sent = CurrentConversation.getLastSentence();
        String output = "";
        User user = User.Instance();
        Context context = CurrentConversation.getContext();
        
        //Si Affirmation -> Question ou Comparaison des goûts
        if (context == Context.AFFIRMATION) {
            boolean aime = false;
            for (Word v : sent.getVerb()) {
                if (v.getWord().contains("aime") || v.getWord().contains("adore")) {
                    aime = true;
                }
            }
            if (aime) {
                output = GenerateComparativeResponse(words, user);
            }
            output = output.concat(GenerateQuestionResponse(sent));

        //Si Question -> Réponse
        } else if (context == Context.QUESTION) {
            output = GeneratePreferenceResponse(words);

        //Si Relance -> Relancement ou Question
        } else if (context == Context.RELANCE) {
            String sujet;

            // Le relancer sur son sujet préféré
            sujet = user.getFavoriteSubject();
            if (!CurrentConversation.getCurrentSubjects().contains(sujet)) {
                return GenerateConversationUserPref(sujet);
            }
            
            // Le relancer sur son sujet détesté
            sujet = user.getDespisedSubject();
            if (!CurrentConversation.getCurrentSubjects().contains(sujet)) {
                return GenerateConversationUserDesp(sujet);
            }
                
            // Le relancer sur le sujet aimé d'ARIA
            MongoDB mongo = MongoDB.Instance();
            for (Word w : sent.getSubject()) {
                if (w.getType().contains("NC")) {
                    String desc = mongo.GetSingleDefinition(w.getWord(), "nc");
                    if (desc != null) {
                        List<Document> docs = mongo.GetSameDesc(desc);
                        int i = 0;
                        while (i < docs.size()) {
                            sujet = docs.get(i).getString("word");
                            if (mongo.GetPreference(sujet, "nc") >= 20 &&
                                        (!CurrentConversation.getCurrentSubjects().contains(sujet))) {
                                String verb = "penser";
                                String subject = "tu";
                                String obj = sujet;

                                NPPhraseSpec sub = factory.createNounPhrase("le", subject);
                                sub.setFeature(Feature.PERSON, Person.SECOND);
                                sub.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
                                sub.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
                                sub.setFeature(Feature.PRONOMINAL, true);
                
                                VPPhraseSpec ve = factory.createVerbPhrase(verb);

                                SPhraseSpec ret = factory.createClause();
                                ret.setSubject(sub);
                                ret.setVerb(ve);
                                ret.setObject(obj);

                                ret.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
                                output = realiser.realiseSentence(ret);
                                return output;
                            }
                        }
                    }
                }
            }
            //Sujet "aléatoire" ou question
            output = GenerateRandomSubject(sent, user);
            
        //Si Réponse -> Relancement ou Question
        } else {
            output = GenerateRandomSubject(sent, user);
        }
        return output;
    }

    public static String GenerateComparativeResponse(List<Word> words, User user) {
        String output = "";
        int prefU;
        int prefIA;

        for (Word word : words) {
            if (word.getType().equals("nc") || (word.getType().equals("verb") && !word.getWord().equals("aimer"))) {
                prefU = user.getCommonPreference(word.getWord());
                prefIA = word.getPreference();

                if (prefIA > 0 && prefU > 0) {
                    String verb = "aimer";
                    String subject = "je";

                    NPPhraseSpec obj = factory.createNounPhrase(word.getDet(), word.getWord());
                    if (word.getNumber().equals("s")) {
                        obj.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
                    } else {
                        obj.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
                    }
                    if (word.getKind().equals("f")) {
                        obj.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
                    } else {
                        obj.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
                    }

                    obj.addPostModifier("également");

                    VPPhraseSpec ve = factory.createVerbPhrase(verb);

                    SPhraseSpec ret = factory.createClause();
                    ret.setSubject(subject);
                    ret.setVerb(ve);
                    ret.setObject(obj);
                    //output = "J'aime " + word.getDet() + " " + word.getWord() + " également!";

                    output = realiser.realiseSentence(ret);

                } else if (prefIA <= 0 && prefU <= 0) {
                    String verb = "aimer";
                    String subject = "je";

                    NPPhraseSpec obj = factory.createNounPhrase(word.getDet(), word.getWord());
                    if (word.getNumber().equals("s")) {
                        obj.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
                    } else {
                        obj.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
                    }
                    if (word.getKind().equals("f")) {
                        obj.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
                    } else {
                        obj.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
                    }

                    obj.addPostModifier("non plus");

                    VPPhraseSpec ve = factory.createVerbPhrase(verb);

                    SPhraseSpec ret = factory.createClause();
                    ret.setSubject(subject);
                    ret.setVerb(ve);
                    ret.setObject(obj);
                 

                    output = realiser.realiseSentence(ret);

                    //output = "Je n'aime pas spécialement " + word.getDet() + word.getWord() + " non plus!\n";
                } else {
                    output = "Sur ce coup, je dois dire que mon opinion est à l'inverse de la tienne.\n";
                }
            }
        }
        return output;
    }

    public static String GeneratePreferenceResponse(List<Word> words) {
        String verb = "";
        String subject = "";
        Word object = null;
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
        /*if (object.getNumber().equals("p")) {
            obj.setPlural(true);
        }*/

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

        output = realiser.realiseSentence(ret) + "\n";

        return output;
    }

    private static String GenerateQuestionResponse(Sentence sent) {
        String verb = "";
        String subject = "";
        String obj = "";
        String adj = "";

        CurrentConversation.setContext(Context.REPONSEFORCEE);

        if (sent.getSubject().size() > 0) {
            switch (sent.getSubject().get(0).getWord().toLowerCase()) {
                case "je":
                    subject = "tu";
                    break;
                case "j\\":
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

        int ran = (int) (Math.random() * (3 - 1));

        switch (ran) {
            case 3:
                ret.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW);
                break;
            case 2:
                ret.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHERE);
                break;
            default:
                ret.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
                break;
        }

        return realiser.realiseSentence(ret);
    }

    private static String GenerateConversationUserPref(String sujet) {
        String verb = "aimer";
        String subject = "tu";
        String obj = sujet;

        NPPhraseSpec sub = factory.createNounPhrase("le", subject);
        sub.setFeature(Feature.PERSON, Person.SECOND);
        sub.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
        sub.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
        sub.setFeature(Feature.PRONOMINAL, true);

        VPPhraseSpec ve = factory.createVerbPhrase(verb);

        SPhraseSpec ret = factory.createClause();
        ret.setSubject(sub);
        ret.setVerb(ve);
        ret.setObject(obj);

        ret.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
        return realiser.realiseSentence(ret);
    }
    
    private static String GenerateConversationUserDesp(String sujet) {
        String verb = "détester";
        String subject = "tu";
        String obj = sujet;

        NPPhraseSpec sub = factory.createNounPhrase("le", subject);
        sub.setFeature(Feature.PERSON, Person.SECOND);
        sub.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
        sub.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
        sub.setFeature(Feature.PRONOMINAL, true);

        VPPhraseSpec ve = factory.createVerbPhrase(verb);

        SPhraseSpec ret = factory.createClause();
        ret.setSubject(sub);
        ret.setVerb(ve);
        ret.setObject(obj);

        ret.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
        return realiser.realiseSentence(ret);
    }
    
    private static String GenerateRandomSubject(Sentence sent, User user) {
        boolean find = false;
        String output = "";
        Document newSub = new Document();
        MongoDB mongo = MongoDB.Instance();

        for (Word w : sent.getSubject()) {

            if (w.getType().contains("NC")) {
            String desc = mongo.GetSingleDefinition(w.getWord(), "nc");

                if (desc != null) {
                    List<Document> docs = mongo.GetSameDesc(desc);

                    int i = 0;
                    while (i < docs.size() && !find) {
                        if (!docs.get(i).getString("word").equals(w.getWord())) {
                            if (!user.contains(docs.get(i).getString("word"))) {
                                find = true;
                                newSub = docs.get(i);
                            }
                        }
                        i++;
                    }
                }
            }
        }

        if (find) {
            String verb = "aimer";
            String subject = "tu";
            String obj = newSub.getString("word") + "(" + newSub.getString("desc") + ")";

            NPPhraseSpec sub = factory.createNounPhrase("le", subject);
            sub.setFeature(Feature.PERSON, Person.SECOND);
            sub.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
            sub.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
            sub.setFeature(Feature.PRONOMINAL, true);

            VPPhraseSpec ve = factory.createVerbPhrase(verb);

            SPhraseSpec ret = factory.createClause();
            ret.setSubject(sub);
            ret.setVerb(ve);
            ret.setObject(obj);

            ret.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);

            //NPPhraseSpec o = factory.createNounPhrase("le", newSub.getString("word"));
            output += realiser.realiseSentence(ret);
        } else {
            output += "Y a-t-il d'autres sujets que nous n'avons pas encore abordé qui te passionnent?";
        }
        return output;
    }
}
