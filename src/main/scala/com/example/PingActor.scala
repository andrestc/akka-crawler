package com.example

import akka.actor.{Actor, ActorLogging, Props}

class CrawlerActor extends Actor with ActorLogging {

  def receive = {
  	case Crawl(url) =>
	    log.info("Crawling url: {}", url)
}

object CrawlerActor {
  val props = Props[CrawlerActor]
  case class Crawl(url: String)
}
