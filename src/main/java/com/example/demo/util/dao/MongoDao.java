package com.example.demo.util.dao;


import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;
import java.util.Map;

//数据操作接口
public interface MongoDao {
    /**
     * 根据id检索文档
     *
     * @param db
     * @param table
     * @param id
     * @return
     * @throws Exception
     */
    public Map<String, Object> queryByID(MongoDatabase db, String table,Object id) throws Exception;

    /**
     * 插入文档
     *
     * @param db
     * @param table
     * @param doc
     * @return
     * @throws Exception
     */
    public boolean insert(MongoDatabase db, String table, Document doc)
            throws Exception;

    /**
     * 删除文档
     *
     * @param db
     * @param table
     * @param doc
     * @return
     * @throws Exception
     */
    public boolean deleteOne(MongoDatabase db, String table, BasicDBObject doc)
            throws Exception;

    /**
    * 删除文档
    *
    * @param db
    * @param table
    * @param doc
    * @return
    * @throws Exception
    */
    public boolean update(MongoDatabase db, String table, BasicDBObject oldDoc,
                          BasicDBObject newDoc) throws Exception;


}
