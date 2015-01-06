package com.example

import akka.actor.{Actor, ActorLogging, Props, ActorSystem}

class CrawlerActor extends Actor with ActorLogging {
  import CrawlerActor._
  import scala.io.Source

  def receive = {
  	case Crawl(url) =>
	    log.info("Crawling url: {}", url)
      val source = Source.fromURL(url, "ISO-8859-1").mkString
      sender ! ControllerActor.Process(url, source)
  }
}

object CrawlerActor {
  val props = Props[CrawlerActor]
  case class Crawl(url: String)
}

class IndexerActor extends Actor with ActorLogging {
  import IndexerActor._

  def receive = {
    case Index(url, source) =>
      log.info("Indexing {}", url)
      log.info(source)
  }
}

object IndexerActor {
  val props = Props[IndexerActor]
  case class Index(url: String, source: String)
}

class ControllerActor extends Actor with ActorLogging {
  import ControllerActor._
  import CrawlerActor._

  val system = ActorSystem("MyActorSystem")

  def receive = {
    case Initialize(urls) =>
      log.info("Running controller with urls: {}", urls)
      urls match {
        case head :: rest => {
          val crawlerActor = system.actorOf(CrawlerActor.props, "crawlerActor")
          crawlerActor ! CrawlerActor.Crawl(head)
          self ! ControllerActor.Initialize(rest)
        }
        case Nil => {
          log.info("End of list.")
        }
      }
    case Process(url, source) =>
      val indexerActor = system.actorOf(IndexerActor.props, "indexerActor")
      indexerActor ! IndexerActor.Index(url, source)
  }
}

object ControllerActor {
  val props = Props[ControllerActor]
  case class Initialize(urls: List[String])
  case class Process(url: String, source: String)
}
