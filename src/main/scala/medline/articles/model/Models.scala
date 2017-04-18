package medline.articles.model


case class Author(LastName: String, ForeName: String,Initials: String)
case class AuthorList(Author:List[Author])
case class Article(ArticleTitle:String,AuthorList:AuthorList)
case class Articles(articles:List[Article])
