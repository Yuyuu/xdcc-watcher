package watcher.model.bot

import org.bson.types.ObjectId
import spock.lang.Specification

class PackTest extends Specification {

  static ObjectId id = ObjectId.get()
  static ObjectId otherId = ObjectId.get()

  def "two packs with the same properties are equal"() {
    given:
    def firstPack = new Pack(23, "season.02.episode.08", id)
    def secondPack = new Pack(23, "season.02.episode.08", id)

    expect:
    firstPack == secondPack
  }

  def "two packs with different positions are different"() {
    given:
    def firstPack = new Pack(23, "season.02.episode.08", id)
    def secondPack = new Pack(10, "season.02.episode.08", id)

    expect:
    firstPack != secondPack
  }

  def "two packs with different titles are not equal"() {
    given:
    def firstPack = new Pack(23, "season.01.episode.12", id)
    def secondPack = new Pack(23, "season.02.episode.08", id)

    expect:
    firstPack != secondPack
  }

  def "two packs which do not belong to the same bot are different"() {
    given:
    def firstPack = new Pack(10, "season.02.episode.08", id)
    def secondPack = new Pack(10, "season.02.episode.08", otherId)

    expect:
    firstPack != secondPack
  }

  def "two packs with different position have different hashcodes"() {
    given:
    def firstPack = new Pack(23, "season.02.episode.08", id)
    def secondPack = new Pack(10, "season.02.episode.08", id)

    expect:
    firstPack.hashCode() != secondPack.hashCode()
  }

  def "two packs with different title have different hashcodes"() {
    given:
    def firstPack = new Pack(23, "season.01.episode.12", id)
    def secondPack = new Pack(23, "season.02.episode.08", id)

    expect:
    firstPack.hashCode() != secondPack.hashCode()
  }

  def "two packs with different bot id have different hashcodes"() {
    given:
    def firstPack = new Pack(10, "season.02.episode.08", id)
    def secondPack = new Pack(10, "season.02.episode.08", otherId)

    expect:
    firstPack.hashCode() != secondPack.hashCode()
  }
}
