/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Language;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.*;

import opennlp.tools.util.PlainTextByLineStream;

/**
 *
 * @author despa
 */
public class LanguageProcessing {

    /**
     * 
     * @param sentence the sentence send by the user
     * @return the sentence with all the type of the word
     * @throws IOException 
     * 
     */
    public static String Parse(String sentence) throws IOException {

        /*//////////////////////////////////////////////////////////////*
        Syntaxe de retour de la fonction :
        mot_typeDeMotGenreNombre  -> pommes_NCmp (Nom Commun Masculin Pluriels
        
        NC -> nom commun
        D -> Déterminant
        CL_ -> Pronom où le _ est remplacer par la personne (1,2 ou 3)
        VP_/VK_/VS_ -> Verbeoù le _ est remplacer par la personne (1,2 ou 3)
        A -> Adjectif
        
        P -> Préposition
        ADV -> Adverbe
        CC -> Conjonction
        
        ////////////////////////////////////////////////////////////*/
        
        
        POSModel model = new POSModelLoader()
                .load(new File("fr-pos-ftb-morpho.bin"));
        PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
        POSTaggerME tagger = new POSTaggerME(model);

        ObjectStream<String> lineStream = new PlainTextByLineStream(new StringReader(sentence));

        perfMon.start();
        String line;
        String ret = new String();
        while ((line = lineStream.read()) != null) {
            String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
                    .tokenize(sentence);
            String[] tags = tagger.tag(whitespaceTokenizerLine);

            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            //System.out.println(sample.toString());
            ret = sample.toString();
            perfMon.incrementCounter();
        }

        return ret;
    }

}
