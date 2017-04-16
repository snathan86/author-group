package medline.articles

import org.apache.spark.{SparkConf, SparkContext}
import senthil.learn.JsonParser
import senthil.learn.JsonParser.{Article, Author}

/**
  * Created by senthil on 16/4/17.
  */
object Main {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext(conf)

    val rdd = sc.parallelize(JsonParser.parseArticles("/home/senthil/medline.json"))
    //rdd.map( article => article.)
    val mappedRDD = rdd.flatMap(article => mapAuthors1(article) )
    //rdd.fold(List[String]())(mapAuthors)
    val result = mappedRDD.map((_, 1)).reduceByKey(_ + _).collect()
    
    result.foreach(s => println(s"NAME : ${s._1},  COUNT: ${s._2}"))
    println("FINAL RESULT MAPPED TOGETHER: "+result)
    //mappedRDD.groupBy()
  }

  def mapAuthors1(article : Article) : List[String] ={
    //article.AuthorList.author.foldLeft(List[String]())(foldAuthor)
    combine(article.AuthorList.Author)
  }

  //TODO: do it with match case head::tail something like this in functional style
  def combine(authors : List[Author]) : List[String] = {
    var lst : List[String] = List()
    for(i <- authors.indices) {
      var j = i
      while(j<authors.length) {
        val author1 = authors.apply(i)
        val author2 = authors.apply(j)
        val a1 = s"${author1.LastName}~${author1.ForeName}~${author1.Initials}"
        val a2 = s"${author2.LastName}~${author2.ForeName}~${author2.Initials}"

        val s = a1>a2 match {
          case true => s"$a1|$a2"
          case _ => s"$a2|$a1"
        }

        lst = lst :+s
        j+=1
      }
    }
    lst
  }

  def mapAuthors(article: Article,authors: List[String]) : List[String] = {
    authors :+ article.AuthorList.Author.toString()
  }

}
