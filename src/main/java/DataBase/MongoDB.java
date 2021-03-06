/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import TypeWord.*;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author Patrice Desrochers
 * @author Gildo Conte
 */
public class MongoDB {

    private static MongoDB database;
    MongoClient client;
    MongoDatabase data;
    MongoCollection nameCommun;
    MongoCollection properName;
    MongoCollection adj;
    MongoCollection verb;
    MongoCollection adv;

    private MongoDB() {
        MongoClientURI uri = new MongoClientURI("mongodb://user:user@ds161471.mlab.com:61471/dictionnaire");
        client = new MongoClient(uri);
        data = client.getDatabase("dictionnaire");
        nameCommun = data.getCollection("names");
        properName = data.getCollection("Pname");
        adj = data.getCollection("adj");
        verb = data.getCollection("verb");
        adv = data.getCollection("adv");
    }

    public static MongoDB Instance() {
        if (database == null) {
            return new MongoDB();
        }
        return database;
    }

    private void insertDocuments(MongoCollection collect, List<Document> doc) {
        collect.insertMany(doc);
    }

    private void insertDocument(MongoCollection collect, Document doc) {
        collect.insertOne(doc);
    }

    private void updateQuery(Document toUpdate, Document update, MongoCollection collect) {
        collect.updateOne(toUpdate, new Document("$set", update));
    }

    private void updateQueryInc(Document toUpdate, Document update, MongoCollection collect) {
        collect.updateOne(toUpdate, new Document("$inc", update));
    }

    private void removeDocument(MongoCollection collect, Document doc) {
        collect.deleteOne(doc);
    }

    /**
     *
     * @param words the list of document to insert
     * @return
     */
    public List<Word> InsertOrUpdate(List<Word> words) {

        List<Word> retDoc = new ArrayList<>();

        for (Word word : words) {
            if (word.getType().equals("nc")) {
                Document isFind = (Document) nameCommun.find(eq("word", word.getWord())).first();
                if (isFind != null) {
                    Document upd = new Document("preference", word.getPreference());
                    updateQueryInc(isFind, upd, nameCommun);
                    Document Find = (Document) nameCommun.find(eq("word", word.getWord())).first();
                    if (Find != null) {
                        retDoc.add(new Noun(word.getType(), word.getWord(), Find.getInteger("preference"), word.getDet(),word.getKind(),word.getNumber()));
                    }

                } else {
                    this.insertDocument(nameCommun, word.CreateDoc());
                    retDoc.add(word);
                }
            } else if (word.getType().equals("v")) {
                Document isFind = (Document) verb.find(eq("word", word.getWord())).first();
                if (isFind != null) {
                    Document upd = new Document("preference", word.getPreference());
                    updateQueryInc(isFind, upd, verb);
                    Document Find = (Document) verb.find(eq("word", word.getWord())).first();
                    if (Find != null) {
                        retDoc.add(new Verb(word.getType(), word.getWord(), Find.getInteger("preference")));
                    }
                } else {
                    this.insertDocument(verb, word.CreateDoc());
                    retDoc.add(word);
                }
            } else if (word.getType().equals("adj")) {
                Document isFind = (Document) adj.find(eq("word", word.getWord())).first();
                if (isFind != null) {
                    retDoc.add(word);
                } else {
                    this.insertDocument(adj, word.CreateDoc());
                    retDoc.add(word);
                }
            } else if (word.getType().equals("adv")) {
                Document isFind = (Document) adv.find(eq("word", word.getWord())).first();
                if (isFind != null) {

                } else {
                    this.insertDocument(adv, word.CreateDoc());
                    retDoc.add(word);
                }
            } else if (word.getType().equals("npp")) {
                Document isFind = (Document) properName.find(eq("word", word.getWord())).first();
                if (isFind != null) {
                    Document upd = new Document("preference", word.getPreference());
                    updateQueryInc(isFind, upd, properName);
                    Document Find = (Document) properName.find(eq("word", word.getWord())).first();
                    if (Find != null) {
                        retDoc.add(new ProperNoun(word.getType(), word.getWord(), Find.getInteger("preference")));
                    }
                } else {
                    this.insertDocument(properName, word.CreateDoc());
                    retDoc.add(word);
                }
            } else if (word.getType().equals("cls")) {
                retDoc.add(word);
            }

        }

        return retDoc;

    }

    public int HaveDefinition(String word, String typeWord) {
        if (typeWord.equals("nc")) {
            Document isFind = (Document) nameCommun.find(eq("word", word)).first();
            if (isFind != null) {
                String type = (String) isFind.getString("desc");
                if (type == null) {
                    return 1;
                }
            }
        }
        return 0;
    }

    public List<Document> FindDesc(String word) {
        List<Document> allDef = new ArrayList<>();

        Document def = (Document) nameCommun.find(eq("word", word)).first();

        FindIterable<Document> isFind = nameCommun.find(eq("desc", def.getString("desc")));
        for (Document d : isFind) {
            allDef.add(d);
        }
        return allDef;
    }

    public void UpdateType(Document toUpdate, Document upd, String collectionName) {
        if (collectionName.equals("names")) {
            updateQuery(toUpdate, upd, nameCommun);
        } else if (collectionName.equals("verb")) {
            updateQuery(toUpdate, upd, verb);
        } else if (collectionName.equals("adj")) {
            updateQuery(toUpdate, upd, adj);
        }
    }

    public String GetSingleDefinition(String word, String typeWord) {
        Document d;
        switch (typeWord) {
            case "nc":
                d = (Document) nameCommun.find(eq("word", word)).first();
                break;
            case "adj":
                d = (Document) adj.find(eq("word", word)).first();
                break;
            default:
                return null;
        }
        return d.getString("desc");
    }

    public int GetPreference(String word, String typeWord) {
        if (typeWord.equals("nc")) {
            Document isFind = (Document) nameCommun.find(eq("word", word)).first();
            if (isFind != null) {
                return isFind.getInteger("preference");
            }
        } else if (typeWord.equals("v")) {
            Document isFind = (Document) verb.find(eq("word", word)).first();
            if (isFind != null) {
                return isFind.getInteger("preference");
            }
        }
        return 0;
    }

    public Document GetDocumentDefinition(String word, String typeWord) {
        switch (typeWord) {
            case "nc":
                return (Document) nameCommun.find(eq("word", word)).first();
            case "v":
                return (Document) verb.find(eq("word", word)).first();
            case "adj":
                return (Document) adj.find(eq("word", word)).first();

            default:
                return null;
        }
    }

    public List<Document> GetSameDesc(String desc) {

        List<Document> allSame = new ArrayList<>();

        MongoCursor<Document> cursor = nameCommun.find(eq("desc", desc)).iterator();

        while (cursor.hasNext()) {
            allSame.add((Document) cursor.next());
        }

        return allSame;
    }
}
