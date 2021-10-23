package com.example.demo.util;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;


/**
 * MongoDB工具类 Mongo实例代表了一个数据库连接池，即使在多线程的环境中，一个Mongo实例对我们来说已经足够了<br>
 * 注意Mongo已经实现了连接池，并且是线程安全的。 <br>
 * 设计为单例模式， 因 MongoDB的Java驱动是线程安全的，对于一般的应用，只要一个Mongo实例即可，<br>
 * Mongo有个内置的连接池（默认为10个） 对于有大量写和读的环境中，为了确保在一个Session中使用同一个DB时，<br>
 * DB和DBCollection是绝对线程安全的<br>
 * 
 * @author 
 * @date 2015-5-29 上午11:49:49
 * @version 0.0.0
 * @Copyright (c)1997-2015 NavInfo Co.Ltd. All Rights Reserved.
 */
public class MongoDBUtil {
	
    private static MongoClient mongoClient;
    
    public static void getClient() {
    	if(mongoClient == null){
	        mongoClient = new MongoClient("127.0.0.1",27017);
	        Builder options = new Builder();
	        options.connectionsPerHost(300);// 连接池设置为300个连接,默认为100
	        options.connectTimeout(5000);// 连接超时，推荐>3000毫秒
	        options.maxWaitTime(5000); //
	        options.socketTimeout(0);// 套接字超时时间，0无限制
	        options.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
	        options.writeConcern(WriteConcern.SAFE);//
	        options.build();
    	}
    }

    // ------------------------------------共用方法---------------------------------------------------
    /**
     * 获取DB实例 - 指定DB
     * 
     * @param dbName
     * @return
     */
    public static MongoDatabase getDB(String dbName) {
        if (dbName != null && !"".equals(dbName)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            return database;
        }
        return null;
    }

    /**
     * 获取collection对象 - 指定Collection
     * 
     * @param collName
     * @return
     */
    public static MongoCollection<Document> getCollection(String dbName, String collName) {
        if (null == collName || "".equals(collName)) {
            return null;
        }
        if (null == dbName || "".equals(dbName)) {
            return null;
        }
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collName);
        return collection;
    }

    /**
     * 查询DB下的所有表名
     */
    public static List<String> getAllCollections(String dbName) {
        MongoIterable<String> colls = getDB(dbName).listCollectionNames();
        List<String> _list = new ArrayList<String>();
        for (String s : colls) {
            _list.add(s);
        }
        return _list;
    }

    /**
     * 获取所有数据库名称列表
     * 
     * @return
     */
    public MongoIterable<String> getAllDBNames() {
        MongoIterable<String> s = mongoClient.listDatabaseNames();
        return s;
    }

    /**
     * 删除一个数据库
     */
    public void dropDB(String dbName) {
        getDB(dbName).drop();
    }

    /**
     * 查找对象 - 根据主键_id
     * 
     * @param
     * @param id
     * @return
     */
    public static Document findById(MongoCollection<Document> coll, String id) {
        ObjectId _idobj = null;
        try {
            _idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        Document myDoc = coll.find(Filters.eq("_id", _idobj)).first();
        return myDoc;
    }

    /** 统计数 */
    public int getCount(MongoCollection<Document> coll) {
        int count = (int) coll.count();
        return count;
    }

    /** 条件查询 */
    public static MongoCursor<Document> find(MongoCollection<Document> coll, Bson filter) {
        return coll.find(filter).iterator();
    }
    
    /** 分页查询 */
    public MongoCursor<Document> findByPage(MongoCollection<Document> coll, Bson filter, int pageNo, int pageSize) {
        Bson orderBy = new BasicDBObject("_id", 1);
        return coll.find(filter).sort(orderBy).skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
    }

    /**
     * 通过ID删除
     * 
     * @param coll
     * @param id
     * @return
     */
    public static  int deleteById(MongoCollection<Document> coll, String id) {
        int count = 0;
        ObjectId _id = null;
        try {
            _id = new ObjectId(id);
        } catch (Exception e) {
            return 0;
        }
        Bson filter = Filters.eq("_id", _id);
        DeleteResult deleteResult = coll.deleteOne(filter);
        count = (int) deleteResult.getDeletedCount();
        return count;
    }
    
    /**
     * 插入数据
     * 
     */

    public static void insert(MongoCollection<Document> coll ,Document document){
    	coll.insertOne(document);
    }
    
    
    /**
     * FIXME
     * 
     * @param coll
     * @param id
     * @param newdoc
     * @return
     */
    public static   Document updateById(MongoCollection<Document> coll, String id, Document newdoc) {
        ObjectId _idobj = null;
        try {
            _idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        Bson filter = Filters.eq("_id", _idobj);
        // coll.replaceOne(filter, newdoc); // 完全替代
        coll.updateOne(filter, new Document("$set", newdoc));
        return newdoc;
    }
    
    /**
     * 
     * @param coll
     * @param cs:参数名
     * @param zi:值
     * @param newdoc
     */
    public static void update(MongoCollection<Document> coll,String cs,String zi, Document newdoc){
    	coll.updateMany(Filters.eq(cs, zi), new Document("$set",newdoc));
    }

    public void dropCollection(String dbName, String collName) {
        getDB(dbName).getCollection(collName).drop();
    }

    public static void delte(MongoCollection<Document> coll, Document newdoc){
        coll.deleteOne(newdoc);
    }

    /**
     * 关闭Mongodb
     */
    public static void close() {
//        if (mongoClient != null) {
//            mongoClient.close();
//        }
    }

}
