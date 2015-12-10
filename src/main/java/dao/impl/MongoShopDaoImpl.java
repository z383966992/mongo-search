package dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import utils.GeoNearOperationDistance;
import utils.ResponsesDTO;
import utils.ReturnCode;

import com.mongodb.CommandResult;

import dao.MongoShopDao;
import domain.QueryShopDomain;
import domain.ShopDomain;

/**
 * 搜索店铺
 */
public class MongoShopDaoImpl implements MongoShopDao {
    @Resource
    private MongoTemplate mongoTemplate;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<ShopDomain> findShopNear(QueryShopDomain queryShopDomain) {
        String sort = queryShopDomain.getSort();
        String sequence = queryShopDomain.getSequence();
        Integer pageSize = queryShopDomain.getPageSize();

        NearQuery spherical = NearQuery.near(queryShopDomain.getLongitude(), queryShopDomain.getLatitude()).spherical(true).distanceMultiplier(6371 * Math.PI / 180.0d * 1000);
        Query query = new Query();
        if (queryShopDomain.getMaxDistance() >= 0) {
            spherical = spherical.maxDistance(queryShopDomain.getMaxDistance());
        }
        if (StringUtils.isNotBlank(queryShopDomain.getShopName())) {
            query.addCriteria(Criteria.where("shopName").regex(queryShopDomain.getShopName()));
        }
        if (queryShopDomain.getClassifyId() != null) {
            query.addCriteria(Criteria.where("categoryId").is(queryShopDomain.getClassifyId()));
        }
        GeoNearOperationDistance geoNearOperationDistance = new GeoNearOperationDistance(spherical.query(query));

        //分页
        if (pageSize != null) {
            SkipOperation skipOperation = new SkipOperation(queryShopDomain.getStartIdx() - 1);
            LimitOperation limitOperation = new LimitOperation(queryShopDomain.getPageRows());

            //排序
            if (StringUtils.isNotBlank(sort)) {
                SortOperation sortOperation = new SortOperation(new Sort(Sort.Direction.DESC, sort));
                if (StringUtils.isNotBlank(sequence)) {
                    sortOperation = new SortOperation(new Sort(new Sort.Order("asc".equals(sort) ? Sort.Direction.ASC : Sort.Direction.DESC, sort)));
                }
                Aggregation aggregation = Aggregation.newAggregation(geoNearOperationDistance, skipOperation, limitOperation, sortOperation);
                AggregationResults<ShopDomain> aggregate = mongoTemplate.aggregate(aggregation, "shopCollection", ShopDomain.class);
                return aggregate.getMappedResults();
            }

            Aggregation aggregation = Aggregation.newAggregation(geoNearOperationDistance, skipOperation, limitOperation);
            AggregationResults<ShopDomain> aggregate = mongoTemplate.aggregate(aggregation, "shopCollection", ShopDomain.class);
            return aggregate.getMappedResults();
        }

        //排序
        if (StringUtils.isNotBlank(sort)) {
            SortOperation sortOperation = new SortOperation(new Sort(Sort.Direction.DESC, sort));
            if (StringUtils.isNotBlank(sequence)) {
                sortOperation = new SortOperation(new Sort(new Sort.Order("asc".equals(sequence) ? Sort.Direction.ASC : Sort.Direction.DESC, sort)));
            }
            Aggregation aggregation = Aggregation.newAggregation(geoNearOperationDistance, sortOperation);
            AggregationResults<ShopDomain> aggregate = mongoTemplate.aggregate(aggregation, "shopCollection", ShopDomain.class);
            return aggregate.getMappedResults();
        }

        Aggregation aggregation = Aggregation.newAggregation(geoNearOperationDistance);
        AggregationResults<ShopDomain> aggregate = mongoTemplate.aggregate(aggregation, "shopCollection", ShopDomain.class);
        return aggregate.getMappedResults();
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
    public void insertShopData(List<ShopDomain> list) {
        mongoTemplate.insert(list, ShopDomain.class);
    }

    @Override
    public String geoNearTest() {
        //CommandResult commandResult = mongoTemplate.executeCommand("{geoNear:\"shopCollection\", near:[0,0], distanceMultiplier:6378137, maxDistance:0.0004703567828662194, limit:2, spherical:true, query:{shopName:{$regex: \"商店\"}}}");
        CommandResult commandResult = mongoTemplate.executeCommand("{geoNear:\"shopCollection\",near:[0.0,0.0],distanceMultiplier:6378137, spherical:true, maxDistance:distance/6378137, query:{shopName:{$regex:\"商店\"}}}");
        return commandResult.toString();
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

    private void connectionInfo() {
        System.out.println("db : " + mongoTemplate.getDb().toString());
        System.out.println("collection : " + mongoTemplate.getCollectionName(ShopDomain.class));
        System.out.println("host : " + mongoTemplate.getDb().getMongo().getAddress());
    }


}




