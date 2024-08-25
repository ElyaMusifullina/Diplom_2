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
import praktikum.steps.OrderSteps;
import praktikum.steps.UserSteps;

public class OrderGetTests extends BaseTest {
  private final OrderSteps orderSteps = new OrderSteps();
  private final UserSteps userSteps = new UserSteps();
  private String accessToken;

  @Before
  public void setUp() {
    User user = new User();
    user.setEmail(RandomStringUtils.randomAlphabetic(6) +  "@example.com");
    user.setPassword(RandomStringUtils.randomAlphabetic(6));
    user.setName(RandomStringUtils.randomAlphabetic(6));
    userSteps.createUser(user);
    accessToken = userSteps.loginUser(user).extract().body().path("accessToken");
  }

  @Test
  @DisplayName("[200] Get user's orders with authorization")
  public void shouldReturn200WithAuthorization() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

    orderSteps
      .getUserOrders(accessToken)
      .statusCode(200)
      .body("success", is(true));
  }

  @Test
  @DisplayName("[401] Get user's orders without authorization")
  public void shouldReturn401WithoutAuthorization() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

    orderSteps
      .getUserOrders(null)
      .statusCode(401)
      .body("success", is(false))
      .body("message", is("You should be authorised"));
  }

  @After
  public void tearDown() {
    if (accessToken != null) {
      userSteps.deleteUser(accessToken);
    }
  }
}
