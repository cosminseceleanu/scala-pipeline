package com.cosmin.pipeline.executor

import com.cosmin.pipeline.Stage

import scala.util.Try

trait PipelineExecutor[In, Out] {
  def execute(in: In, stages: List[Stage]) (onComplete: Try[Out] => Unit) : Unit
}
