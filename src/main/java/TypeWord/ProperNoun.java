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
public class ProperNoun implements Word {

    private String type;
    private String word;
    private int preference;

    public ProperNoun(String t, String w, int p) {
        type = t;
        word = w;
        preference = p;
    }

    @Override
    public Document CreateDoc() {
        return new Document("type", type).append("word", word).append("preference", preference);
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
        return preference;
    }

    @Override
    public String getGenre() {
        return null;
    }

    @Override
    public String getNumber() {
        return null;
    }

    @Override
    public String getDet() {
        return null;
    }

}
