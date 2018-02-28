package com.cosmin.pipeline

class SynchronouslyExecutor[In, Out] extends PipelineExecutor[In, Out] {
  override def execute(in: In, stages: List[Stage]): Out = {
    //@ToDo - rewrite this
    var out: Any = null
    var input: Any = in
    stages.reverse.foreach(stage => {
      out = stage.filter.execute(input.asInstanceOf[stage.In])
      input = out
    })

    out.asInstanceOf[Out]
  }
}

object SynchronouslyExecutor {
  def apply[In, Out](): SynchronouslyExecutor[In, Out] = new SynchronouslyExecutor()
}
