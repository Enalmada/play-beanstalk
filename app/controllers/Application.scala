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


}
