package com.cosmin.pipeline

trait PipelineExecutor[In, Out] {
  def execute(in: In, list: List[Stage]): Out
}
