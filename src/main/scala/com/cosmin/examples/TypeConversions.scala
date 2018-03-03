package com.cosmin.examples

import com.cosmin.pipeline.Pipeline

import scala.util.Random

/**
  * Pipeline who receive a random int --> calculate sqrt --> create a message with sqrt result
  * Int -> Double -> String
  */
object TypeConversions {
  def main(args: Array[String]): Unit = {
    val pipeline = Pipeline[Int, Int]() | (nr => Math.sqrt(nr)) | (sqrt => s"Sqrt: $sqrt!")
    val random = new Random()
    val input = Math.abs(random.nextInt())
    val out = pipeline.execute(input)
    println(s"input: $input --> $out")
  }
}
