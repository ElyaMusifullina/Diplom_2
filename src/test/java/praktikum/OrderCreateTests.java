package praktikum;

import static org.hamcrest.CoreMatchers.is;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.model.User;
import praktikum.steps.IngredientSteps;
import praktikum.steps.OrderSteps;
import praktikum.steps.UserSteps;

public class OrderCreateTests extends BaseTest {
  private final OrderSteps orderSteps = new OrderSteps();
  private final UserSteps userSteps = new UserSteps();
  private final IngredientSteps ingredientSteps = new IngredientSteps();
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
  @DisplayName("[200] Create order with authorization")
  public void shouldReturn200WithAuthorization() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    List<String> ingredients = ingredientSteps.getIngredients()
      .extract()
      .jsonPath()
      .getList("data.findAll { it._id != null }.shuffled().take(3)._id");

    orderSteps
      .createOrder(accessToken, ingredients.toArray(new String[0]))
      .statusCode(200)
      .body("success", is(true));
  }

  @Test
  @DisplayName("[200] Create order without authorization")
  public void shouldReturn200WithoutAuthorization() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    List<String> ingredients = ingredientSteps.getIngredients()
      .extract()
      .jsonPath()
      .getList("data.findAll { it._id != null }.shuffled().take(3)._id");

    orderSteps
      .createOrder(null, ingredients.toArray(new String[0]))
      .statusCode(200)
      .body("success", is(true));
  }

  @Test
  @DisplayName("[400] Create order without ingredients")
  public void shouldReturn400WithoutIngredients() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    orderSteps
      .createOrder(accessToken, new String[]{})
      .statusCode(400)
      .body("success", is(false))
      .body("message", is("Ingredient ids must be provided"));
  }

  @Test
  @DisplayName("[500] Create order with invalid ingredient's hash")
  public void shouldReturn500WithInvalidIngredientsHash() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    orderSteps
      .createOrder(accessToken, new String[]{"test_invalid_hash"})
      .statusCode(500);
  }

  @After
  public void tearDown() {
    if (accessToken != null) {
      userSteps.deleteUser(accessToken);
    }
  }
}