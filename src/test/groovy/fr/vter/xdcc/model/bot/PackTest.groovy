package fr.vter.xdcc.model.bot

import spock.lang.Specification
import watcher.model.bot.Pack

class PackTest extends Specification {

  def "gets a default id upon creation"() {
    given:
    def pack = new Pack(3, "episode 3")

    expect:
    pack.id != null
  }

  def "two packs with the same position and title are equal"() {
    given:
    def firstPack = new Pack(23, "season.02.episode.08")
    def secondPack = new Pack(23, "season.02.episode.08")

    expect:
    firstPack == secondPack
  }

  def "two packs with different positions are not equal"() {
    given:
    def firstPack = new Pack(23, "season.02.episode.08")
    def secondPack = new Pack(10, "season.02.episode.08")

    expect:
    firstPack != secondPack
  }

  def "two packs with different titles are not equal"() {
    given:
    def firstPack = new Pack(23, "season.01.episode.12")
    def secondPack = new Pack(23, "season.02.episode.08")

    expect:
    firstPack != secondPack
  }
}
