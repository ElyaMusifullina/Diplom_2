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

public class UserLoginTests extends BaseTest {
  private final UserSteps userSteps = new UserSteps();
  private User firstUser;
  private User secondUser;

  @Before
  public void setUp() {
    firstUser = new User();
    firstUser.setEmail(RandomStringUtils.randomAlphabetic(6) +  "@example.com");
    firstUser.setPassword(RandomStringUtils.randomAlphabetic(6));
    firstUser.setName(RandomStringUtils.randomAlphabetic(6));
  }

  @Test
  @DisplayName("[200] Login user")
  public void shouldReturn200() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    userSteps
      .createUser(firstUser);

    firstUser.setName(null);

    userSteps
      .loginUser(firstUser)
      .statusCode(200)
      .body("success", is(true));
  }

  @Test
  @DisplayName("[401] Login user with incorrect email")
  public void shouldReturn401IncorrectEmail() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    secondUser = new User();

    userSteps
      .createUser(firstUser);

    secondUser.setEmail(RandomStringUtils.randomAlphabetic(6) +  "@example.com");
    secondUser.setPassword(firstUser.getPassword());

    userSteps
      .loginUser(secondUser)
      .statusCode(401)
      .body("success", is(false))
      .body("message", is("email or password are incorrect"));
  }

  @Test
  @DisplayName("[401] Login user with incorrect password")
  public void shouldReturn401IncorrectPassword() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    secondUser = new User();
    userSteps
      .createUser(firstUser);

    secondUser.setEmail(firstUser.getEmail());
    secondUser.setPassword(RandomStringUtils.randomAlphabetic(6));

    userSteps
      .loginUser(secondUser)
      .statusCode(401)
      .body("success", is(false))
      .body("message", is("email or password are incorrect"));
  }

  @After
  public void tearDown() {
    String accessToken = userSteps.loginUser(firstUser)
      .extract().body().path("accessToken");
    if (accessToken != null) {
      userSteps.deleteUser(accessToken);
    }
  }
}