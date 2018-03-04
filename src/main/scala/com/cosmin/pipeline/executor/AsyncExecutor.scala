package com.cosmin.pipeline.executor
import com.cosmin.pipeline.Stage

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class AsyncExecutor[In, Out] (implicit executor: ExecutionContext) extends PipelineExecutor[In, Out] {
  override def execute(in: In, stages: List[Stage])(onComplete: Try[Out] => Unit): Unit = {
    val out: Future[Out] = doExecute[In, Out](stages, Future.successful[In](in))

    out.onComplete(onComplete)
  }

  def doExecute[I, O](stages: List[Stage], input: Future[I]): Future[O] = stages match {
    case Nil => input.asInstanceOf[Future[O]]
    case stage :: nextStage :: tail =>
      val stageOutput = input.map[stage.Out](i => stage.execute(i.asInstanceOf[stage.In]))

      doExecute[stage.Out, nextStage.Out](nextStage :: tail, stageOutput).asInstanceOf[Future[O]]
    case stage :: tail =>
      val stageOutput = input.map[stage.Out](i => stage.execute(i.asInstanceOf[stage.In]))

      doExecute[stage.Out, Out](tail, stageOutput).asInstanceOf[Future[O]]
  }
}

object AsyncExecutor {
  def apply[In, Out]: AsyncExecutor[In, Out] = new AsyncExecutor() (scala.concurrent.ExecutionContext.Implicits.global)
}
