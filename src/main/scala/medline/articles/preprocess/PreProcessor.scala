package medline.articles.preprocess

import java.io.FileNotFoundException

import medline.articles.model.Article
import org.apache.logging.log4j.LogManager
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse
import java.io._

/**
  * Created by svijayaraja on 17/04/17.
  */
object PreProcessor {

  val logger = LogManager.getLogger(this.getClass.getName)

  implicit val formats = DefaultFormats

  /**
    * Stream json file extract useful information and write into a different file as one record per each line
    * Use this file to create RDD
    */
  def process(filePath: String): Unit = {
    //TODO: this is very important incase the file is very large and has all record under single root element or the json is prettified

    //step 1: read file as a stream
    //step 2: iterate reader
    //step 3: extract required data from file stream using stream json parser libraries like json4s pull parser
    //        or Gson stream parser or create own parser
    //step 4: write each record in a single line into a file
    //step 5: end of file
    //close reader and writer, persist the file
    //NOTE: keep input stream and output stream open till parsing the whole file

  }

  /**
    *
    * @param filePath
    * @return
    */
  def parseArticles(filePath : String) : List[Article] = {
    try {
      val lines = scala.io.Source.fromFile(filePath).mkString
      val authorList = (parse(lines) \ "MedlineCitationSet" \ "Article").extract[List[Article]]
      authorList
    } catch {
      case fnf: FileNotFoundException => { logger.error(s"Input File Not Found in ${filePath} ,kindly provide filename with fill path: ${fnf.getMessage}")
        throw fnf }
      case ioe : IOException => { logger.error(s"IOException occurred while reading ${filePath} ,please try again: ${ioe.getMessage}")
        throw ioe }
    }
  }

}
