package domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 搜索结果
 *
 * @author zhouliangliang1
 */
@Document(collection = "shopCollection")
public class ShopDomain implements Serializable {

    private static final long serialVersionUID = -606637325939378764L;

    @Id
    private String id;

    private Long shopId;

    private String shopName;

    private String openTime;

    private String endTime;

    private Integer categoryId;

    private Integer serviceType;

    private double[] position = new double[2];

    private String address;

    private String tel;

    private Integer state;

    private Integer shopLevel;

    private String shopPic;

    private String logoPic;

    private String remark;

    //    @Transient
    private String dis;

    private Integer comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getShopLevel() {
        return shopLevel;
    }

    public void setShopLevel(Integer shopLevel) {
        this.shopLevel = shopLevel;
    }

    public String getShopPic() {
        return shopPic;
    }

    public void setShopPic(String shopPic) {
        this.shopPic = shopPic;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getLogoPic() {
        return logoPic;
    }


    public void setLogoPic(String logoPic) {
        this.logoPic = logoPic;
    }


    public String getDis() {
        return new BigDecimal(dis).setScale(2, RoundingMode.HALF_UP).toString();
    }


    public void setDis(String dis) {
        this.dis = dis;
    }


    public Integer getComment() {
        return comment;
    }


    public void setComment(Integer comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "ShopDomain{" +
                "id='" + id + '\'' +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", openTime='" + openTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", categoryId=" + categoryId +
                ", serviceType=" + serviceType +
                ", position=" + Arrays.toString(position) +
                ", address='" + address + '\'' +
                ", tel='" + tel + '\'' +
                ", state=" + state +
                ", shopLevel=" + shopLevel +
                ", shopPic='" + shopPic + '\'' +
                ", logoPic='" + logoPic + '\'' +
                ", remark='" + remark + '\'' +
                ", dis='" + dis + '\'' +
                ", comment=" + comment +
                '}';
    }
}
