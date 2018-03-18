package com.cosmin.pipeline

import scala.util.{Failure, Success}

class PipelineSpec extends UnitSpec {
  test("Test building pipeline") {
    val pipeline: Pipeline[String, String] = createDefaultPipeline

    assert(2 == pipeline.stages.size)
    assertResult("input->f2") {
      val stage = pipeline.stages.head
      stage.execute("input".asInstanceOf[stage.In])
    }
  }

  test("Test successful execution of a pipeline") {
    val pipeline: Pipeline[String, String] = createDefaultPipeline

    pipeline.execute("msg") {
      case Success(out) => assert("msg->f1->f2".equals(out))
      case Failure(_) => fail("Pipeline execution expected be successful")
    }
  }

  test("Test failed execution of a pipeline") {
    val pipeline: Pipeline[String, String] = createDefaultPipeline | (s => throw new RuntimeException)

    pipeline.execute("msg") {
      case Success(out) => fail("Pipeline execution expected fail")
      case Failure(e) => assert(e.isInstanceOf[RuntimeException])
    }
  }


  private def createDefaultPipeline = {
    val firstFilter: String => String = s => s"$s->f1"
    val secondFilter: String => String = s => s"$s->f2"

    Pipeline[String, String]() | firstFilter | secondFilter
  }
}
