package com.cosmin.examples.filters

import com.cosmin.pipeline.Filter

class PlusOne extends Filter[Int, Int] {
  override def execute: Int => Int = (i: Int) => i + 1
}

object PlusOne {
  def apply(): PlusOne = new PlusOne()
}
