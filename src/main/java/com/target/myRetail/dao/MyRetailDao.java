package com.target.myRetail.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Filters.eq;

@Service
public class MyRetailDao {

    @Autowired
    private MongoClient mongoClient;


    public String getProduct(String productId) {
         MongoDatabase database  = mongoClient.getDatabase("test");
         MongoCollection<Document> collection = database.getCollection("productPrice");
        Document document = collection.find(eq("_id", productId)).first();
        String id = (String) document.get("_id");
        return id;
    }
}
