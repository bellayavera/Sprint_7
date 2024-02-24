import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest extends Api{
    private Integer idCourier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check successful login courier")
    public void loginCourierAndCheckResponse(){
        File jsonForCreate = new File("src/test/resources/createCourierPeach.json");
        File jsonForLogin = new File("src/test/resources/loginCourierPeach.json");

        Response responseCreate = createCourier(jsonForCreate);

        Response responseLogin = loginCourier(jsonForLogin);

        responseLogin.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);

        idCourier = responseLogin.jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Check unsuccessful login nonexistent courier")
    public void checkLoginNonexistentCourierIsFail(){
        File jsonForLogin = new File("src/test/resources/loginCourierNonexistent.json");

        Response responseLogin = loginCourier(jsonForLogin);

        responseLogin.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    @DisplayName("Check unsuccessful login courier with incorrect pass")
    public void checkLoginCourierWithIncorrectPass(){
        File jsonForCreate = new File("src/test/resources/createCourierPeach.json");
        File jsonForIncorrectLogin = new File("src/test/resources/loginCourierPeachWithIncorrectPass.json");
        File jsonForCorrectLogin = new File("src/test/resources/loginCourierPeach.json");

        Response responseCreate = createCourier(jsonForCreate);

        Response responseIncorrectLogin = loginCourier(jsonForIncorrectLogin);

        responseIncorrectLogin.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);

        Response responseCorrectLogin = loginCourier(jsonForCorrectLogin);

        idCourier = responseCorrectLogin.jsonPath().getInt("id");
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
