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
import static main.main.db;
import org.bson.Document;

/**
 *
 * @author Patrice Desrochers
 */
public class MongoDB {

    MongoClient client;
    MongoClientURI uri;

    public MongoDB() {
        uri = new MongoClientURI("mongodb://user:user@ds161471.mlab.com:61471/dictionnaire");
        client = new MongoClient(uri);
    }

    public MongoDatabase GetDatabase() {
        return client.getDatabase("dictionnaire");
    }

    public MongoCollection getCollection(String name, MongoDatabase db) {
        return db.getCollection(name);
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

    public void removeDocument(MongoCollection collect, Document doc) {
        collect.deleteOne(doc);
    }

    /**
     *
     * @param doc the list of document to insert
     * @param collectionName the name of the collections to insert in
     */
    public static void InsertIfNotIn(List<Document> doc, String collectionName) {

        MongoDatabase data = db.GetDatabase();
        MongoCollection collect = db.getCollection(collectionName, data);

        for (Document d : doc) {
            Document isFind = (Document) collect.find(eq("word", d.get("word"))).first();
            if (isFind != null) {

            } else {
                db.insertDocument(collect, d);
            }
        }
    }

}
