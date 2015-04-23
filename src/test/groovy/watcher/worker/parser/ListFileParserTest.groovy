package watcher.worker.parser

import spock.lang.Shared
import spock.lang.Specification

class ListFileParserTest extends Specification {

  @Shared
  ListFileParser listFileParser = new ListFileParser()

  def "converts a list file into packs"() {
    given:
    File listFile = new File(getClass().classLoader.getResource("parser/file/list_file_well_formed.txt").file)

    when:
    def packs = listFileParser.parsePacksFrom(listFile)

    then:
    packs.size() == 8
    packs.get(13L) == "The.Big.Bang.Theory.S01E12.HDTV.XviD-FoV.avi"
    packs.get(14L) == "The.Big.Bang.Theory.S01E13.HDTV.[ XviD-FoV ].avi"
    packs.get(15L) == "The.Big.Bang.Theory.S01E14.HDTV.XviD-XOR.avi"
    packs.get(16L) == "The.Big.Bang.Theory.- S01E15 -.HDTV.XviD-FoV.avi"
    packs.get(17L) == "The.Big.Bang.Theory.S01E16.HDTV.XviD-XOR.avi"
    packs.get(18L) == "The.Big.Bang.Theory.S01E17.HDTV.XviD-LOL.avi"
    packs.get(19L) == "The.Big.Bang.Theory.S02.HDTV.XviD.SRT.VF.zip"
    packs.get(20L) == "The.Big.Bang.Theory.S02E01.REAL.PROPER.HDTV.XviD-NoTV.avi"
  }

  def "skips a line if it cannot be split correctly"() {
    given:
    File listFile = new File(getClass().classLoader.getResource("parser/file/list_file_malformed_line.txt").file)

    when:
    def packs = listFileParser.parsePacksFrom(listFile)

    then:
    packs.get(10L) == null
    packs.get(16L) == "The.Big.Bang.Theory.- S01E15 -.HDTV.XviD-FoV.avi"
    packs.get(17L) == "The.Big.Bang.Theory.S01E16.HDTV.XviD-XOR.avi"
  }

  def "skips a line with a malformed pack id"() {
    given:
    File listFile = new File(getClass().classLoader.getResource("parser/file/list_file_malformed_id.txt").file)

    when:
    def packs = listFileParser.parsePacksFrom(listFile)

    then:
    packs.get(10L) == null
    packs.get(16L) == "The.Big.Bang.Theory.- S01E15 -.HDTV.XviD-FoV.avi"
    packs.get(17L) == "The.Big.Bang.Theory.S01E16.HDTV.XviD-XOR.avi"
  }
}
