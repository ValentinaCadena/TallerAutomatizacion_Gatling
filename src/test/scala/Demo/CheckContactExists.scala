package Demo

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import Demo.Data._

class CheckContactExistsTest extends Simulation{

  // 1 Http Conf
  val httpConf = http.baseUrl(url)
    .acceptHeader("application/json")
    .header("Authorization",s"Bearer $authToken")
    //Verificar de forma general para todas las solicitudes
    .check(status.is(200))

  // 2 Scenario Definition
  val scn = scenario("Check Contact Exists").
    exec(http("Get Contacts")
      .get(s"contacts")
      .check(status.is(200))
      .check(substring("Valentina"))
    )

  // 3 Load Scenario
  setUp(
    scn.inject(rampUsersPerSec(5).to(15).during(30))
  ).protocols(httpConf);
}


