package praktikum.steps;

import static io.restassured.RestAssured.given;
import static praktikum.EndPoints.INGREDIENTS;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class IngredientSteps {
  @Step("Get ingredients")
  public ValidatableResponse getIngredients() {
    return given()
      .when()
      .get(INGREDIENTS)
      .then();
  }

}
