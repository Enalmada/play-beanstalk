
import javax.inject.Inject

import play.api.http.HttpFilters
import play.api.mvc.EssentialFilter
import play.filters.cors.CORSFilter
import play.filters.gzip.GzipFilter
import play.filters.headers.SecurityHeadersFilter

class Filters @Inject()(securityHeadersFilter: SecurityHeadersFilter, corsFilter: CORSFilter) extends HttpFilters {

  // Beanstalk has nginx compression built in.  IF you don't use beanstalk, use nginx.  Don't use this, it adds massive latency and cuts req/sec according to my wrk testing.
  // https://github.com/playframework/playframework/blob/2.3.x/framework/src/play-filters-helpers/src/main/scala/play/filters/gzip/GzipFilter.scala
  val gzipFilterOpt: Option[EssentialFilter] = if (play.api.Play.current.configuration.getBoolean("filter.gzip.enable").getOrElse(false)) {
    // gzips everything (images, etc) which is probably not what you want.  Specify what is important to you.
    val gzippableTypes = Seq("text/html", "text/plain", "text/css", "application/json", "application/x-javascript", "text/xml", "application/xml", "application/xml+rss", "text/javascript")
    Some(new GzipFilter(shouldGzip = (request, response) => response.headers.get("Content-Type").exists(c => gzippableTypes.exists(c.startsWith))))
  } else None

  // https://www.playframework.com/documentation/2.4.x/SecurityHeaders
  val securityFilterOpt: Option[EssentialFilter] = if (play.api.Play.current.configuration.getBoolean("filter.security.enable").getOrElse(false)) {
    Some(securityHeadersFilter)
  } else None

  // https://www.playframework.com/documentation/2.4.x/CorsFilter
  val corsFilterOpt: Option[EssentialFilter] = if (play.api.Play.current.configuration.getBoolean("filter.cors.enable").getOrElse(false)) {
    Some(corsFilter)
  } else None

  def filters = Seq(securityFilterOpt, corsFilterOpt, gzipFilterOpt).flatten

}