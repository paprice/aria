/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author Patrice Desrochers
 */
public class MongoDB {

    MongoClient client;
    MongoDatabase data;
    MongoCollection nameCommun;
    MongoCollection properName;
    MongoCollection adj;
    MongoCollection verb;
    MongoCollection adv;

    public MongoDB() {
        MongoClientURI uri = new MongoClientURI("mongodb://user:user@ds161471.mlab.com:61471/dictionnaire");
        client = new MongoClient(uri);
        data = client.getDatabase("dictionnaire");
        nameCommun = data.getCollection("names");
        properName = data.getCollection("Pname");
        adj = data.getCollection("adj");
        verb = data.getCollection("verb");
        adv = data.getCollection("adv");
    }

    public void CloseConnection() {
        client.close();
    }

    public void insertDocuments(MongoCollection collect, List<Document> doc) {
        collect.insertMany(doc);
    }

    public void insertDocument(MongoCollection collect, Document doc) {
        collect.insertOne(doc);
    }

    public void updateQuery(Document toUpdate, Document update, MongoCollection collect) {
        collect.updateOne(toUpdate, new Document("$set", update));
    }

    public void updateQueryInc(Document toUpdate, Document update, MongoCollection collect) {
        collect.updateOne(toUpdate, new Document("$inc", update));
    }

    public void removeDocument(MongoCollection collect, Document doc) {
        collect.deleteOne(doc);
    }

    /**
     *
     * @param doc the list of document to insert
     */
    public void InsertOrUpdate(List<Document> doc) {

        for (Document d : doc) {
            if (d.get("type").equals("nc")) {
                Document isFind = (Document) nameCommun.find(eq("word", d.get("word"))).first();
                if (isFind != null) {
                    Document upd = new Document("count", 1);
                    updateQueryInc(d, upd, nameCommun);
                } else {
                    this.insertDocument(nameCommun, d);
                }
            } else if (d.get("type").equals("v")) {
                Document isFind = (Document) verb.find(eq("word", d.get("word"))).first();
                if (isFind != null) {

                } else {
                    this.insertDocument(verb, d);
                }
            } else if (d.get("type").equals("adj")) {
                Document isFind = (Document) adj.find(eq("word", d.get("word"))).first();
                if (isFind != null) {

                } else {
                    this.insertDocument(adj, d);
                }
            } else if (d.get("type").equals("adv")) {
                Document isFind = (Document) adv.find(eq("word", d.get("word"))).first();
                if (isFind != null) {

                } else {
                    this.insertDocument(adv, d);
                }
            } else if (d.get("type").equals("npp")) {
                Document isFind = (Document) properName.find(eq("word", d.get("word"))).first();
                if (isFind != null) {

                } else {
                    this.insertDocument(properName, d);
                }
            }

        }

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

    public void UpdateType(Document toUpdate, Document upd, String collectionName) {
        updateQuery(toUpdate, upd, nameCommun);
    }

}
