package com.cosmin.examples.wordcount

import com.cosmin.pipeline.Filter

class Grep(word: String) extends Filter[Seq[String], Seq[String]] {
  override def execute: Seq[String] => Seq[String] = lines => lines.filter(_.contains(word))
}

object Grep {
  def apply(word: String): Grep = new Grep(word)
}
