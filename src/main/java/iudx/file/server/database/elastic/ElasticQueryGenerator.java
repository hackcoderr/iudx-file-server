package iudx.file.server.database.elastic;


import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import io.vertx.core.json.JsonObject;
import iudx.file.server.apiserver.query.QueryType;

public class ElasticQueryGenerator {


  public String getQuery(JsonObject json, QueryType type) {

    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
        .filter(QueryBuilders.termsQuery("id", json.getString("id")))
        .filter(QueryBuilders.rangeQuery("timeRange.startTime").lte(json.getString("time")))
        .filter(QueryBuilders.rangeQuery("timeRange.endTime").gte(json.getString("endTime")));

    return boolQuery.toString();
  }

  // TODO : discuss if it will be better to include other filters.
  public String deleteQuery(String id) {
    
      JsonObject json=new JsonObject(); 
      JsonObject matchJson=new JsonObject();
      matchJson.put("fileId", id); 
      json.put("match", matchJson);
     

    /*
     * BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
     * .filter(QueryBuilders.termsQuery("file-id", id));
     */
    
    return json.toString();
  }
}