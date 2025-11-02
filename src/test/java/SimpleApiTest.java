import config.ApiConfig;
import io.restassured.response.Response;

import models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utils.UserHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimpleApiTest {
    
    private static final String BASE_URL = ApiConfig.getBaseUrl();
    private static final String API_KEY = ApiConfig.getApiKey();

    // Scenario_1: Get a list of available users and print users with odd ID numbers
    @Test
    void shouldGetUsersAndPrintOddIds() {
        Response firstPageResponse = given()
                .header("x-api-key", API_KEY)
                .queryParam("page", 1)
                .when()
                .get(BASE_URL + "/users")
                .then()
                .statusCode(200)
                .extract()
                .response();

        int totalPages = firstPageResponse.jsonPath().getInt("total_pages");
        System.out.println("Total pages: " + totalPages);

        List<User> allOddUsers = new ArrayList<>(UserHelper.processUsersWithOddIds(firstPageResponse));

        for (int page = 2; page <= totalPages; page++) {
            Response response = given()
                    .header("x-api-key", API_KEY)
                    .queryParam("page", page)
                    .when()
                    .get(BASE_URL + "/users")
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();

            allOddUsers.addAll(UserHelper.processUsersWithOddIds(response));
        }

        System.out.println("== Users with odd ID numbers ==");
        for (User user : allOddUsers) {
            System.out.println(user.getUserInfo());
        }
    }
    
    // Scenario_2: Create a new user and validate creation date is today
    @Test
    void shouldCreateUserAndValidateDate() {
        String todayDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        
        Response response = given()
                .header("x-api-key", API_KEY)
                .contentType("application/json")
                .body("{ \"name\": \"John Doe\", \"job\": \"QA Engineer\" }")
                .when()
                .post(BASE_URL + "/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("John Doe"))
                .body("job", equalTo("QA Engineer"))
                .body("id", notNullValue())
                .extract()
                .response();

        String createdAt = response.jsonPath().getString("createdAt");
        assertTrue(createdAt.startsWith(todayDate), 
                "Creation date should be today. Expected: " + todayDate + ", but got: " + createdAt);
    }


    // Scenario_3: Update a user and validate response matches request
    @Test
    void shouldUpdateUserAndValidateResponse() {
        String requestBody = "{ \"name\": \"Jane Smith\", \"job\": \"Senior Developer\" }";
        
        given()
                .header("x-api-key", API_KEY)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(BASE_URL + "/users/2")
                .then()
                .statusCode(200)
                .body("name", equalTo("Jane Smith"))
                .body("job", equalTo("Senior Developer"))
                .body("updatedAt", notNullValue());
    }

    // Scenario_4: Parametrized test for response time with delay parameter
    @ParameterizedTest
    @ValueSource(ints = {0, 3})
    void shouldValidateResponseTimeWithDelay(int delay) {
        long startTime = System.currentTimeMillis();
        
        given()
                .header("x-api-key", API_KEY)
                .queryParam("delay", delay)
                .when()
                .get(BASE_URL + "/users")
                .then()
                .statusCode(200)
                .time(lessThan(1000L));
        
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        System.out.println("Delay parameter: " + delay + " seconds");
        System.out.println("Response time: " + responseTime + " ms");
    }

    // Scenario_5: Login user without password
    @Test
    void shouldFailLoginWithoutPassword() {
        given()
                .header("x-api-key", API_KEY)
                .contentType("application/json")
                .body("{ \"email\": \"eve.holt@reqres.in\" }")
                .when()
                .post(BASE_URL + "/login")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }
}
