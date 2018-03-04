package com.cosmin.pipeline

import com.cosmin.pipeline.executor.{PipelineExecutor, SynchronouslyExecutor}

import scala.util.Try

trait Pipeline[In, Out] {
  val stages: List[Stage]

  def | [X](f: Out => X): Pipeline[In, X] = pipe(f)

  def pipe[X](f: Out => X): Pipeline[In, X] = {
    val stage = Stage[Out, X](Filter[Out, X](f))

    Pipeline[In, X]( stage :: stages reverse)
  }

  def | [X](filter: Filter[Out, X]): Pipeline[In, X] = pipe(filter)

  def pipe[X](filter: Filter[Out, X]): Pipeline[In, X] = {
    val stage = Stage[Out, X](filter)
    
    Pipeline[In, X](stage :: stages reverse)
  }

  def execute(in: In)(onComplete: Try[Out] => Unit)(implicit pipelineExecutor: PipelineExecutor[In, Out] = SynchronouslyExecutor()): Unit = {
    pipelineExecutor.execute(in, stages) (onComplete)
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