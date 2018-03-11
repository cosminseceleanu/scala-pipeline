package com.cosmin.pipeline.executor.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.cosmin.pipeline.Stage
import com.cosmin.pipeline.executor.actor.Supervisor.{StageCompleted, Start}
import com.cosmin.pipeline.executor.actor.Worker.Execute

import scala.util.Try

object Supervisor {
  def props[Out](stages: List[Stage], onComplete: Try[Out] => Unit): Props = Props(new Supervisor(stages, onComplete))

  final case class Start[In](in: In)
  final case class StageCompleted[Out](stage: Stage, result: Out)
}

class Supervisor[Out](stages: List[Stage], onComplete: Try[Out] => Unit) extends Actor with ActorLogging {
  private var remainingStages = stages
  private var stageToActor: Map[Stage, ActorRef] = Map.empty

  override def preStart(): Unit = stageToActor = stages.map(stage => (stage, context.actorOf(Worker.props(stage)))).toMap

  override def receive: Receive = {
    case Start(in) => executeStage(in)
    case StageCompleted(stage, result) =>
      if (remainingStages.isEmpty) {
        onComplete(Try(result.asInstanceOf[Out]))
        context.stop(self)
      } else {
        executeStage(result)
      }
  }

  private def executeStage(in: Any): Unit = {
    val stage = remainingStages.head
    stageToActor(stage) ! Execute[stage.In](in.asInstanceOf[stage.In])
    remainingStages = remainingStages.tail
  }
}
