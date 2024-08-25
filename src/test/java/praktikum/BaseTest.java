package praktikum;

import static praktikum.config.RestConfig.HOST;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.junit.Before;

public abstract class BaseTest {

  @Before
  public void setUpRestAssured() {
    RestAssured.requestSpecification = new RequestSpecBuilder()
      .setContentType(ContentType.JSON)
      .setBaseUri(HOST)
      .build();
  }
}
