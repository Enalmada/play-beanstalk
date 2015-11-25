package controllers.jobs

import javax.inject.Inject

import play.api.Logger
import play.api.mvc.{Action, Controller}

class Job @Inject() extends Controller {

  // Test out a basic worker tier job.
  def backup = Action {

    Logger.info("ran backup job")
    Ok(views.html.index(s"Backup job completed"))
  }

}
