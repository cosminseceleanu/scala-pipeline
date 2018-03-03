package com.cosmin.pipeline.executor

import com.cosmin.pipeline.Stage

import scala.concurrent.Future

trait PipelineExecutor[In, Out] {
  def execute(in: In, stages: List[Stage]): Future[Out]
}
