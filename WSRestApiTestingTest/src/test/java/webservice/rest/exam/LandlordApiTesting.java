package webservice.rest.exam;

import static com.jayway.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.apache.tools.ant.taskdefs.condition.Matches;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;



public class LandlordApiTesting {

	public static String id;

	@BeforeTest
	public void setUpUrl() {

		RestAssured.baseURI="http://localhost:8080";
		RestAssured.basePath="landlords";
		
	}
	
	
	@Test(priority=1,enabled=false)
	public void getLandlord() {
		Response res =  given()
				.when()
				.get();
		res.then()
		.statusCode(200)
		.log().all();
		assertEquals(true, res.getBody().as(List.class).isEmpty());
	}

@DataProvider
 public String[][] getData(){
	
	String[][] data = new String[][] {
	{"anna","sa","false"}};
	
     return data;
 }

@Test(dataProvider="getData",enabled=true)
public void PostLandlorddataProvider(String firstName, String lastName, String trusted ) {
	//Landlord landdata = new Landlord("anna", "sa", false);
	
	Response res = given()
			.contentType(ContentType.JSON)
			.body(firstName)
			.body(lastName)
			.body(trusted)
			.when()
			.post();
	id = res.body().path("id");
	res.then()
	//.assertThat().body(matchesJsonSchemaInClasspath("LandlordSchema.json"))
	//.statusCode(201)
	.log().all();
	res.body().jsonPath();
	assertEquals("anna", res.body().path("firstName"));
	assertEquals("sa", res.body().path("lastName"));
}
	
@Test(priority=2,enabled=false)
	public void PostLandlord() {
		Landlord landdata = new Landlord("anna", "sa", false);
		Response res = given()
				.contentType(ContentType.JSON)
				.body(landdata)
				.when()
				.post();
		id = res.body().path("id");
		res.then()
		.assertThat().body(matchesJsonSchemaInClasspath("LandlordSchema.json"))
		.statusCode(201)
		.log().all();
		assertEquals("anna", res.body().path("firstName"));
		assertEquals("sa", res.body().path("lastName"));
	}


	@Test(priority=3,enabled=false)
	public void PostLandlord2() {
		Landlord landdata2 = new Landlord("john", "KL", false);
		Response res = given()
				.contentType(ContentType.JSON)
				.body(landdata2)
				.when()
				.post();
		id = res.body().path("id");
		res.then()
		.statusCode(201)
		.extract().response().prettyPrint();
		List<?> list =res.body().path("apartments");
		assertEquals(true, list.isEmpty());
	}

	@Test(priority=5,enabled=false)
	public void putLandlord() {
		Landlord landdata3 = new Landlord("antony", "KL", false);
		Response res = given()
				.contentType(ContentType.JSON)
				.pathParam("id", id)
				.body(landdata3)
				.when()
				.put("{id}");
		res.then()
		.assertThat().body(matchesJsonSchemaInClasspath("MessageSchema.json"))
		.statusCode(200)
		.log().all();
		String msg= res.body().path("message");
		assertEquals("LandLord with id: "+id+" successfully updated", msg);	         
	}


	@Test(priority=6,enabled=false)
	public void putLandlord2() {
		Landlord landdata3 = new Landlord("saara", "KL", false);
		Response res = given()
				.contentType(ContentType.JSON)
				.pathParam("id", "4DKlS34")
				.body(landdata3)
				.when()
				.put("{id}");
		res.then()
		.statusCode(404)
		.log().all();
		String msg= res.body().path("message");
		assertEquals("There is no LandLord with id: 4DKlS34",msg);	         
	}

	@Test(priority=4,enabled=false)
	public void getLandlordById() {
		Response res =given()
				.pathParam("id", id)
				.contentType(ContentType.JSON)
				.when()
				.get("{id}");
		res.then()
		.statusCode(200)
		.log().all();
		assertEquals("john", res.body().path("firstName"));
		assertEquals("KL", res.body().path("lastName"));
		assertEquals(id, res.body().path("id"));
	}

}
