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
    val indexedRDD = rdd.flatMap(indexAuthors)

   // val mappedRDD = rdd.flatMap(article => mapAuthors1(article) )
    //rdd.fold(List[String]())(mapAuthors)
    //val result = mappedRDD.map((_, 1)).reduceByKey(_ + _).collect()
      indexedRDD.foreach(e => println(s"KEY : ${e._1}  INDEX : ${e._2}"))
    //result.foreach(s => println(s"NAME : ${s._1},  COUNT: ${s._2}"))
    //println("FINAL RESULT MAPPED TOGETHER: "+result)
    //mappedRDD.groupBy()
  }
  //use linked hash map
  def indexAuthors(article : Article): Map[String,Int] = {
    val authors =article.AuthorList.Author
    //authors.map( author => constructKey(author) -> 1 )
    authors.foldLeft(Map[String,Int]())(index)

  }

  def index(map: Map[String,Int], author : Author) : Map[String,Int] = {
    map + (constructKey(author) -> map.size)
  }

  def mapAuthors1(article : Article) : List[String] ={
    //article.AuthorList.author.foldLeft(List[String]())(foldAuthor)
    build(article.AuthorList.Author,List[String]())
  }

  //TODO: do it with match case head::tail something like this in functional style
  def combine2(authors : List[Author]) : List[String] = {
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

  def build(authors : List[Author],acc : List[String]) : List[String] = {
   authors match {
     case head +: Nil => combine(head,head) +: acc
     case head +: tail => build(tail,combine(head,head) +: tail.map(e => combine(head,e)) ++: acc)
   }
  }

  def combine(author1: Author , author2 : Author) : String = {

    val a1 = constructKey(author1)
    val a2 = constructKey(author2)

    a1<a2 match {
      case true => s"$a1|$a2"
      case _ => s"$a2|$a1"
    }
  }

  def constructKey(author : Author) : String = {
      s"${author.LastName}~${author.ForeName}~${author.Initials}"
  }



  def mapAuthors(article: Article,authors: List[String]) : List[String] = {
    authors :+ article.AuthorList.Author.toString()
  }

}
