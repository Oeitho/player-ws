package com.oeitho.resource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.oeitho.testcontainer.InfinispanResource;

@QuarkusTest
@QuarkusTestResource(InfinispanResource.class)
public class PlayerResourceTest {

    @Test
    public void fetchPlayerNotExisting() {
        when()
            .get("api/v1/player/1")
        .then()
            .statusCode(404);
    }

    @Test
    public void createPlayerEndpointTest() {
        given()
            .contentType("application/json")
            .body("{ \"testInt\": 1, \"testString\": \"test\" }")
        .when()
            .post("api/v1/player")
        .then()
            .statusCode(200)
            .header("playerId", notNullValue());
    }

    @Test
    public void fetchEmptyPlayerAfterCreation() {
        final String playerId = createPlayerAndGetId("{}");
        when()
            .get("api/v1/player/" + playerId)
        .then()
            .statusCode(200)
            .body(equalTo("{}"));
    }

    @Test
    public void fetchPlayerWithPropertiesAfterCreation() {
        final String playerId = createPlayerAndGetId("{ \"testInt\": 1, \"testString\": \"test\" }");
        when()
            .get("api/v1/player/" + playerId)
        .then()
            .statusCode(200)
            .body("testInt", equalTo(1),
                  "testString", equalTo("test"));
    }

    private String createPlayerAndGetId(String body) {
        final Response response = 
        given()
            .contentType("application/json")
            .body(body)
        .when()
            .post("/api/v1/player")
        .then()
            .extract()
            .response();
        return response.getHeaders().get("playerId").getValue();
    }

}