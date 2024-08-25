package praktikum.steps;

import static io.restassured.RestAssured.given;
import static praktikum.EndPoints.ORDER;
import com.google.gson.Gson;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class OrderSteps {
  private final Gson gson = new Gson();

  @Step("Create order")
  public ValidatableResponse createOrder(String accessToken, String[] ingredients) {
    CreateOrderRequest createOrderRequest = new CreateOrderRequest(ingredients);
    return given()
      .header("Authorization", accessToken != null ? accessToken : "")
      .body(gson.toJson(createOrderRequest))
      .when()
      .post(ORDER)
      .then();
  }

  @Step("Get user's orders")
  public ValidatableResponse getUserOrders(String accessToken) {
    return given()
      .header("Authorization", accessToken != null ? accessToken : "")
      .when()
      .get(ORDER)
      .then();
  }

  private static class CreateOrderRequest {
    private final String[] ingredients;
    public CreateOrderRequest(String[] ingredients) {
      this.ingredients = ingredients;
    }
  }
}
