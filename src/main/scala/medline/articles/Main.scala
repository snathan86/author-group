package medline.articles

import medline.articles.persistence.SerDe
import org.apache.spark.{SparkConf, SparkContext}
import medline.articles.preprocess.PreProcessor
import medline.articles.model.{Article, Author}
import medline.articles.result.{DistinctAuthors, Result}
import org.apache.spark.rdd.RDD
import org.apache.logging.log4j.LogManager

/**
  * Created by senthil on 16/4/17.
  */
object Main {

  val logger = LogManager.getLogger(this.getClass.getName)

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("author-grouping")
    val sc = new SparkContext(conf)

    logger.info("file processing started")
    //make top level RDD
    //TODO: call PreProcessor.process("/tmp/exercise/input/med.json")
    //This could be  a different job,which process the files and bring it into a shape which is easy to parallelize
    //sc.parallelize is only for prototyping , not suitable for large files
    val rdd = sc.parallelize(PreProcessor.parseArticles(args(0)))

    //Go through all articles and make all author~co-author combinations
    val authorPairs = combineAuthors(rdd).cache()

    //compute actual result, count occurrences of the keys(author combination)
    //if required,do operation like sort by name, sort by count, fetch by author/co author on this result
    val authorCountMap = countCoAuthoredArticles(authorPairs)

    //fetch all authors
    val authors = extractAuthors(authorPairs)
    val distinctAuthors = authors.distinct

    //Note: Persisting result as csv/json/tsv like formats is better and easy than persisting object,
    //(contd..) since I am using serialization for for the simplicity, I am constructing a matrix and saving it
    val matrix = buildMatrix(authorCountMap,distinctAuthors)

    //write object into file(simulate storing computed result to hdfs/s3/db)
    SerDe.serialize("../op.tmp",Result(matrix))
    logger.info("resultant matrix successfully persisted")
    //store authors in order
    SerDe.serialize("../author.tmp",DistinctAuthors(distinctAuthors))
    logger.info(s"Distinct Author names persisted, size : ${distinctAuthors.size}, process completed!")

  }

  /**
    * take author array of an article and build keys by making possible combinations
    * of two authors(include repetitions)
    * @param articles
    * @return RDD[String] : RDD["author1_fullName~author2_fullName"]
    */
  def combineAuthors(articles:RDD[Article]) : RDD[String]= {
    articles.flatMap(article => buildKeys(article.AuthorList.Author,List[String]()))
  }

  /**
    * build keys by making possible combinations of two authors(include repetitions)
    */
  def buildKeys(authors : List[Author],acc : List[String]) : List[String] = {
   authors match {
     case head +: Nil => combine(head,head) +: acc
     case head +: tail => buildKeys(tail,combine(head,head) +: tail.map(e => combine(head,e)) ++: acc)
   }
  }

  /**
    * build key by combining two author names ~ delimited
    */
  def combine(author1: Author , author2 : Author) : String = {

    val a1 = fullName(author1)
    val a2 = fullName(author2)

    a1<a2 match {
      case true => s"$a1~$a2"
      case _ => s"$a2~$a1"
    }
  }

  /**
    *construct full name by combining first,last name and initial space delimited
    */
  def fullName(author : Author) : String = {
      s"${author.LastName} ${author.ForeName} ${author.Initials}"
  }

  /**
    * Find no of books co authored by each pair of authors
    * @param authorPairs
    * @return Seq[(String,Int)] : Seq[authorPair,count]
    */
  def countCoAuthoredArticles(authorPairs: RDD[String]): Seq[(String,Int)] ={
    authorPairs.map((_, 1)).reduceByKey(_ + _).collect()
  }

  /**
    * extract all author names from cachedRDD
    */
  def extractAuthors(mappedRDD: RDD[String]) : List[String]={
    mappedRDD.aggregate(List[String]())(
      (list,value) => { val s = value.split("~")
        s(0) +: s(1) +: list
      },
      (list1,list2) => list1 ++: list2
    )

  }
  /**
    * construct two dimensional array of final result
    * @param authorCountMap
    * @param authors
    * @return
    */
  def buildMatrix(authorCountMap : Seq[(String,Int)], authors : List[String]): Array[Array[Int]] = {
    val authorIndex = authors.view.zipWithIndex.toMap
    val result = Array.ofDim[Int](authorIndex.size,authorIndex.size)

    authorCountMap.foreach(v => { val s = v._1.split("~")
      val i = authorIndex.get(s(0))
      val j = authorIndex.get(s(1))
      result(i.get)(j.get) = v._2
      result(j.get)(i.get) = v._2
    })
    result
  }

}
