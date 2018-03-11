package com.cosmin.pipeline.executor
import akka.actor.ActorSystem
import com.cosmin.pipeline.Stage
import com.cosmin.pipeline.executor.actor.Supervisor
import com.cosmin.pipeline.executor.actor.Supervisor.Start

import scala.util.Try

class AkkaExecutor[In, Out] extends PipelineExecutor[In, Out] {
  override def execute(in: In, stages: List[Stage])(onComplete: Try[Out] => Unit): Unit = {
    val system = ActorSystem("pipeline")
    val supervisor = system.actorOf(Supervisor.props[Out](stages, onComplete), "pipeline-supervisor")

    supervisor ! Start[In](in)
  }
}

object AkkaExecutor {
  def apply[In, Out]: AkkaExecutor[In, Out] = new AkkaExecutor[In, Out]
}
