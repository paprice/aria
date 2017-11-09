/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputProcessing;

import java.util.List;

/**
 *
 * @author despa
 */
class Sentence {
    List<String> subject;
    List<String> verb;
    List<String> object;
    
    public Sentence(){
        
    }

    public List<String> getSubject() {
        return subject;
    }

    public void addSubject(String subject) {
        this.subject.add(subject);
    }

    public List<String> getVerb() {
        return verb;
    }

    public void addVerb(String verb) {
        this.verb.add(verb);
    }

    public List<String> getObject() {
        return object;
    }

    public void addObject(String object) {
        this.object.add(object);
    }
    
    
    
}
