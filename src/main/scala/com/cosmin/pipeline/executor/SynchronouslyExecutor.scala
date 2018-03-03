package com.cosmin.pipeline.executor

import com.cosmin.pipeline.Stage

import scala.concurrent.Future

class SynchronouslyExecutor[In, Out] extends PipelineExecutor[In, Out] {

  override def execute(in: In, stages: List[Stage]): Future[Out] = {
    //@ToDo - rewrite this
    var out: Any = null
    var input: Any = in
    stages.reverse.foreach(stage => {
      out = stage.execute(input.asInstanceOf[stage.In])
      input = out
    })

    Future.successful[Out](out.asInstanceOf[Out])
  }
}

object SynchronouslyExecutor {
  def apply[In, Out](): SynchronouslyExecutor[In, Out] = new SynchronouslyExecutor()
}
