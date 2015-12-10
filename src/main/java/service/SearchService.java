package service;

import java.util.List;

import utils.ResponsesDTO;
import domain.ShopDomain;

/**
 * 搜索服务service
 *
 * @author zhouliangliang1
 */
public interface SearchService {

    /**
     * 根据店铺分类id搜索店铺(精确匹配)
     *
     * @param classifyId  分类id
     * @param longitude   经度
     * @param latitude    纬度
     * @param maxDistance 搜索的最大距离
     * @param limit       搜索返回的数据条数
     * @param pageNum     分页的页码，必须与每页数据同时传入，有一个为null则返回全部数据
     * @param pageSize    每页数据，必须与分页的页码同时传入，有一个为null则返回全部数据
     * @param sort        按照什么排序，目前默认是shopLevel，为""则默认按照dis排序
     * @param sequence    排序的顺序 asc-升序， desc-降序
     * @return
     */
    public ResponsesDTO searchShop(Long classifyId, double longitude, double latitude, double maxDistance,
                                   Integer limit, Integer pageNum, Integer pageSize, String sort, String sequence);


    /**
     * 根据店铺名称搜索店铺(模糊匹配)
     *
     * @param key         店铺名称
     * @param longitude   经度
     * @param latitude    纬度
     * @param maxDistance 搜索的最大距离
     * @param limit       搜索返回的数据条数
     * @return
     */
    public ResponsesDTO searchShop(String key, double longitude, double latitude, double maxDistance,
                                   Integer limit, Integer pageNum, Integer pageSize, String sort, String sequence);

    /**
     * 给店铺增加评论数
     *
     * @param shopId  增加评论数的店铺id
     * @param comment 增加的评论数
     */
    public ResponsesDTO incrComment(Long shopId, int comment);

    /**
     * 向mongo数据集合shopCollection插入数据
     */
    public ResponsesDTO insertData(List<ShopDomain> list);

    /**
     * 从mongo数据集合shopCollection删除数据
     */
    public ResponsesDTO deleteData(Long shopId);
}
