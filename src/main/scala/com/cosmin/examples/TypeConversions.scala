package com.cosmin.examples

import com.cosmin.pipeline.{Filter, Pipeline}

import scala.util.Success

/**
  * Pipeline who receive a random int --> calculate sqrt --> create a message with sqrt result
  * Int -> Double -> String
  */
object TypeConversions {
  def main(args: Array[String]): Unit = {
    val pipeline = Pipeline[Int, Int]() | new Sqrt | (sqrt => s"Sqrt: $sqrt!")

    pipeline.execute(4) {
      case Success(output) => println(output)
    }
  }
}

class Sqrt extends Filter[Int, Double] {
  override def execute: Int => Double = input => Math.sqrt(input)
}
