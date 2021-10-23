package com.example.demo.study;

import com.example.demo.util.MongoDBUtil;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * sdcsdc
 */
public class test {
    public static void main(String[] args) {
        //连接数据库
//        MongoHelper mongoHelper=new  MongoHelper();
//        MongoDaoImpl mongoDaoImpl=new MongoDaoImpl();
//        Document document=new  Document();
//        document.put("name","小李");
//        document.put("age",20);
//        document.put("sex","男");
//
//        MongoClient con=mongoHelper.getMongoClient();
//        mongoDaoImpl.insert( "isrm", "user",document);

//        mongoDaoImpl.queryByID(isrm,user,document);

        MongoDBUtil.getClient();
        MongoCollection<Document> coll = MongoDBUtil.getCollection("study","table");
       /* Document document1=new  Document();
        document1.put("name","小李3");
        document1.put("age",20);
        document1.put("sex","男");

        Document document2=new Document();
        document2.put("name","小李2");
        document2.put("age",200);
        document2.put("sex","女");

        Document document3 = new Document();
        document3.put("a","10");
        document3.put("b","20");
        document3.put("c","30");*/
        //插入数据
       /* for (int i = 0; i < 9; i++) {
            Document document3 = new Document();
            document3.put("a","10");
            document3.put("b","20");
            document3.put("c","30");
            MongoDBUtil.insert(coll,document3);
        }*/

//        查询数据
     /*   Document document3 = new  Document();
        document3.put("age",20);
        MongoCursor<Document> list = MongoDBUtil.find(coll,document3);
        while (list.hasNext()){
            Document user = list.next();
            System.out.println(user.toJson());
        }*/

        Document document3 = new Document();
        document3.put("name","xiaoli");
        //修改数据
        MongoDBUtil.updateById(coll,"6173ceae56605f094c65e6df",document3);
//      int a =   MongoDBUtil.deleteById(coll,"6173d28b56605f061cf7d6e7");

        //删除数据
        /*MongoDBUtil.delte(coll,document1);
        MongoDBUtil.delte(coll,document2);*/
    }
}
