package watcher.worker.parser

import spock.lang.Shared
import spock.lang.Specification

class WebsiteParserTest extends Specification {

  @Shared
  WebsiteParser websiteParser = new WebsiteParser()

  def "converts the content of a bot web page into packs"() {
    given:
    def stream = getClass().classLoader.getResourceAsStream("parser/website/page.html")

    when:
    def packs = websiteParser.parsePacksFrom(stream)

    then:
    packs.size() == 8
    packs.get(1L) == "Gotham.S01E01.HDTV.x264-LOL.mp4"
    packs.get(2L) == "Gotham.S01E02.HDTV.x264-LOL.mp4"
    packs.get(3L) == "Gotham.S01E03.HDTV.x264-LOL.mp4"
    packs.get(4L) == "Gotham.S01E04.HDTV.x264-LOL.mp4"
    packs.get(5L) == "Gotham.S01E05.HDTV.x264-LOL.mp4"
    packs.get(6L) == "Gotham.S01E06.HDTV.x264-LOL.mp4"
    packs.get(7L) == "Gotham.S01E07.HDTV.x264-LOL.mp4"
    packs.get(8L) == "Gotham.S01E08.HDTV.x264-LOL.mp4"
  }
}
