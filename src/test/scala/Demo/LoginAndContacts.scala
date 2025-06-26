package Demo

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import Demo.Data._

class LoginAndContactsTest extends Simulation {

  // --- Configuración HTTP base ---
  val httpConf = http.baseUrl(url)
    .acceptHeader("application/json")

  // --- Paso 1: Login Request ---
  val loginRequest = exec(
    http("Login")
      .get("users/login")
      .body(StringBody(s"""{"email": "$email", "password": "$password"}""")).asJson
      .check(status.is(200))
      .check(jsonPath("$.token").saveAs("authToken"))
  )

  // --- Paso 2: Obtener Contactos ---
  val getContacts = exec(
    http("Get Contacts")
      .get("contacts")
      .header("Authorization", "Bearer ${authToken}")
      .check(status.is(200))
      .check(jsonPath("$.contacts").exists)
  )

  // --- Escenario principal ---
  val scn = scenario("Login + Get Contacts")
    .exec(loginRequest)
    .pause(1)
    .exec(getContacts)

  setUp(
    scn.inject(rampUsersPerSec(5).to(15).during(30))
  ).protocols(httpConf)
}
