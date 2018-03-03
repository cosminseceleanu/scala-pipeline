package com.cosmin.pipeline

trait Pipeline[In, Out] {
  val stages: List[Stage]
  def | [X](f: Out => X): Pipeline[In, X] = pipe(f)

  def pipe[X](f: Out => X): Pipeline[In, X] = {
    Pipeline[In, X](Stage[Out, X](Filter[Out, X](f)) :: stages)
  }

  def | [X](filter: Filter[Out, X]): Pipeline[In, X] = pipe(filter)

  def pipe[X](filter: Filter[Out, X]): Pipeline[In, X] = {
    Pipeline[In, X](Stage[Out, X](filter) :: stages)
  }

  def execute(in: In)(implicit pipelineExecutor: PipelineExecutor[In, Out] = SynchronouslyExecutor()): Out = {
    pipelineExecutor.execute(in, stages)
  }
}

object Pipeline {
  def apply[In, Out](stagesList: List[Stage]): Pipeline[In, Out] = new Pipeline[In, Out] {
    override val stages: List[Stage] = stagesList
  }

  def apply[In, Out](): Pipeline[In, Out] = new Pipeline[In, Out] {
    override val stages: List[Stage] = List()
  }
}