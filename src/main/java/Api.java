import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.io.File;

import static io.restassured.RestAssured.given;

public class Api {

    @Step("Send POST request to /api/v1/courier")
    public Response createCourier(File json){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");
    }
    @Step("Send POST request to /api/v1/courier/login")
    public Response loginCourier(File json){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");
    }
}
