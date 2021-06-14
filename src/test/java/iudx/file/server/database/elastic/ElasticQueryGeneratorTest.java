package iudx.file.server.database.elastic;

import static iudx.file.server.database.utilities.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import iudx.file.server.common.QueryType;

@ExtendWith(VertxExtension.class)
public class ElasticQueryGeneratorTest {

  private ElasticQueryGenerator elasticQueryGenerator;

  @BeforeEach
  public void setup(Vertx vertx, VertxTestContext testContext) {
    elasticQueryGenerator = new ElasticQueryGenerator();
    testContext.completeNow();
  }

  @Test
  public void testGenerateListQuery(Vertx vertx, VertxTestContext testContext) {
    // query
    JsonObject query = new JsonObject();
    query.put(ID, "IUDX_ID");

    // expected elastic query;
    String elasticQuery =
        "{\"bool\":{\"filter\":[{\"terms\":{\"id\":[\"IUDX_ID\"],\"boost\":1.0}}],\"adjust_pure_negative\":true,\"boost\":1.0}}";

    // call test method
    String generatedQuery = elasticQueryGenerator.getQuery(query, QueryType.LIST);
    // assertions
    assertEquals(new JsonObject(elasticQuery), new JsonObject(generatedQuery));
    assertTrue(generatedQuery.contains("bool"));
    assertTrue(generatedQuery.contains("IUDX_ID"));
    testContext.completeNow();
  }

  @Test
  public void testGenerateTemporalQuery(Vertx vertx, VertxTestContext testContext) {
    // query
    JsonObject temporalQuery = new JsonObject("{\n" +
        "    \"id\": \"iisc.ac.in/89a36273d77dac4cf38114fca1bbe64392547f86/file.iudx.io/surat-itms-realtime-information/surat-itms-live-eta\",\n"
        +
        "    \"timerel\": \"during\",\n" +
        "    \"time\": \"2020-09-10T00:00:00Z\",\n" +
        "    \"endTime\": \"2020-09-15T00:00:00Z\"\n" +
        "}");

    // expected elastic query
    String elasticQuery = "{\n" +
        "    \"bool\": {\n" +
        "      \"filter\": [\n" +
        "        {\n" +
        "          \"terms\": {\n" +
        "            \"id\": [\n" +
        "              \"iisc.ac.in/89a36273d77dac4cf38114fca1bbe64392547f86/file.iudx.io/surat-itms-realtime-information/surat-itms-live-eta\"\n"
        +
        "            ],\n" +
        "            \"boost\": 1.0\n" +
        "          }\n" +
        "        },\n" +
        "        {\n" +
        "          \"range\": {\n" +
        "            \"timeRange\": {\n" +
        "              \"from\": \"2020-09-10T00:00:00Z\",\n" +
        "              \"to\": \"2020-09-15T00:00:00Z\",\n" +
        "              \"include_lower\": true,\n" +
        "              \"include_upper\": true,\n" +
        "              \"boost\": 1.0\n" +
        "            }\n" +
        "          }\n" +
        "        }\n" +
        "      ],\n" +
        "      \"adjust_pure_negative\": true,\n" +
        "      \"boost\": 1.0\n" +
        "    }\n" +
        "  }";


    // call test method
    String generatedQuery = elasticQueryGenerator.getQuery(temporalQuery, QueryType.TEMPORAL);
    // assertions
    assertEquals(new JsonObject(elasticQuery), new JsonObject(generatedQuery));
    assertTrue(generatedQuery.contains("bool"));
    assertTrue(generatedQuery.contains("timeRange"));
    testContext.completeNow();
  }
}