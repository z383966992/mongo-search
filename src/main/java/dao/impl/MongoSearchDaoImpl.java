package dao.impl;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import utils.ResponsesDTO;
import utils.ReturnCode;

import com.mongodb.CommandResult;

import dao.MongoSearchDao;
import domain.ShopDomain;
/**
 * 搜索实现
 * @author zhouliangliang1
 *
 */
public class MongoSearchDaoImpl implements MongoSearchDao{

    
    private MongoTemplate mongoTemplate;
    
    @Override
    public ResponsesDTO searchShop(Long classifyId, double longitude, double latitude, 
            Double maxDistance, Integer limit) throws Exception{
        
        connectionInfo();
        
        double dis = maxDistance/6371;
        
        String queryCommand = "{geoNear:\"shopCollection\"" +","
                            + "near:[" + longitude + "," + latitude + "]" + ","
                            + "distanceMultiplier:6371, spherical:true, "
                            + "maxDistance:" + dis;
        limit = limit == null ? 3000 : limit;
        queryCommand = queryCommand + ", " + "limit:" + limit + ", " + "query:{categoryId:" + classifyId.toString() + "}}";
                            
        CommandResult commandResult = mongoTemplate.executeCommand(queryCommand);
        
        ResponsesDTO responsesDTO = new ResponsesDTO(ReturnCode.ACTIVE_FAILURE);
        
        if (commandResult.containsField("results")) {
            JSONArray array = parseResult(commandResult.get("results"));
            responsesDTO.setCode(ReturnCode.ACTIVE_SUCCESS.code());
//            responsesDTO.setAttach(commandResult.get("results"));
            responsesDTO.setAttach(array);
            return responsesDTO;
        } else if (commandResult.containsField("errmsg")) {
            responsesDTO.setCode(ReturnCode.ACTIVE_FAILURE.code());
            responsesDTO.setAttach(commandResult.getString("errmsg"));
           return responsesDTO;
        } else {
            return responsesDTO;
        }
    }

    @Override
    public ResponsesDTO searchShop(String key, double longitude, double latitude, Double maxDistance, Integer limit) throws Exception{
        
        connectionInfo();
        
        double dis = maxDistance/6371;
        
        String queryCommand = "{geoNear:\"shopCollection\"" +","
                            + "near:[" + longitude + "," + latitude + "]" + ","
                            + "distanceMultiplier:6371, spherical:true, "
                            + "maxDistance:" + dis;
        
        limit = limit == null ? 3000 : limit;
        queryCommand = queryCommand + ", " + "limit:" + limit + ", " + "query:{shopName:{$regex:" + "\"" + key + "\"" + "}}}";
        
        CommandResult commandResult = mongoTemplate.executeCommand(queryCommand);
        
//        GeoResults<ShopDomain> geoResults = mongoTemplate.geoNear(NearQuery.near(0.0, 0.0).distanceMultiplier(6378137).spherical(true).num(10).query(new Query(Criteria.where("shopName").regex("商店"))), ShopDomain.class);
        
        ResponsesDTO responsesDTO = new ResponsesDTO(ReturnCode.ACTIVE_FAILURE);
        
        if (commandResult.containsField("results")) {
            JSONArray array = parseResult(commandResult.get("results"));
            responsesDTO.setCode(ReturnCode.ACTIVE_SUCCESS.code());
            responsesDTO.setAttach(array);
            return responsesDTO;
        } else if (commandResult.containsField("errmsg")) {
            responsesDTO.setCode(ReturnCode.ACTIVE_FAILURE.code());
            responsesDTO.setAttach(commandResult.getString("errmsg"));
           return responsesDTO;
        } else {
            return responsesDTO;
        }
    }
    
    
    @Override
    public ResponsesDTO incrComment(Long shopId, int comment) throws Exception {
        
        connectionInfo();
        
        ResponsesDTO responsesDTO = new ResponsesDTO(ReturnCode.ACTIVE_FAILURE);
        
        mongoTemplate.updateFirst(new Query().addCriteria(new Criteria("shopId").is(shopId)), new Update().inc("comment", comment), ShopDomain.class);
        
        responsesDTO.setCode(ReturnCode.ACTIVE_SUCCESS.code());
        responsesDTO.setAttach(ReturnCode.ACTIVE_SUCCESS.msg());
        return responsesDTO;
    }
    
