/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windows;

import static Language.LanguageProcessing.Parse;
import static Language.LanguageProcessing.PreParse;
import static Language.Words.ExtractImportant;
import static DataBase.MongoDB.InsertIfNotIn;
import com.mongodb.MongoClient;
import java.io.IOException;
import java.util.List;
import opennlp.tools.postag.POSSample;
import org.bson.Document;

import simplenlg.framework.*;
import simplenlg.realiser.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import simplenlg.lexicon.Lexicon;

/**
 *
 * @author Patrice Desrochers
 */
public class WindowsController {

    MongoClient client;

    public static String AiDecortication(String userInput) throws IOException {

        userInput = PreParse(userInput);
        POSSample parsed = Parse(userInput);
        List<Document> important = ExtractImportant(parsed);
        InsertIfNotIn(important, "names");

        Lexicon lexicon = new simplenlg.lexicon.french.XMLLexicon();
        NLGFactory factory = new NLGFactory(lexicon);
        Realiser realiser = new Realiser();

        NPPhraseSpec theMan = factory.createNounPhrase("le", "homme");
        NPPhraseSpec theCrowd = factory.createNounPhrase("le", "foule");

        SPhraseSpec greeting
                = factory.createClause(theMan, "saluer", theCrowd);

        SPhraseSpec p = factory.createClause();
        for (Document d : important) {
            p.setSubject(d.get("word"));
            p.setVerb("mange");
            p.setObject("une pomme");

        }

        String output = realiser.realiseSentence(greeting) + " " + realiser.realiseSentence(p);

        return output;
    }

}
