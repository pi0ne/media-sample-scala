package controllers

import javax.inject.Inject
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class AuthenticatedAccountAction @Inject()(parser: BodyParsers.Default)(
    implicit ec: ExecutionContext)
    extends ActionBuilderImpl(parser) {

  private val logger = play.api.Logger(this.getClass)

  override def invokeBlock[A](request: Request[A],
                              block: (Request[A]) => Future[Result]) = {
    logger.info("ENTERED AuthenticatedAccountAction::invokeBlock ...")
    val maybeAccountId =
      request.session.get(models.Global.SESSION_ACCOUNTID_KEY)
    maybeAccountId match {
      case None => {
        Future.successful(Forbidden("Dude, you’re not logged in."))
      }
      case Some(u) => {
        val res: Future[Result] = block(request)
        res
      }
    }
  }
}
