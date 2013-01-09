package thirtytwo.degrees.halfpipe

/**
 * User: spencergibb
 * Date: 10/5/12
 * Time: 2:07 PM
 */
abstract class ScalaApplication[C, VC] extends Application[C, VC] {

  final def main(args: Array[String]) {
    run(args)
  }

}
