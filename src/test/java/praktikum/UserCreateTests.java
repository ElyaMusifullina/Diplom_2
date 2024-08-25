package praktikum;

import static org.hamcrest.CoreMatchers.is;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.model.User;
import praktikum.steps.UserSteps;

public class UserCreateTests extends BaseTest {
  private final UserSteps userSteps = new UserSteps();
  private User user;

  @Before
  public void setUp() {
    user = new User();
    user.setEmail(RandomStringUtils.randomAlphabetic(6) +  "@example.com");
    user.setPassword(RandomStringUtils.randomAlphabetic(6));
    user.setName(RandomStringUtils.randomAlphabetic(6));
  }

  @Test
  @DisplayName("[200] Create new user with all parameters")
  public void shouldReturn200() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    userSteps
      .createUser(user)
      .statusCode(200)
      .body("success", is(true));
  }

  @Test
  @DisplayName("[403] Create user that already exists")
  public void shouldReturn403UserExists() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    userSteps
      .createUser(user);

    userSteps
      .createUser(user)
      .statusCode(403)
      .body("success", is(false))
      .body("message", is("User already exists"));
  }

  @Test
  @DisplayName("[403] Create user without name")
  public void shouldReturn403WithoutName() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    user.setName("");

    userSteps
      .createUser(user)
      .statusCode(403)
      .body("success", is(false))
      .body("message", is("Email, password and name are required fields"));
  }

  @Test
  @DisplayName("[403] Create user without password")
  public void shouldReturn403WithoutPassword() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    user.setPassword("");

    userSteps
      .createUser(user)
      .statusCode(403)
      .body("success", is(false))
      .body("message", is("Email, password and name are required fields"));
  }

  @Test
  @DisplayName("[403] Create user without email")
  public void shouldReturn403WithoutEmail() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    user.setEmail("");

    userSteps
      .createUser(user)
      .statusCode(403)
      .body("success", is(false))
      .body("message", is("Email, password and name are required fields"));
  }

  @After
  public void tearDown() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    String accessToken = userSteps.loginUser(user)
      .extract().body().path("accessToken");
    if (accessToken != null) {
      userSteps.deleteUser(accessToken);
    }
  }
}
