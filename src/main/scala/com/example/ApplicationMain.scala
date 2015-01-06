package com.example

import akka.actor.ActorSystem

object ApplicationMain extends App {
  val system = ActorSystem("MyActorSystem")
  val controllerActor = system.actorOf(ControllerActor.props, "controllerAtor")

  val seeds: List[String] = List("http://www.google.com", "http://www.google.com/1")

  controllerActor ! ControllerActor.Initialize(seeds)
  system.awaitTermination()
}
