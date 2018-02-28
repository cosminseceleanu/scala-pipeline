package com.cosmin.examples.filters

import com.cosmin.pipeline.Filter

class ToString extends Filter[Int, String] {
  override def execute: Int => String = i => s"$i"
}

object ToString {
  def apply(): ToString = new ToString()
}