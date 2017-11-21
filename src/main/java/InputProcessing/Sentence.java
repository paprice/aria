/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputProcessing;

import TypeWord.Word;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author despa
 */
public class Sentence {

    List<Word> subject;
    List<Word> verb;
    List<Word> object;

    public Sentence() {
        subject = new ArrayList<>();
        verb = new ArrayList<>();
        object = new ArrayList<>();
    }

    public List<Word> getSubject() {
        return subject;
    }

    public void addSubject(Word subject) {
        this.subject.add(subject);
    }

    public List<Word> getVerb() {
        return verb;
    }

    public void addVerb(Word verb) {
        this.verb.add(verb);
    }

    public List<Word> getObject() {
        return object;
    }

    public void addObject(Word object) {
        if (!object.getWord().contains(".")) {
            this.object.add(object);
        }

    }

}
