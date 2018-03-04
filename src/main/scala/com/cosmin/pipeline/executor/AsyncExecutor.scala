package com.cosmin.pipeline.executor
import com.cosmin.pipeline.Stage

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class AsyncExecutor[In, Out] (implicit executor: ExecutionContext) extends PipelineExecutor[In, Out] {
  override def execute(in: In, stages: List[Stage])(onComplete: Try[Out] => Unit): Unit = {
    var out: Future[_] = Future.successful[In](in)
    println("execute pipeline with async executor")

    stages.reverse.foreach(stage => {
      //@ToDo - rewrite this
      out = out.map[stage.Out](input => stage.execute(input.asInstanceOf[stage.In]))
    })

    out.asInstanceOf[Future[Out]].onComplete(onComplete)
  }
}

object AsyncExecutor {
  def apply[In, Out]: AsyncExecutor[In, Out] = new AsyncExecutor() (scala.concurrent.ExecutionContext.Implicits.global)
}
