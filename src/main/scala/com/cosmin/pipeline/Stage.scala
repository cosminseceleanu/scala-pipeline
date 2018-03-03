package com.cosmin.pipeline

trait Stage {
  type In
  type Out

  val filter: Filter[In, Out]

  def execute(in: In): Out = filter.execute(in)
}

object Stage {
  def apply[I, O](f: Filter[I, O]): Stage = new Stage() {
    override type In = I
    override type Out = O
    override val filter: Filter[In, Out] = f
  }
}