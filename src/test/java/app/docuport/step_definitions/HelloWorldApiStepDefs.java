package app.docuport.step_definitions;

import app.docuport.utilities.ConfigurationReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class HelloWorldApiStepDefs {

    public static final Logger LOG = LogManager.getLogger();

    String url = ConfigurationReader.getProperty("hello.world.api");
    Response response;

    @Given("User sends get request to hello world api")
    public void user_sends_get_request_to_hello_world_api() {
        LOG.info("Sending GET request to Hello World API: " + url);

         response = given().accept(ContentType.JSON)
                .when().get(url);

         LOG.info("GET request completed with response: " + response.asString());
    }

    @Then("hello world api status code is {int}")
    public void hello_world_api_status_code_is(int expectedSC) {
        LOG.info("Actual status Code: " + response.statusCode());
        LOG.info("Expected status Code: " + expectedSC);
        assertEquals(expectedSC, response.statusCode());

    }

    @Then("hello world api response body contains {string}")
    public void hello_world_api_response_body_contains(String expectedMessage) {

        String actMessage = response.path("message");
        assertEquals(expectedMessage, actMessage);

    }


}
