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
import java.util.List;
import org.bson.Document;

/**
 *
 * @author despa
 */
public class MongoDB {

    public MongoClient MongoDB() {

        MongoClientURI uri = new MongoClientURI("mongodb://user:user@ds161471.mlab.com:61471/dictionnaire");
        MongoClient client = new MongoClient(uri);

        return client;
    }

    public MongoDatabase GetDatabase(MongoClient client){
        return client.getDatabase("dictionnaire");
    }
    
    public MongoCollection getCollection (String name, MongoDatabase db){
        return db.getCollection(name);
    }
    
    public void CloseConnection(MongoClient client){
        client.close();
    }
    
    public void insertDocuments(MongoCollection collect, List<Document> doc){
        collect.insertMany(doc);
    }
    
    public void insertDocument(MongoCollection collect, Document doc){
        collect.insertOne(doc);
    }
    
    public void updateQuery(Document toUpdate, Document update, MongoCollection collect){
        collect.updateOne(toUpdate,new Document("$set",update));
    }
    
    public void removeDocument(MongoCollection collect, Document doc){
        collect.deleteOne(doc);
    }
    
}
