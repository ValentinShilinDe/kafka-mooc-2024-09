package Akka1

import Akka1.AkkaMain3.change_behaviour.WorkerProtocol
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, Props, SpawnProtocol}
import akka.into_actor.behaviour_factory_method
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.Future
import scala.language.{existentials, postfixOps}
import akka.actor.typed.scaladsl.AskPattern.{Askable, schedulerFromActorSystem}


object AkkaMain {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem[String](behaviour_factory_method.Echo(), "Echo")
    system ! "Hello"
    Thread.sleep(1000)
    system.terminate()
  }
}

//2 root aktor
object AkkaMain2 {
  object  Superviser{
    def apply(): Behavior[SpawnProtocol.Command] = Behaviors.setup{ctx =>
      ctx.log.info(ctx.self.toString)
      SpawnProtocol()
    }
  }

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem[SpawnProtocol.Command](Superviser(), "echo")
    implicit val ec = system.executionContext
    implicit val timeout = Timeout(3 seconds)

    val echo: Future[ActorRef[String]] = system.ask(
      SpawnProtocol.Spawn(behaviour_factory_method.Echo(), "Echo", Props.empty,_)
    )

    for (ref <- echo)
      ref ! "hello from ask"
  }
}


//3 change state
object AkkaMain3 {
  object change_behaviour{
    sealed trait WorkerProtocol
    object  WorkerProtocol {
      case object Start extends WorkerProtocol
      case object Stop extends WorkerProtocol
      case object StandBy extends WorkerProtocol
    }
    import  WorkerProtocol._

    def apply(): Behavior[WorkerProtocol] = idle()
    def idle(): Behavior[WorkerProtocol] = Behaviors.setup{ctx =>
      Behaviors.receiveMessage{
        case msg@Start =>
          ctx.log.info(msg.toString())
          workInProgress()
        case msg@StandBy =>
          ctx.log.info(msg.toString())
          idle()
        case msg@Stop =>
          ctx.log.info(msg.toString())
          Behaviors.stopped
      }
    }

    def workInProgress(): Behavior[WorkerProtocol] = Behaviors.setup{ctx =>
      Behaviors.receiveMessage{
        case msg@Start => Behaviors.unhandled
        case msg@StandBy =>
          ctx.log.info("go to standby")
          idle()
        case msg@Stop =>
          ctx.log.info("stopped")
          Behaviors.stopped
      }
    }
  }
}

object AkkaMain3_execute {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem[WorkerProtocol](AkkaMain3.change_behaviour(), "Echo")
    system ! AkkaMain3.change_behaviour.WorkerProtocol.Start
    Thread.sleep(100)
    system ! AkkaMain3.change_behaviour.WorkerProtocol.StandBy
    Thread.sleep(100)
    system ! AkkaMain3.change_behaviour.WorkerProtocol.Stop
    Thread.sleep(100)

    Thread.sleep(2000)
    system.terminate()

  }
}