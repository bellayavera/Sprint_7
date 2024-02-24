import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


import java.io.File;

public class CreateCourierTest extends Api {
    private Integer idCourier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check create courier")
    public void createNewCourierAndCheckResponse(){
        File jsonForCreate = new File("src/test/resources/createCourierPeach.json");
        File jsonForLogin = new File("src/test/resources/loginCourierPeach.json");

        Response responseCreate = createCourier(jsonForCreate);

        responseCreate.then().assertThat().body("ok", equalTo(true))
            .and()
            .statusCode(201);

        Response responseLogin = loginCourier(jsonForLogin);

        responseLogin.then().assertThat().body("id", notNullValue())
            .and()
            .statusCode(200);

        idCourier = responseLogin.jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Check create the same couriers")
    public void checkCreateSameCouriersIsFail(){
        File jsonForCreate = new File("src/test/resources/createCourierPeach.json");
        File jsonForLogin = new File("src/test/resources/loginCourierPeach.json");

        Response responseCreate = createCourier(jsonForCreate);

        responseCreate.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);

        Response responseLogin = loginCourier(jsonForLogin);

        responseLogin.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);

        idCourier = responseLogin.jsonPath().getInt("id");

        Response responseCreateSameCourier = createCourier(jsonForCreate);

        responseCreateSameCourier.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @Test
    @DisplayName("Check create courier without Pass")
    public void checkCreateCourierWithoutPassIsFail(){
        File jsonForCreate = new File("src/test/resources/createCourierWithoutPass.json");
        File jsonForLogin = new File("src/test/resources/loginCourierWithoutPass.json");

        Response responseCreate = createCourier(jsonForCreate);

        responseCreate.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);

        Response responseLogin = loginCourier(jsonForLogin);

        responseLogin.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Check create courier without login")
    public void checkCreateCourierWithoutLoginIsFail(){
        File jsonForCreate = new File("src/test/resources/createCourierWithoutLogin.json");
        File jsonForLogin = new File("src/test/resources/loginCourierWithoutLogin.json");

        Response responseCreate = createCourier(jsonForCreate);

        responseCreate.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);

        Response responseLogin = loginCourier(jsonForLogin);

        responseLogin.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @After
    public void courierDeletion(){
        if (idCourier != null) {
            RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
            given()
                .header("Content-type", "application/json")
                    .expect().statusCode(200)
                .when()
                .delete("/api/v1/courier/"+ idCourier);
        }
    }
}
