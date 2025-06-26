package Demo

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import Demo.Data._

class InvalidContactTest extends Simulation{

  // 1 Http Conf
  val httpConf = http.baseUrl(url)
    .acceptHeader("application/json")
    .header("Authorization",s"Bearer $authToken")
    //Verificar de forma general para todas las solicitudes
    .check(status.is(200))

  // 2 Scenario Definition
  val scn = scenario("Invalid Contact").
    exec(http("addContact")
      .get(s"addContact")
      .body(StringBody(s"""{"firstName": "", "lastName": ""}""")).asJson
       //Validar status 200 del servicio
      .check(status.is(401))
      .check(bodyString.is("Contact validation failed: firstName: Path `firstName` is required., lastName: Path `lastName` is required."))
    )

  // 3 Load Scenario
  setUp(
    scn.inject(rampUsersPerSec(5).to(15).during(30))
  ).protocols(httpConf);
}
