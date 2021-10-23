package com.example.demo.util;

import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoHelper {
        private  static  final Logger logger = LoggerFactory.getLogger(MongoHelper.class);
        static final  String DBName ="isrm";
        static final String ServerAddress ="127.0.0.1";
        static final int  PORT =27017;

        public  MongoHelper(){

        }
        public MongoClient getMongoClient() {
                MongoClient mongoClient = null;
                try {
                        // 连接到 mongodb 服务
                        mongoClient = new MongoClient(ServerAddress, PORT);
                        logger.debug("Connect to mongodb successfully");
                } catch (Exception e) {
                        System.err.println(e.getClass().getName() + ": " + e.getMessage());
                }
                return mongoClient;
        }

}
