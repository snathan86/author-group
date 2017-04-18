package medline.articles


import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfterAll
import org.scalatest.prop.Checkers

class AuthorGroupTest extends FunSuite with BeforeAndAfterAll  with Checkers {

  val logger = LogManager.getLogger(this.getClass.getName)

  /**
    * Generate test data using ScalaCheck Generators, this is very useful in generating
    * any number of all possible inputs randomly.
    */
  class SparkTestingGenerators extends Properties("Spark Job Steps") {
    //TODO: generate test data
    //TODO: use holdenkarau's spark test library to generate DF, RDD and mock spark specific objects
    //"com.holdenkarau" %% "spark-testing-base" % "2.1.0_0.6.0"

  }

  //Repeat testcases similar to this to all the functions
  test("check combineAuthors") {
    //TODO: get arbitrary input from  SparkTestingGenerators
    //call combineAuthors with generated input
    //validate result against input
     true
  }



}