package com.cosmin.examples.wordcount

import com.cosmin.pipeline.Filter

class Count extends Filter[Seq[String], Int] {
  override def execute: Seq[String] => Int = lines => lines.count(_ => true)
}

object Count {
  def apply(): Count = new Count()
}
