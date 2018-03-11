package com.cosmin.pipeline.executor.actor

import akka.actor.{Actor, Props}
import com.cosmin.pipeline.Stage
import com.cosmin.pipeline.executor.actor.Supervisor.{StageCompleted, StageFailed}
import com.cosmin.pipeline.executor.actor.Worker.Execute

import scala.util.{Failure, Success, Try}

object Worker {
  def props(stage: Stage): Props = Props(new Worker(stage))

  final case class Execute[In](in: In)
}

class Worker(stage: Stage) extends Actor {
  override def receive: Receive = {
    case Execute(input) =>
       executeStage(input)
  }

  private def executeStage(input: Any): Unit = {
    Try[stage.Out](stage.execute(input.asInstanceOf[stage.In])) match {
      case Success(result) => sender() ! StageCompleted[stage.Out](stage, result.asInstanceOf[stage.Out])
      case Failure(e) => sender() ! StageFailed(stage, e)
    }
  }
}
