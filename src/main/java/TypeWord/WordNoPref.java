/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TypeWord;

import org.bson.Document;

/**
 *
 * @author despa
 */
public class WordNoPref implements Word {

    private String type;
    private String word;

    public WordNoPref(String t, String w) {
        type = t;
        word = w;
    }

    @Override
    public Document CreateDoc() {
        return new Document("type", type).append("word", word);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getWord() {
        return word;
    }

    @Override
    public int getPreference() {
        return 0;
    }

    @Override
    public String getDet() {
        return null;
    }

    @Override
    public String getKind() {
        return null;
    }

    @Override
    public String getNumber() {
        return null;
    }

    @Override
    public WordNoPref clone() {
        WordNoPref n = null;
        try {
            // On récupère l'instance à renvoyer par l'appel de la 
            // méthode super.clone()
            n = (WordNoPref) super.clone();
        } catch (CloneNotSupportedException cnse) {
            // Ne devrait jamais arriver car nous implémentons 
            // l'interface Cloneable
            cnse.printStackTrace(System.err);
        }

        return n;
    }
}
