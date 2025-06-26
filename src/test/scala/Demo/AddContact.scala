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
      .post(s"addContacts")
      .body(StringBody(
        s"""{
        "firstName": "Valentina",
        "lastName": "Cadena",
        "email": "valentina${System.currentTimeMillis()}@mail.com"
        }""")).asJson
       //Validar status 200 del servicio
      .check(status.is(201))
      .check(jsonPath("$._id").exists)
    )

  // 3 Load Scenario
  setUp(
    scn.inject(rampUsersPerSec(10).to(150).during(30))
  ).protocols(httpConf);
}
