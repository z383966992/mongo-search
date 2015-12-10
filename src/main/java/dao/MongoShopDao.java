package dao;

import java.util.List;

import utils.ResponsesDTO;
import domain.QueryShopDomain;
import domain.ShopDomain;

/**
 * 搜索店铺
 */
public interface MongoShopDao {

    List<ShopDomain> findShopNear(QueryShopDomain queryShopDomain);

    public ResponsesDTO incrComment(Long shopId, int comment) throws Exception;

    public void insertShopData(List<ShopDomain> list);

    public String geoNearTest();

    public ResponsesDTO deleteData(Long shopId);

}