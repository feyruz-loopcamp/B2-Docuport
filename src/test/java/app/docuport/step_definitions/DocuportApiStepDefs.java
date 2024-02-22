package app.docuport.step_definitions;

import app.docuport.utilities.ConfigurationReader;
import app.docuport.utilities.DocuportApiUtil;
import app.docuport.utilities.Environment;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DocuportApiStepDefs {

    public static final Logger LOG = LogManager.getLogger();
    String baseUrl = Environment.BASE_URL;

    Response response;
    String accessToken;
    @Given("User logged in to Docuport api as advisor role")
    public void user_logged_in_to_Docuport_api_as_advisor_role() {
//        String email;
//        String password;
//        if (role.equals("advisor")) {
//             email = Environment.ADVISOR_EMAIL;
//             password = Environment.ADVISOR_PASSWORD;
//        } else if ( role.equals("employee")) {
//             email = Environment.EMPLOYEE_EMAIL;
//             password = Environment.EMPLOYEE_PASSWORD;
//        }

        String email = Environment.ADVISOR_EMAIL;
        String password = Environment.ADVISOR_PASSWORD;
        LOG.info("Authorizing advisor role: email: " + email,  ", password: " + password);
        LOG.info(("Environment Base Url: " + baseUrl));

        accessToken = DocuportApiUtil.getAccessToken(email, password);


        if (accessToken == null) {
            LOG.error("Could not authorize the user in server");
            // The one below is in JUnit as a method which also show error
            fail("Could not authorize the user in server");
        } else {
            LOG.info("Access token: " + accessToken);
        }
    }

    @Given("User sends GET request to {string} with query param {string} email address")
    public void user_sends_GET_request_to_with_query_param_email_address(String endpoint, String param) {
        String email;
//        if (param.equals("advisor")) {
//            email = Environment.ADVISOR_EMAIL;
//        } else {
//            email = null;
//        }
        switch (param) {
            case "advisor":
                email = Environment.ADVISOR_EMAIL;
                break;
            case "employee":
                email = Environment.EMPLOYEE_EMAIL;
            default:
                email = null;
        }
        // this switch case can continue for all the roles

       response = given().accept(ContentType.JSON)
                .and().header("Authorization", accessToken)
                .and().queryParam("EmailAddress", email)
                .when().get(baseUrl + endpoint);


    }

    @Then("status code should be {int}")
    public void status_code_should_be(int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.statusCode());
        response.then().statusCode(expectedStatusCode); // this is doing same as above
    }

    @Then("content type is {string}")
    public void content_type_is(String expContentType) {
        assertEquals(expContentType, response.contentType());
    }

    @Then("role is {string}")
    public void role_is(String expRoleName) {

        assertEquals(expRoleName, response.path("items[0].roles[0].name"));
    }

}
