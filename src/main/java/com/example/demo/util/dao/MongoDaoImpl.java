package com.example.demo.util.dao;

import com.example.demo.util.JsonStrToMap;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MongoDaoImpl implements MongoDao {
    private static final Logger logger = LoggerFactory
            .getLogger(MongoDaoImpl.class);

    /**
     * 查询文档
     *
     * @param db
     * @param table
     * @param
     * @return
     * @throws Exception
     */

    public  Map<String, Object>  queryByID(MongoDatabase db,String table,Object id) throws Exception{
        MongoCollection<Document> collection=db.getCollection(table);
        BasicDBObject query =new BasicDBObject("_id",id);
        FindIterable<Document>  iterable = collection.find(query);

        Map<String, Object> jsonStrToMap = null;
        MongoCursor<Document> cursor=iterable.iterator();
        while (cursor.hasNext()){
            Document user = cursor.next();
            String jsonString = user.toJson();
            jsonStrToMap = JsonStrToMap.jsonStrToMap(jsonString);
        }
        logger.debug("检索ID完毕，db：{}，table：{}，id：{} ", db.getName(), table, id);
        return jsonStrToMap;
    }



    /**
     * 插入文档
     *
     * @param db
     * @param table
     * @param
     * @return
     * @throws Exception
     */
    public  boolean insert(MongoDatabase db,String table,Document doc){
        MongoCollection<Document> collection=db.getCollection(table);
        collection.insertOne(doc);
        long  count =collection.count(doc);
        if(count>=1){
            logger.debug("文档插入成功，影响条数：{}，db：{}，table：{}，doc：{} ", count,
                    db.getName(), table, doc.toJson());
                    return true;
        }else {
            logger.debug("文档插入失败，影响条数：{}，db：{}，table：{}，doc：{} ", count,
                    db.getName(), table, doc.toJson());
        }
                    return false;
    }


    /**
     * 删除文档
     *
     * @param db
     * @param table
     * @param doc
     * @return
     * @throws Exception
     */
    public boolean deleteOne(MongoDatabase db, String table, BasicDBObject doc) throws Exception {
        MongoCollection<Document> collection = db.getCollection(table);
        DeleteResult deleteOneResult = collection.deleteOne(doc);
        long deletedCount = deleteOneResult.getDeletedCount();
        System.out.println("删除的数量: " + deletedCount);
        if (deletedCount == 1) {
            logger.debug("文档删除成功，影响条数：{}，db：{}，table：{}，doc：{} ", deletedCount,
                    db.getName(), table, doc.toJson());
            return true;
        } else {
            logger.debug("文档删除失败，影响条数：{}，db：{}，table：{}，doc：{} ", 0,
                    db.getName(), table, doc.toJson());
            return false;
        }
    }

    /**
     * 修改文档
     *
     * @param db
     * @param table
     * @param
     * @return
     * @throws Exception
     */
    public boolean update(MongoDatabase db, String table,
                          BasicDBObject whereDoc, BasicDBObject updateDoc) throws Exception {
        MongoCollection<Document> collection = db.getCollection(table);
        UpdateResult updateManyResult = collection.updateMany(whereDoc,
                new Document("$set", updateDoc));
        long modifiedCount = updateManyResult.getModifiedCount();
        System.out.println("修改的数量: " + modifiedCount);

        if (modifiedCount > 0) {
            logger.debug(
                    "文档更新成功，影响条数：{}，db：{}，table：{}，whereDoc：{}，updateDoc：{} ",
                    modifiedCount, db.getName(), table, whereDoc.toJson(),
                    updateDoc.toJson());
            return true;
        } else {
            logger.debug(
                    "文档更新成功，影响条数：{}，db：{}，table：{}，whereDoc：{}，updateDoc：{} ",
                    0, db.getName(), table, whereDoc.toJson(),
                    updateDoc.toJson());
            return false;
        }
    }

}
