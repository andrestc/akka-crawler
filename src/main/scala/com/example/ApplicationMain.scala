package com.example

import akka.actor.ActorSystem

object ApplicationMain extends App {
  val system = ActorSystem("MyActorSystem")
  val crawlerActor = system.actorOf(CrawlerActor.props, "crawlerActor")
  crawlerActor ! CrawlerActor.Crawl("http://www.google.com")
  // This example app will ping pong 3 times and thereafter terminate the ActorSystem -
  // see counter logic in PingActor
  system.awaitTermination()
}
