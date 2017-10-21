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
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

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
    public static POSSample Parse(String sentence) throws IOException {

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
        PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
        perfMon.start();
        // modèle de classes de mots
        
        POSModel model = new POSModelLoader()
                .load(new File("models/fr-pos.bin"));
        POSTaggerME tagger = new POSTaggerME(model);
        // Séparation des mots
        
        InputStream modelIn = new FileInputStream("models/fr-token.bin");
        TokenizerModel TokModel;
        TokModel = new TokenizerModel(modelIn);
        TokenizerME tokenizer = new TokenizerME(TokModel);
        // séparation de la phrase
        String tokens[] = tokenizer.tokenize(sentence);
        // Tagging des mots
        String[] tags = tagger.tag(tokens);
        // Remise de tous les mots dans une string
        POSSample sample = new POSSample(tokens, tags);
 
        perfMon.incrementCounter();

        return sample;
    }
    
    public static String PreParse(String sentence) throws IOException {
        String preParsed;
        for (int i = 0; i < sentence.length(); i++)
        {
            if (i != sentence.length() - 1)
            {
                if (sentence.charAt(i) != ' ' && (sentence.charAt(i + 1) == '!' || sentence.charAt(i + 1) == '?'))
                {
                    preParsed = sentence.substring(0, i + 1) + ' ' + sentence.substring(i + 1, sentence.length());
                    sentence = PreParse(preParsed);
                }
            }
        }
        return sentence;
    }

}
