package Demo

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import Demo.Data._

class LoginTest extends Simulation{

  // 1 Http Conf
  val httpConf = http.baseUrl(url)
    .acceptHeader("application/json")
    //Verificar de forma general para todas las solicitudes
    .check(status.is(200))

  // 2 Scenario Definition
  val scn = scenario("Login").
    exec(http("login")
      .get(s"users/login")
      .body(StringBody(s"""{"email": "$email", "password": "$password"}""")).asJson
       //Validar status 200 del servicio
      .check(status.is(200))
      .check(jsonPath("$.token").saveAs("authToken"))
    )

  // 3 Load Scenario
  setUp(
    scn.inject(rampUsersPerSec(10).to(100).during(20))
  ).protocols(httpConf);
}
