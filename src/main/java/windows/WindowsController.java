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

/**
 *
 * @author despa
 */
public class WindowsController {

    MongoClient client;

    public static String AiDecortication(String userInput) throws IOException {

        userInput = PreParse(userInput);
        POSSample parsed = Parse(userInput);
        List<Document> important = ExtractImportant(parsed);
        InsertIfNotIn(important, "names");

        return parsed.toString();
    }

}
