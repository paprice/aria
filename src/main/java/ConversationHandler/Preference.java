/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConversationHandler;

import org.bson.Document;

/**
 *
 * @author Gildo
 */
public enum Preference {
    HATE("Beurk!", "je", "détester", false, -20),
    DISLIKE("Ouf...", "je", "aimer", true, -10),
    NEUTRAL("Hmmm.", " je", "savoir", true, 0),
    LIKE("Oui!", "je", "aimer", false, 10),
    LOVE("Ooooh!", "je", "adorer", false, 20);

    public String ono;
    public String subject;
    public String verb;
    public boolean isNegative;
    public int scorePref;

    Preference(String o, String s, String v, boolean neg, int score) {
        this.ono = o;
        this.subject = s;
        this.verb = v;
        this.isNegative = neg;
        this.scorePref = score;
    }

    private Document getAll() {
        Document doc = new Document("ono", this.ono).append("subject", this.subject).append("verb", this.verb).append("neg", this.isNegative);
  
        return doc;
    }

    public static Document ReturnPref(int score) {
        if (score >= 20) {
            return LOVE.getAll();
        } else if (score >= 10 && score < 20) {
            return LIKE.getAll();
        } else if (score < 10 && score > -10) {
            return NEUTRAL.getAll();
        } else if (score <= -10 && score > -20) {
            return DISLIKE.getAll();
        } else {
            return HATE.getAll();
        }
    }

}
