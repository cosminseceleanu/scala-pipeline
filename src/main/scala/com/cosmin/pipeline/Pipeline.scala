package com.cosmin.pipeline

class Pipeline[In, Out](stages: List[Stage]) {

  def | [X](f: Out => X): Pipeline[In, X] = pipe(f)

  def pipe[X](f: Out => X): Pipeline[In, X] = {
    new Pipeline[In, X](Stage[Out, X](Filter[Out, X](f)) :: stages)
  }

  def | [X](filter: Filter[Out, X]): Pipeline[In, X] = pipe(filter)

  def pipe[X](filter: Filter[Out, X]): Pipeline[In, X] = {
    new Pipeline[In, X](Stage[Out, X](filter) :: stages)
  }

  def execute(in: In)(implicit pipelineExecutor: PipelineExecutor[In, Out] = SynchronouslyExecutor()): Out = {
    pipelineExecutor.execute(in, stages)
  }
}