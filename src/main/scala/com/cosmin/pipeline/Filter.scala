package com.cosmin.pipeline

trait Filter[In, Out] {
  def execute: In => Out
}

object Filter {
  def apply[In, Out](f: In => Out): Filter[In, Out] = new Filter[In, Out] {
    override def execute: In => Out = f
  }
}
