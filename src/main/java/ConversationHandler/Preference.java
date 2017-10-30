/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConversationHandler;

/**
 *
 * @author Gildo
 */
public enum Preference {
    HATE("Beurk! Je déteste ", -20),
    DISLIKE("Ouf... Je n'aime pas ", -10),
    NEUTRAL("Hmmm. Je ne sais pas quoi penser sur ", 0),
    LIKE("Oui! Moi j'aime ", 10),
    LOVE("Ooooh! J'ADORE ", 20);

    public String pref;
    public int scorePref;

    public String getPref() {
        return pref;
    }

    public int getScorePref() {
        return scorePref;
    }

    public void setPref(int score) {
        if (score >= 20) {
            this.pref = Preference.LOVE.getPref();
        } else if (score >= 10 && score < 20) {
            this.pref = Preference.LIKE.getPref();
        } else if (score < 10 && score > -10) {
            this.pref = Preference.NEUTRAL.getPref();
        } else if (score <= -10 && score > -20) {
            this.pref = Preference.DISLIKE.getPref();
        } else {
            this.pref = Preference.HATE.getPref();
        }
    }

    public void setScorePref(int score) {
        this.scorePref = score;
    }

    Preference(String pref, int score) {
        this.pref = pref;
        this.scorePref = score;
    }

    public static String ReturnPref(int score) {
        if (score >= 20) {
            return LOVE.getPref();
        } else if (score >= 10 && score < 20) {
            return LIKE.getPref();
        } else if (score < 10 && score > -10) {
            return NEUTRAL.getPref();
        } else if (score <= -10 && score > -20) {
            return DISLIKE.getPref();
        } else {
            return HATE.getPref();
        }
    }

}
