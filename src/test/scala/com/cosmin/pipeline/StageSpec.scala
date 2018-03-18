package com.cosmin.pipeline

class StageSpec extends UnitSpec {
  test("Text execute stage filter") {
    val intToStringFilter: Filter[Int, String] = new Filter[Int, String] {
      override def execute = i => i.toString
    }
    val stage = Stage(intToStringFilter)

    assertResult("1") (stage.execute(1.asInstanceOf[stage.In]))
  }
}
