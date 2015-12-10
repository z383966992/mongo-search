package mongo_search_service_test;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import service.SearchService;
import utils.ResponsesDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/spring/spring-config.xml"})
public class MongoSearchServiceTest {

    @Autowired
    private SearchService searchService;

    @Test
    // @Ignore
    public void searchShopByClassify() {
        /**
         * 测试情况
         * 默认排序(距离升序)
         * 1.默认排序， 没有缓存，不分页
         * 2.默认排序，有缓存，不分页
         * 3.默认排序，没有缓存， 分页
         * 4.默认排序，有缓存，分页
         *
         * 店铺星级排序， 星级可以升序和降序
         * 1.星级排序，没有缓存，不分页
         * 2.星级排序，有缓存，不分页
         * 3.星级排序，没有缓存，分页
         * 4.星级排序，有缓存， 分页
         */


        ResponsesDTO response = searchService.searchShop(1L, 0.0, 0.0, 30000000.0, null, 1, 2, "shopLevel", "asc");

        JSONObject jsonObj = JSONObject.fromObject(response.getAttach());

        System.out.println("count : " + jsonObj.getInt("count"));

        Object content = jsonObj.get("content");

        JSONArray obj = JSONArray.fromObject(content);

        for (int i = 0; i < obj.size(); i++) {
            JSONObject jsonObject = JSONObject.fromObject(obj.get(i));
            System.out.println("shopLevel : " + jsonObject.getString("shopLevel"));
        }


        System.out.println();
        System.out.println("content.length : " + obj.size());

        for (int i = 0; i < obj.size(); i++) {
            JSONObject jsonObject = JSONObject.fromObject(obj.get(i));
            System.out.println(jsonObject.toString());
        }

    }

    @Test
    @Ignore
    public void searchShopByClassify2() {
        ResponsesDTO response = searchService.searchShop(1L, 0.0, 0.0, 30000000.0, null, 1, 2, "", "asc");

        JSONObject jsonObject = JSONObject.fromObject(response.getAttach());
        System.out.println(jsonObject.toString());

    }

    @Test
    @Ignore
    public void incrComment() {
        ResponsesDTO response = searchService.incrComment(4L, 5);

        System.out.println(response.getCode());
        System.out.println(response.getAttach());

    }

    @Test
    @Ignore
    public void delteData() {
        ResponsesDTO response = searchService.deleteData(4L);
        System.out.println(response.getCode());
        System.out.println(response.getAttach());
    }
}
