/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputProcessing;

import static OutputProcessing.SentenceCreation.InitializeRealiser;
import TypeWord.WordNoPref;
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

        // séparation de la phrase
        String tokens[] = tokenizer.tokenize(sentence);
        // Tagging des mots
        String[] tags = tagger.tag(tokens);
        // Remise de tous les mots dans une string
        POSSample sample = new POSSample(tokens, tags);

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
                        sentence.addVerb(new WordNoPref(tags[i], words[i]));
                    } else if (isAddingVerb) {
                        if (!tags[i].contains("V")) {
                            isAddingVerb = false;
                        }
                        sentence.addObject(new WordNoPref(tags[i], words[i]));
                    } else {
                        sentence.addObject(new WordNoPref(tags[i], words[i]));
                    }
                } else {
                    if (isAddingSubject) {
                        sentence.addSubject(new WordNoPref(tags[i], words[i]));
                        start = true;
                    } else if (isAddingVerb) {
                        sentence.addVerb(new WordNoPref(tags[i], words[i]));
                    } else {
                        sentence.addObject(new WordNoPref(tags[i], words[i]));
                    }
                }
            } else {
                if (isAddingSubject) {
                    if (!tags[i].contains("V")) {
                        sentence.addSubject(new WordNoPref(tags[i], words[i]));
                    } else {
                        sentence.addVerb(new WordNoPref(tags[i], words[i]));
                        isAddingSubject = false;
                        isAddingVerb = true;
                    }
                } else if (isAddingVerb) {
                    if (tags[i].contains("V")) {
                        sentence.addVerb(new WordNoPref(tags[i], words[i]));
                    } else {
                        sentence.addObject(new WordNoPref(tags[i], words[i]));
                    }
                } else {
                    sentence.addObject(new WordNoPref(tags[i], words[i]));
                }
            }
        }

        return sentence;
    }

}
