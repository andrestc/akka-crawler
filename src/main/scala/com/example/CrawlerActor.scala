package com.example

import akka.actor.{Actor, ActorLogging, Props, ActorSystem}

class CrawlerActor extends Actor with ActorLogging {
  import CrawlerActor._

  def receive = {
  	case Crawl(url) =>
	    log.info("Crawling url: {}", url)
  }
}

object CrawlerActor {
  val props = Props[CrawlerActor]
  case class Crawl(url: String)
}

class ControllerActor extends Actor with ActorLogging {
  import ControllerActor._
  import CrawlerActor._

  def receive = {
    case Initialize(urls) =>
      log.info("Intializing controller with urls: {}", urls)
      urls match {
        case head :: rest => {
          val system = ActorSystem("MyActorSystem")
          val crawlerActor = system.actorOf(CrawlerActor.props, "crawlerActor")
          crawlerActor ! CrawlerActor.Crawl(head)
          self ! ControllerActor.Initialize(rest)
        }
        case Nil => log.info("End of list.")
      }
  }
}

object ControllerActor {
  val props = Props[ControllerActor]
  case class Initialize(urls: List[String])
}
