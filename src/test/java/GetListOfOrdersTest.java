import com.google.gson.Gson;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class GetListOfOrdersTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check get list of orders")
    public void checkListOfOrders(){

        CreateOrder orderLemon = new CreateOrder("Lemon","Fruit","Moskovskaya","Arbatskaya","+7 800 355 35 35",48,"2024-01-01","No comments", List.of("GREY"));
        CreateOrder orderMelon = new CreateOrder("Melon","Fruit","Moskovskaya","Arbatskaya","+7 800 355 35 35",48,"2024-01-01","No comments", List.of("BLACK"));

        given()
                .header("Content-type", "application/json")
                .and()
                .body(orderLemon)
                .when()
                .post("/api/v1/orders");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(orderMelon)
                .when()
                .post("/api/v1/orders");

        given()
                .get("/api/v1/orders")
                .then().assertThat().body("orders.id", notNullValue())
                .and()
                .statusCode(200);

        GetOrders getOrders = given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders")
                .body().as(GetOrders.class);

        Assert.assertNotNull(getOrders.getOrders());
    }
}
