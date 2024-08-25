package praktikum.steps;

import static io.restassured.RestAssured.given;
import static praktikum.EndPoints.USER;
import static praktikum.EndPoints.USER_CREATE;
import static praktikum.EndPoints.USER_LOGIN;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.model.User;

public class UserSteps {

  @Step("Create user")
  public ValidatableResponse createUser(User user) {
    return given()
      .body(user)
      .when()
      .post(USER_CREATE)
      .then();
  }

  @Step("Login user")
  public ValidatableResponse loginUser(User user) {
    return given()
      .body(user)
      .when()
      .post(USER_LOGIN)
      .then();
  }

  @Step("Delete user")
  public void deleteUser(String accessToken) {
    given()
      .header("Authorization", accessToken)
      .when()
      .delete(USER)
      .then();
  }

  @Step("Change user's parameter")
  public ValidatableResponse changeUser(User user, String accessToken) {
      return given()
        .header("Authorization", accessToken != null ? accessToken : "")
        .body(user)
        .when()
        .patch(USER)
        .then();
  }
}
