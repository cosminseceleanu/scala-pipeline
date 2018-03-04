package com.cosmin.examples.wordcount

import com.cosmin.pipeline.Filter

import scala.io.Source

class Cat() extends Filter[String, Seq[String]]{
  override def execute: String => Seq[String] = file => Source.fromFile(file).getLines.toSeq
}

object Cat {
  def apply(): Cat = new Cat()
}


