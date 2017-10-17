/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Language;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
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
        DET -> Déterminant
        CLS -> Pronom
        V/VINF -> Verbe
        ADJ -> Adjectif
        P -> Préposition
        ADV -> Adverbe
        CC -> Conjonction
        syntaxe complète des models : http://www.llf.cnrs.fr/Gens/Abeille/French-Treebank-fr.php
        ////////////////////////////////////////////////////////////*/
        POSModel model = new POSModelLoader()
                .load(new File("models/fr-pos.bin"));
        PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
        POSTaggerME tagger = new POSTaggerME(model);
        perfMon.start();
        String ret = new String();
        InputStream modelIn = new FileInputStream("models/fr-token.bin");
        TokenizerModel TokModel;
        TokModel = new TokenizerModel(modelIn);
        TokenizerME tokenizer = new TokenizerME(TokModel);
        String tokens[] = tokenizer.tokenize(sentence);
        String[] tags = tagger.tag(tokens);
        POSSample sample = new POSSample(tokens, tags);
        //System.out.println(sample.toString());
        ret = sample.toString();
        perfMon.incrementCounter();

        return ret;
    }

}
