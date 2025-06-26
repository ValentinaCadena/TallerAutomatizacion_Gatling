package Demo

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import Demo.Data._
import Demo.LoginTest._

class ContactsTest extends Simulation {

  val httpConf = http.baseUrl(url)
    .acceptHeader("application/json")
    .header("Authorization", "Bearer " + authToken)
    .check(status.is(200))

  val scn = scenario("Contacts")
    .exec(
      http("get contacts")
        .get("contacts")
        .check(status.is(200))
        .check(jsonPath("$.contacts").exists)
    )

  setUp(
    scn.inject(rampUsersPerSec(5).to(15).during(30))
  ).protocols(httpConf)

}
