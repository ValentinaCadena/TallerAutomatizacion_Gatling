package Demo

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import Demo.Data._

class AddContactTest extends Simulation{

  // 1 Http Conf
  val httpConf = http.baseUrl(url)
    .acceptHeader("application/json")
    .header("Authorization",s"Bearer $authToken")
    //Verificar de forma general para todas las solicitudes
    .check(status.is(200))

  // 2 Scenario Definition
  val scn = scenario("Add Contact").
    exec(http("addContact")
      .get(s"addContacts")
      .body(StringBody(s"""{"firstName": "Valentina", "lastName": "Cadena"}""")).asJson
       //Validar status 200 del servicio
      .check(status.is(200))
      .check(jsonPath.saveAs("newContact"))
    )

  // 3 Load Scenario
  setUp(
    scn.inject(rampUsersPerSec(5).to(15).during(30))
  ).protocols(httpConf);
}
