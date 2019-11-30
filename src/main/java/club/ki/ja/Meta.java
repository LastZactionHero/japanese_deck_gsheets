package club.ki.ja;

import java.util.List;

public class Meta {
  private final String code_index;
  private final String jlpt;
  private final String expression;
  private final String kana;
  private final String meaning;
  private final String pos;
  private final String sentence_expression;
  private final String sentence_kana;
  private final String sentence_meaning;
  private final String vocab_furigana;
  private final String sentence_furigana;
  private final String sentence_cloze;

  public Meta(List<Object> raw) {
    this.code_index = (String) raw.get(3);
    this.jlpt = (String) raw.get(4);
    this.expression = (String) raw.get(5);
    this.kana = (String) raw.get(6);
    this.meaning = (String) raw.get(7);
    this.pos = (String) raw.get(8);
    this.sentence_expression = (String) raw.get(9);
    this.sentence_kana = (String) raw.get(10);
    this.sentence_meaning = (String) raw.get(11);
    this.vocab_furigana = (String) raw.get(12);
    this.sentence_furigana = (String) raw.get(13);
    this.sentence_cloze = (String) raw.get(14);
  }
}