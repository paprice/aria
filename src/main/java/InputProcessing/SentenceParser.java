/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputProcessing;

import static OutputProcessing.SentenceCreation.InitializeRealiser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 *
 * @author Patrice Desrochers
 */
public class SentenceParser {

    private static TokenizerME tokenizer;
    private static POSTaggerME tagger;
    private static ChunkerME chunker;

    public static void LoadModel() throws FileNotFoundException, IOException {
        POSModel model = new POSModelLoader()
                .load(new File("models/fr-pos.bin"));
        tagger = new POSTaggerME(model);
        // Séparation des mots
        try (InputStream modelIn = new FileInputStream("models/fr-token.bin")) {
            TokenizerModel TokModel;
            TokModel = new TokenizerModel(modelIn);
            tokenizer = new TokenizerME(TokModel);
        }
        try (InputStream modelInChun = new FileInputStream("models/fr-chunk.bin")) {
            ChunkerModel modelChun = new ChunkerModel(modelInChun);
            chunker = new ChunkerME(modelChun);
        }

        InitializeRealiser();
        
    }

    /**
     *
     * @param sentence the sentence send by the user
     * @return the sentence with all the type of the word
     *
     */
    public static POSSample Parse(String sentence) {

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

        // séparation de la phrase
        String tokens[] = tokenizer.tokenize(sentence);
        // Tagging des mots
        String[] tags = tagger.tag(tokens);
        // Remise de tous les mots dans une string
        POSSample sample = new POSSample(tokens, tags);

        perfMon.incrementCounter();

        return sample;
    }

    public static String PreParse(String sentence) {
        String preParsed;
        for (int i = 0; i < sentence.length(); i++) {
            if (i != sentence.length() - 1) {
                if (sentence.charAt(i) != ' ' && (sentence.charAt(i + 1) == '!' || sentence.charAt(i + 1) == '?' || sentence.charAt(i + 1) == '-')) {
                    preParsed = sentence.substring(0, i + 1) + ' ' + sentence.substring(i + 1, sentence.length());
                    sentence = PreParse(preParsed);
                }
            }
        }
        return sentence;
    }

    public static String[] Chunker(String sentence[], String pos[]) {

        return chunker.chunk(sentence, pos);

    }

}
