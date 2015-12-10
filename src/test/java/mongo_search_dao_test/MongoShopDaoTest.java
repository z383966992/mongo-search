package mongo_search_dao_test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dao.MongoShopDao;
import domain.QueryShopDomain;
import domain.ShopDomain;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/spring/spring-config.xml"})
public class MongoShopDaoTest {

    @Autowired
    private MongoShopDao mongoShopDao;


    @Test
//    @Ignore
    public void searchGeoNear() {

        QueryShopDomain queryShopDomain = new QueryShopDomain();
        queryShopDomain.setPageRows(2);
        queryShopDomain.setPageNum(1);
        queryShopDomain.setLongitude(116);
        queryShopDomain.setLatitude(40);
        queryShopDomain.setMaxDistance(3000);
        queryShopDomain.setClassifyId(2);
        queryShopDomain.setShopName("北辰商家");
        queryShopDomain.setSort("shopId");
        queryShopDomain.setSequence("asc");
        queryShopDomain.setPageSize(2);
        List<ShopDomain> shopNearByClassifyId = mongoShopDao.findShopNear(queryShopDomain);
        for (ShopDomain shopDomain : shopNearByClassifyId) {
            System.out.println("----------------------------------------");
            System.out.println(shopDomain);
            System.out.println(shopDomain.getDis());
        }
    }


}
