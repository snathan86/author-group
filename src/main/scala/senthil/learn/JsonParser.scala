package senthil.learn

import medline.articles.Main
import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
  * Created by senthil on 15/4/17.
  */
object JsonParser {

  case class Author(LastName: String, ForeName: String,Initials: String)
  case class AuthorList(Author:List[Author])
  case class Article(ArticleTitle:String,AuthorList:AuthorList)
  case class Articles(articles:List[Article])


  case class Child(name: String, age: Int, birthdate: Option[java.util.Date])
  case class Address(street: String, city: String)
  case class Person(name: String, address: Address, children: List[Child])
  implicit val formats = DefaultFormats // Brings in default date formats etc.

  def main(args: Array[String]): Unit = {
   // val lines = scala.io.Source.fromFile("/home/senthil/medline.json").mkString

    /** val json = parse("""
      * { "name": "joe",
      * "address": {
      * "street": "Bulevard",
      * "city": "Helsinki"
      * },
      * "children": [
      * {
      * "name": "Mary",
      * "age": 5,
      * "birthdate": "2004-09-04T18:06:22Z"
      * },
      * {
      * "name": "Mazy",
      * "age": 3
      * }
      * ]
      * }
      * """)
      * val persons = json.extract[Person] **/
//    val json = parse(lines)
    val llss = List(Article("Title 1",AuthorList(List(Author("Abraham","Alice","A"), Author("Byron","Bill","B")))),
    Article("Title 2",AuthorList(List(Author("Byron","Bill","B"), Author("Chang","Christie","C")))),
    Article("Title 3",AuthorList(List(Author("Chang","Christie","C"), Author("Abraham","Alice","A")))),
    Article("Title 4",AuthorList(List(Author("Doel","David","J"), Author("Byron","Bill","B")))),
    Article("Title 5 1author",AuthorList(List(Author("senthil","nathan","V")))),
    Article("Title 6 1 author",AuthorList(List(Author("Chang","Christie","C")))),
    Article("Title 7 4 author",AuthorList(List(Author("Chang","Christie","C"), Author("Parithi","Muthu","I"), Author("Subbu","Laks","M"), Author("Doel","David","J")))),
    Article("Title 8 2 author",AuthorList(List(Author("Ram","Laksh","C"), Author("Jayanth","Yadav","V")))),
    Article("Title 9 3 author",AuthorList(List(Author("Christoper","Nolan","W"), Author("Tom","Cruise","K"), Author("Rajni","Kanth","S")))),
      Article("Title 10 3 authors",AuthorList(List(Author("Doel","David","J"), Author("Byron","Bill","B"), Author("Abraham","Alice","A")))))
val lst = List("A","B","C")
println(build1(lst,List[String]()))
    //val authorList = parseArticles("/home/senthil/medline.json")
     // println(json.extract[List[Article]])
   // println(authorList.apply(0).AuthorList.Author)
    //println(Main.combine(authorList.apply(6).AuthorList.Author))
    //println(Main.combine(authorList.apply(7).AuthorList.Author))

  }

  def buildddddd(lst: List[String],op:List[String]) : List[String] = {
    val s =lst match {
      case head +: Nil => { val s = combine(head,head)
      println("first case "+s)
        val l = op :+ s
        l
      }
      case head +: tail => {
        val l = op :+ combine(head, head) :+ combine(head,tail.head)
        println("op here:"+l)
        buildddddd(tail,l)
      }

    }
    s
  }

  def build1(lst: List[String],op:List[String]) : List[String] = {
    val s =lst match {
      case head +: Nil => { combine(head,head) +: op }
      case head +: tail => {
        build1(tail,combine(head, head) +: tail.map(ele => combine(head,ele)) ++:  op)
      }
    }
    s
  }

  def build2(lst: List[String],op:List[String]) : List[String] = {
    val s =lst match {
      case head +: Nil => List(head)
      case head +: tail => {
       val temp =    tail.map(ele => combine(head,ele)) ++: op :+ combine(head, head)
        build1(tail,temp)
      // val temp1 =  combine(head, head) +: combine(head, build1(tail,op).apply(0))
        //List(tail.head)
       //combine(head, head) +: combine(head,build1(tail,op).apply(0))
      }
    }
    s
  }

// tail match { case Nil => Nil
 // case _ => combine(head, tail.head)}
 // buildByFold(op:List[String],)

  def combine(a1 : String , a2: String): String = {
    s"$a1 ~ $a2"
  }
  def parseArticles(filePath : String) : List[Article] = {
    //val lines= scala.io.Source.fromFile(filePath).mkString
    //val json = parse(lines)
    //val authorList = parse(lines,false).extract[List[Article]]
    val llss = List(Article("Title 1",AuthorList(List(Author("Abraham","Alice","A"), Author("Byron","Bill","B")))),
      Article("Title 2",AuthorList(List(Author("Byron","Bill","B"), Author("Chang","Christie","C")))),
      Article("Title 3",AuthorList(List(Author("Chang","Christie","C"), Author("Abraham","Alice","A")))),
      Article("Title 4",AuthorList(List(Author("Doel","David","J"), Author("Byron","Bill","B")))),
      Article("Title 5 1author",AuthorList(List(Author("senthil","nathan","V")))),
      Article("Title 6 1 author",AuthorList(List(Author("Chang","Christie","C")))),
      Article("Title 7 4 author",AuthorList(List(Author("Chang","Christie","C"), Author("Parithi","Muthu","I"), Author("Subbu","Laks","M"), Author("Doel","David","J")))),
      Article("Title 8 2 author",AuthorList(List(Author("Ram","Laksh","C"), Author("Jayanth","Yadav","V")))),
      Article("Title 11 2 authors",AuthorList(List(Author("Ram","Laksh","C"), Author("Jayanth","Yadav","V")))),
      Article("Title 9 3 author",AuthorList(List(Author("Christoper","Nolan","W"), Author("Tom","Cruise","K"), Author("Rajni","Kanth","S")))),
      Article("Title 10 3 authors",AuthorList(List(Author("Doel","David","J"), Author("Byron","Bill","B"), Author("Abraham","Alice","A")))))
    llss
  }
}
