package dao;

import java.util.List;

import utils.ResponsesDTO;
import domain.ShopDomain;

/**
 * 搜索接口
 * @author zhouliangliang1
 *
 */
public interface MongoSearchDao{
    
    /**
     * 搜索生活服务
     * 默认按照距离排序
     * @param classifyId 
     *        分类的id 送餐、水果、便利店等
     * @param longitude
     *        经度
     * @param latitude
     *        纬度
     * @param maxDistance
     *        搜索的最大范围，单位米
     * @param limit
     *        选取多少数据  如果不传默认返回3000条
     * @return ResponsesDTO
     *         搜索结果
     */
    public ResponsesDTO searchShop(Long classifyId, double longitude, double latitude, Double maxDistance, Integer limit) throws Exception;

    
    /**
     * 按地理位置/关键词搜索店铺
     * 默认按照距离排序
     * @param key
     *        店铺关键词
     * @param longitude
     *        经度
     * @param latitude
     *        纬度
     * @param maxDistance
     *        搜索的最大范围，单位米
     * @param limit
     *        选取多少数据  如果不传默认返回3000条
     * @return ResponsesDTO
     *        搜索结果
     */
    public ResponsesDTO searchShop(String key, double longitude, double latitude, Double maxDistance, Integer limit ) throws Exception;
   
    
    /**
     * 
     * @param shopId
     * @param comment
     * @return
     */
    public ResponsesDTO incrComment(Long shopId, int comment) throws Exception;
    
    /**
     * 向shopCollection插入数据
     * @param list
     *            ShopDomain 集合
     */
    public void insertShopData(List<ShopDomain> list);
    
    /**
     * 用geoNear查询距离
     */
    public String geoNearTest();


    /**
     * 删除数据
     * @param shopId
     */
    public ResponsesDTO deleteData(Long shopId);
}