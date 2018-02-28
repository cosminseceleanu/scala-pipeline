package com.cosmin.examples.filters

import com.cosmin.pipeline.Filter

class HelloWorld extends Filter[String, String] {
  override def execute: String => String = s => s"Hello World! $s"
}

object HelloWorld {
  def apply(): HelloWorld = new HelloWorld()
}