    @Override
    public ResponsesDTO deleteData(Long shopId) {
        connectionInfo();
        
        ResponsesDTO responsesDTO = new ResponsesDTO(ReturnCode.ACTIVE_FAILURE);
        
        mongoTemplate.remove(new Query(new Criteria("shopId").is(shopId)), ShopDomain.class);
        
        responsesDTO.setCode(ReturnCode.ACTIVE_SUCCESS.code());
        responsesDTO.setAttach(ReturnCode.ACTIVE_SUCCESS.msg());
        return responsesDTO;
        
    }
    
    
    /**
     * 解析搜索返回的数据，去除_id 和 _class
     * @param object
     * @return
     */
    private JSONArray parseResult(Object object) {
        JSONArray jsonArray = JSONArray.fromObject(object);
        JSONArray returnArray = new JSONArray();
        for(int i=0; i<jsonArray.size(); i++){
            JSONObject attach = JSONObject.fromObject(jsonArray.get(i));
            Object obj = attach.get("obj");
            String dis = attach.getString("dis");
            JSONObject attachIteam = JSONObject.fromObject(obj);
            attachIteam.remove("_id");
            attachIteam.remove("_class");
            attachIteam.element("dis", dis);
            returnArray.add(attachIteam);
        }
        return returnArray;
    }
    
    
    
    @Override
    public void insertShopData(List<ShopDomain> list) {
        mongoTemplate.insert(list, ShopDomain.class);
    }
    
    @Override
    public String geoNearTest() {
        //CommandResult commandResult = mongoTemplate.executeCommand("{geoNear:\"shopCollection\", near:[0,0], distanceMultiplier:6378137, maxDistance:0.0004703567828662194, limit:2, spherical:true, query:{shopName:{$regex: \"商店\"}}}");
        CommandResult commandResult = mongoTemplate.executeCommand("{geoNear:\"shopCollection\",near:[0.0,0.0],distanceMultiplier:6378137, spherical:true, maxDistance:distance/6378137, query:{shopName:{$regex:\"商店\"}}}");
        return commandResult.toString();
    }
    
    private void connectionInfo(){
        System.out.println("db : " + mongoTemplate.getDb().toString());
        System.out.println("collection : " + mongoTemplate.getCollectionName(ShopDomain.class));
        System.out.println("host : " + mongoTemplate.getDb().getMongo().getAddress());
    }
    

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}




//return queryCommand.toString();
//这是用MongoTemplate返回对象的写法，给app返回json串
//GeoResults<ShopDomain> geoResults = mongoTemplate.geoNear(NearQuery.near(0.0, 0.0).distanceMultiplier(6378137).spherical(true).num(10).query(new Query(Criteria.where("shopName").regex("商店"))), ShopDomain.class);
//
//for(int i=0; i<geoResults.getContent().size(); i++){
//GeoResult<ShopDomain> geoResult = geoResults.getContent().get(i);
//System.out.println(geoResult.getDistance().getValue());
//System.out.println(geoResult.getContent().getAddress());
//}


//这是用MongoTemplate返回对象的写法，给app返回json串
//GeoResults<ShopDomain> geoResults = mongoTemplate.geoNear(NearQuery.near(0.0, 0.0).distanceMultiplier(6378137).spherical(true).num(10).query(new Query(Criteria.where("shopName").regex("商店"))), ShopDomain.class);
//
//for(int i=0; i<geoResults.getContent().size(); i++){
//  GeoResult<ShopDomain> geoResult = geoResults.getContent().get(i);
//  System.out.println(geoResult.getDistance().getValue());
//  System.out.println(geoResult.getContent().getAddress());
//}