package akka

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}

object  into_actor {
  object behaviour_factory_method{
    object Echo {
      def apply(): Behavior[String] = Behaviors.setup{ctx =>
        Behaviors.receiveMessage{
          case msg =>
            ctx.log.info(msg)
            Behaviors.same
        }
      }
    }
  }
}


object  abstract_behaviour {
  object Echo {
    def apply(): Behavior[String] = Behaviors.setup{ctx =>
      new Echo(ctx)
    }
  }

  class Echo(ctx: ActorContext[String]) extends AbstractBehavior[String](ctx) {
    override def onMessage(msg: String): Behavior[String] = {
      ctx.log.info(msg)
      this
    }
  }
}