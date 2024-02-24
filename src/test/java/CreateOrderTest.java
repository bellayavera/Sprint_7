import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final List<String> color;

    public CreateOrderTest(
            String firstName,
            String lastName,
            String address,
            String metroStation,
            String phone,
            int rentTime,
            String deliveryDate,
            String comment,
            List<String> color
    ){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getJsonOrder() {
        return new Object[][] {
                {"Apple","Fruit","Moskovskaya","Arbatskaya","+7 800 355 35 35",48,"2024-01-01","No comments",List.of("BLACK", "GREY")},
                {"Banana","Fruit","Moskovskaya","Arbatskaya","+7 800 355 35 35",48,"2024-01-01","No comments",List.of("BLACK")},
                {"Kiwi","Fruit","Moskovskaya","Arbatskaya","+7 800 355 35 35",48,"2024-01-01","No comments",List.of("GREY")},
                {"Mango","Fruit","Moskovskaya","Arbatskaya","+7 800 355 35 35",48,"2024-01-01","No comments",List.of()},
        };
    }

    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new ErrorLoggingFilter());
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check create order")
    public void checkCreateOrder(){

        CreateOrder order = new CreateOrder(
                firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color
        );

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
        response.then().assertThat().body("track",notNullValue())
                .and()
                .statusCode(201);

        int idOrder = response.jsonPath().getInt("track");

        given()
                .get("/api/v1/orders/track?t=" + idOrder)
                .then().assertThat().body("order", notNullValue())
                .and()
                .statusCode(200);
    }
}
