package club.ki.ja;

import java.util.List;

public class Term {
  private final String uuid;
  private final String scheduled_for;
  private final Integer scheduling_interval;
  private final String term_a;
  private final String term_b;
  private final Meta meta;

  public Term(List<Object> raw) {
    this.uuid = (String) raw.get(0);
    this.scheduled_for = (String) raw.get(1);
    this.scheduling_interval = parseInt((String) raw.get(2));
    this.term_a = (String) raw.get(7);
    this.term_b = (String) raw.get(5);
    this.meta = new Meta(raw);
  }

  public String getScheduledFor() {
    return this.scheduled_for;
  }

  private Integer parseInt(String str) {
    if (str == null || str.length() == 0) {
      return 0;
    }
    return Integer.parseInt(str);
  }
}