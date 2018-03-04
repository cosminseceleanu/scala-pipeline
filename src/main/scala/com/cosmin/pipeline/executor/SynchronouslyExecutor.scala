package com.cosmin.pipeline.executor

import com.cosmin.pipeline.Stage

import scala.util.Try

class SynchronouslyExecutor[In, Out] extends PipelineExecutor[In, Out] {
  override def execute(in: In, stages: List[Stage])(onComplete: Try[Out] => Unit): Unit = {
    val result: Out = doExecute[In, Out](stages, in)

    onComplete(Try[Out](result))
  }

  def doExecute[I, O](stages: List[Stage], input: I): O = stages match {
    case Nil => input.asInstanceOf[O]
    case stage :: nextStage :: tail =>
      val stageOutput = stage.execute(input.asInstanceOf[stage.In])

      doExecute[stage.Out, nextStage.Out](nextStage :: tail, stageOutput).asInstanceOf[O]
    case stage :: tail =>
      val stageOutput = stage.execute(input.asInstanceOf[stage.In])

      doExecute[stage.Out, Out](tail, stageOutput).asInstanceOf[O]
  }
}

object SynchronouslyExecutor {
  def apply[In, Out](): SynchronouslyExecutor[In, Out] = new SynchronouslyExecutor()
}
