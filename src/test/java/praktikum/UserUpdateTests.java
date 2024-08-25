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

public class UserUpdateTests extends BaseTest {
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
  @DisplayName("[200] Update user's parameters")
  public void shouldReturn200() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    String accessToken;
    secondUser = new User();
    secondUser.setEmail(RandomStringUtils.randomAlphabetic(6) +  "@example.com");
    secondUser.setName(RandomStringUtils.randomAlphabetic(6));

    userSteps
      .createUser(firstUser);

    accessToken = userSteps.loginUser(firstUser).extract().body().path("accessToken");

    userSteps
      .changeUser(secondUser, accessToken)
      .statusCode(200)
      .body("success", is(true));

    firstUser.setEmail(secondUser.getEmail());
    firstUser.setName(secondUser.getName());
  }

  @Test
  @DisplayName("[401] Update user's parameters without authorization")
  public void shouldReturn401() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    secondUser = new User();
    secondUser.setEmail(RandomStringUtils.randomAlphabetic(6) +  "@example.com");
    secondUser.setName(RandomStringUtils.randomAlphabetic(6));

    userSteps
      .createUser(firstUser);

    userSteps
      .changeUser(secondUser, null)
      .statusCode(401)
      .body("success", is(false))
      .body("message", is("You should be authorised"));
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