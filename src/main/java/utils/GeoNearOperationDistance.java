package utils;

import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.query.NearQuery;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class GeoNearOperationDistance implements AggregationOperation {

    private NearQuery nearQuery;

    private String distanceField = "dis";

    public GeoNearOperationDistance(NearQuery nearQuery) {
        this.nearQuery = nearQuery;
    }

    /**
     * Default is distance
     *
     * @return
     */
    public String getDistanceField() {
        return distanceField;
    }

    /**
     * Set distanceField value Default is distance
     *
     * @param distanceField
     */
    public void setDistanceField(String distanceField) {
        this.distanceField = distanceField;
    }


    @Override
    public DBObject toDBObject(AggregationOperationContext context) {
        DBObject dbObject = context.getMappedObject(nearQuery.toDBObject());
        dbObject.put("distanceField", distanceField);
        return new BasicDBObject("$geoNear", dbObject);
    }
}