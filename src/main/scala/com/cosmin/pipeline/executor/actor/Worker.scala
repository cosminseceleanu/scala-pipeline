package com.cosmin.pipeline.executor.actor

import akka.actor.{Actor, Props}
import com.cosmin.pipeline.Stage
import com.cosmin.pipeline.executor.actor.Supervisor.StageCompleted
import com.cosmin.pipeline.executor.actor.Worker.Execute

object Worker {
  def props(stage: Stage): Props = Props(new Worker(stage))

  final case class Execute[In](in: In)
}

class Worker(stage: Stage) extends Actor {
  override def receive: Receive = {
    case Execute(input) =>
      val result = stage.execute(input.asInstanceOf[stage.In])
      sender() ! StageCompleted[stage.Out](stage, result.asInstanceOf[stage.Out])
  }
}
