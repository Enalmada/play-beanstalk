package controllers

import javax.inject.Inject

import anorm._
import play.api.Play.current
import play.api.db.DB
import play.api.mvc.{Action, Controller}


class Application @Inject() extends Controller {

  def index = Action {

    // Just something simple to make sure my db is connected
    // If it is connected, then env overrides must be working
    val ids: Seq[String] = if (play.api.Play.current.configuration.getBoolean("db.default.url").nonEmpty) {
      DB.withConnection { implicit connection => SQL"""
        SELECT table_name
        FROM information_schema.tables
      """.as(SqlParser.scalar[String].*)
      }
    } else Seq()

    val mySetting = current.configuration.getString("my.setting").get

    Ok(views.html.index(s"Your new application is ready. my.setting: $mySetting $ids"))

  }


  /**
    * Added to do a quick db query to be sure we have actually connected to the database
    * to be "healthy"
    */
  def dbCheck: Int = {
    /*
    transaction {
      val s = Session.currentSession
      val st = s.connection.prepareStatement("SELECT 1")
      val rs = st.executeQuery
      try {
        rs.getRow
      }
      finally {
        rs.close()
        st.close()
      }
    }
    */
    1
  }

  /**
    * Health check handler for Amazon load balancer.  Hits the database since we once has servers up without db connections.
    *
    * @return OK
    */
  def health = Action {

    dbCheck

    Ok("Healthy").withHeaders(CACHE_CONTROL -> "no-cache, no-store, must-revalidate",
      PRAGMA -> "no-cache",
      EXPIRES -> "0")
  }

  /**
    * Respond to head requests for health checking
    *
    * @return OK
    */
  def healthHead = Action {
    dbCheck
    Ok
  }


}
