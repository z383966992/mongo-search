package mongo_search_dao_test;

/**
 * zhouliangliang
 * //一定要记得执行索引 类似这样：db.shopCollection.ensureIndex({position:"2d"})
 */
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utils.ResponsesDTO;
import dao.MongoSearchDao;
import domain.ShopDomain;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/spring/spring-config.xml" })
public class MongoSearchDaoTest {

    @Autowired
    private MongoSearchDao mongoSearchDao;
    
    @Test
    @Ignore
    public void test() {
    	try{
    		System.out.println(mongoSearchDao);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
    /**
     * 向shopCollection插入测试数据
     */
    @Test
    @Ignore
    public void insertShopData() throws Exception{
        
        ShopDomain shopDomain1 = new ShopDomain();
        double [] position1 = new double[2];
        position1[0] = 0.1d;
        position1[1] = -0.1d;
        shopDomain1.setShopId(1L);
        shopDomain1.setShopName("测试商店1");
        shopDomain1.setOpenTime("12:00:00");
        shopDomain1.setEndTime("16:00:00");
        shopDomain1.setCategoryId(1);
        shopDomain1.setServiceType(1);
        shopDomain1.setPosition(position1);
        shopDomain1.setAddress("中关村");
        shopDomain1.setTel("110");
        shopDomain1.setState(0);
        shopDomain1.setShopLevel(1);
        shopDomain1.setShopPic("http://www.baidu.com");
        shopDomain1.setLogoPic("http://storage.jcloud.com/jdlife.mobi/103.png");
        shopDomain1.setRemark("我是remark");
        shopDomain1.setComment(0);
        
        ShopDomain shopDomain2 = new ShopDomain();
        double [] position2 = new double[2];
        position2[0] = 1.0d;
        position2[1] = 1.0d;
      
        shopDomain2.setShopId(2L);
        shopDomain2.setShopName("测试商店2");
        shopDomain2.setOpenTime("12:00:00");
        shopDomain2.setEndTime("16:00:00");
        shopDomain2.setCategoryId(1);
        shopDomain2.setServiceType(1);
        shopDomain2.setPosition(position2);
        shopDomain2.setAddress("海淀黄庄");
        shopDomain2.setTel("110");
        shopDomain2.setState(0);
        shopDomain2.setShopLevel(2);
        shopDomain2.setShopPic("http://www.baidu.com");
        shopDomain2.setLogoPic("http://storage.jcloud.com/jdlife.mobi/103.png");
        shopDomain2.setRemark("我是remark");
        shopDomain2.setComment(0);
        
        ShopDomain shopDomain3 = new ShopDomain();
        double [] position3 = new double[2];
        position3[0] = 0.5d;
        position3[1] = 0.5d;
        shopDomain3.setShopId(3L);
        shopDomain3.setShopName("测试商店3");
        shopDomain3.setOpenTime("12:00:00");
        shopDomain3.setEndTime("16:00:00");
        shopDomain3.setCategoryId(1);
        shopDomain3.setServiceType(1);
        shopDomain3.setPosition(position3);
        shopDomain3.setAddress("上地");
        shopDomain3.setTel("110");
        shopDomain3.setState(0);
        shopDomain3.setShopLevel(3);
        shopDomain3.setShopPic("http://www.baidu.com");
        shopDomain3.setLogoPic("http://storage.jcloud.com/jdlife.mobi/103.png");
        shopDomain3.setRemark("我是remark");
        shopDomain3.setComment(0);
        
        ShopDomain shopDomain4 = new ShopDomain();
        double [] position4 = new double[2];
        position4[0] = -0.5d;
        position4[1] = -0.5d;
        shopDomain4.setShopId(4L);
        shopDomain4.setShopName("测试商店4");
        shopDomain4.setOpenTime("12:00");
        shopDomain4.setEndTime("16:00");
        shopDomain4.setCategoryId(1);
        shopDomain4.setServiceType(1);
        shopDomain4.setPosition(position4);
        shopDomain4.setAddress("益圆");
        shopDomain4.setTel("110");
        shopDomain4.setState(0);
        shopDomain4.setShopLevel(3);
        shopDomain4.setShopPic("http://www.baidu.com");
        shopDomain4.setLogoPic("http://storage.jcloud.com/jdlife.mobi/103.png");
        shopDomain4.setRemark("我是remark");
        shopDomain4.setComment(0);
        
        List<ShopDomain> list = new LinkedList<ShopDomain>();
        list.add(shopDomain1);
        list.add(shopDomain2);
        list.add(shopDomain3);
        list.add(shopDomain4);
        
        mongoSearchDao.insertShopData(list);
    }
    
    @Test
    @Ignore
    public void searchShop(){
        try {
            ResponsesDTO response = mongoSearchDao.searchShop("商店", 0.0, 0.0, 30000.0, 2);
            System.out.println(response.getAttach());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
    @Test
    @Ignore
    public void searchShop2(){
        try {
            ResponsesDTO response = mongoSearchDao.searchShop(1L, 0.0, 0.0, 30000000.0, null);
            System.out.println(JSONArray.fromObject(response.getAttach()).size());
            System.out.println(response.getAttach());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    @Ignore
    public void searchGeoNear(){
        System.out.println(mongoSearchDao.geoNearTest());
    }
//    @Test
//    @Ignore
//    public void search(){
//        
//        System.out.println(mongoSearchDao);
//        
//        List<TestData> list = mongoSearchDao.searchTestData();
//        for(TestData td : list){
//            System.out.print(td.getId() + "   ");
//            System.out.println(td.getX());
//        }
//    }
    
//    @Test
//    @Ignore
//    public void insertTestData(){
//        List<TestData> list = new LinkedList<TestData>();
//        TestData td1 = new TestData();
//        td1.setX("200");
//        
//        TestData td2 = new TestData();
//        td2.setX("210");
//        
//        TestData td3 = new TestData();
//        td3.setX("220");
//        
//        list.add(td1);
//        list.add(td2);
//        list.add(td3);
//        mongoSearchDao.insertTestData(list);
//    }
    
    
//    @Test
//    @Ignore
//    //一定要记得执行索引 类似这样：db.shopCollection.ensureIndex({position:"2d"})
////    public void searchShopData(){
////        List<ShopDomain> list = mongoSearchDao.searchShopData(new Point(0, 0), 0.7);
////        
////        System.out.println(list.size());
////        for(ShopDomain sd : list){
////            System.out.println(sd.getPosition()[0] + "  " +sd.getPosition()[1]);
////        }
////    }
    
//    @Test
//    @Ignore
//    public void searchShopData2(){
//        mongoSearchDao.searchShopData2();
//    }
}
