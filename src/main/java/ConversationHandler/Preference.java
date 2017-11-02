/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConversationHandler;

import org.bson.Document;

/**
 *
 * @author Gildo Conte
 * @author Audrey Eugène
 */
public enum Preference {
    HATE("Beurk!", "je", "détester", "le", "", false, -20),
    DISLIKE("Ouf...", "je", "aimer", "le", "", true, -10),
    NEUTRAL("Hmmm.", " je", "être", "le", "sur", false, 0),
    LIKE("Oui!", "je", "aimer", "le", "", false, 10),
    LOVE("Ooooh!", "je", "adorer", "le", "", false, 20);

    private final String ono;
    private final String subject;
    private final String verb;
    private final String det;
    private final String complement;
    private final boolean isNegative;
    private final int scorePref;

    Preference(String o, String s, String v, String det, String complement, boolean neg, int score) {
        this.ono = o;
        this.subject = s;
        this.verb = v;
        this.isNegative = neg;
        this.scorePref = score;
        this.det = det;
        this.complement = complement;
    }

    private Document getAll() {
        Document doc = new Document("ono", this.ono).append("subject", this.subject).append("verb", this.verb).append("det", this.det).append("comp", this.complement).append("neg", this.isNegative);

        return doc;
    }

    public static Document ReturnPref(int score) {
        if (score >= LOVE.scorePref) {
            return LOVE.getAll();
        } else if (score >= LIKE.scorePref && score < LOVE.scorePref) {
            return LIKE.getAll();
        } else if (score < LIKE.scorePref && score > DISLIKE.scorePref) {
            return NEUTRAL.getAll();
        } else if (score <= DISLIKE.scorePref && score > HATE.scorePref) {
            return DISLIKE.getAll();
        } else {
            return HATE.getAll();
        }
    }

}
