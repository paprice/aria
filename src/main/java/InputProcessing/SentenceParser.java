/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputProcessing;

import static OutputProcessing.SentenceCreation.InitializeRealiser;
import java.io.*;
import opennlp.tools.chunker.*;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.*;
import opennlp.tools.tokenize.*;

/**
 *
 * @author Patrice Desrochers
 * @author Gildo Conte
 */
public class SentenceParser {

    private static TokenizerME tokenizer;
    private static POSTaggerME tagger;
    private static ChunkerME chunker;

    public static void LoadModel() throws FileNotFoundException, IOException {
        POSModel model = new POSModelLoader()
                .load(new File("models/fr-pos-ftb-morpho.bin"));
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
        // modèle de classes de mots
        // séparation de la phrase
        String tokens[] = tokenizer.tokenize(sentence);
        // Tagging des mots
        String[] tags = tagger.tag(tokens);
        // Remise de tous les mots dans une string
        POSSample sample = new POSSample(tokens, tags);

        /*String tag[] = chunker.chunk(tokens,tags);

        for(int i = 0; i < tags.length;i++){
            System.out.println(tokens[i] + " " + tags[i] + " " + tag[i] + " ");
        }*/
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

    private static String[] Chunker(POSSample pos) {

        return chunker.chunk(pos.getSentence(), pos.getTags());

    }

    public static Sentence PartitionnateSentence(POSSample pos) {
        Sentence sentence = new Sentence();
        String chunks[] = Chunker(pos);

        String words[] = pos.getSentence();
        String tags[] = pos.getTags();

        boolean isAddingSubject = true;
        boolean isAddingVerb = false;

        boolean start = false;
        for (int i = 0; i < words.length; i++) {

            if (chunks[i].substring(0, 1).equals("B")) {
                if (start) {
                    if (isAddingSubject) {
                        isAddingSubject = false;
                        isAddingVerb = true;
                        sentence.addVerb(words[i]);
                    } else if (isAddingVerb) {
                        if (!tags[i].contains("V")) {
                            isAddingVerb = false;
                        }
                        sentence.addObject(words[i]);
                    } else {
                        sentence.addObject(words[i]);
                    }
                } else {
                    if (isAddingSubject) {
                        sentence.addSubject(words[i]);
                        start = true;
                    } else if (isAddingVerb) {
                        sentence.addVerb(words[i]);
                    } else {
                        sentence.addObject(words[i]);
                    }
                }
            } else {
                if (isAddingSubject) {
                    if (!tags[i].contains("V")) {
                        sentence.addSubject(words[i]);
                    } else {
                        sentence.addVerb(words[i]);
                        isAddingSubject = false;
                        isAddingVerb = true;
                    }
                } else if (isAddingVerb) {
                    sentence.addVerb(words[i]);
                } else {
                    sentence.addObject(words[i]);
                }
            }
        }

        return sentence;
    }

}
