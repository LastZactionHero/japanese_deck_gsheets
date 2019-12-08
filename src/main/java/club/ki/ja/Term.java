package club.ki.ja;

import java.lang.reflect.Array;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Term {
  private final String uuid;
  private String scheduled_for;
  private Integer scheduling_interval;
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

  public Integer getSchedulingInterval() {
    return this.scheduling_interval;
  }

  public String getUUID() {
    return this.uuid;
  }

  public void success() {
    System.out.printf("Success! %s\n", this.uuid);
    System.out.printf("Was: %d %s\n", this.scheduling_interval, this.scheduled_for);

    List<Duration> durations = Arrays.asList(
      Duration.of(1, ChronoUnit.MINUTES),
      Duration.of(10, ChronoUnit.MINUTES),
      Duration.of(1, ChronoUnit.HOURS),
      Duration.of(5, ChronoUnit.HOURS),
      Duration.of(1, ChronoUnit.DAYS),
      Duration.of(5, ChronoUnit.DAYS),
      Duration.of(25, ChronoUnit.DAYS),
      Duration.of(4 * 30, ChronoUnit.DAYS),
      Duration.of(1 * 365, ChronoUnit.DAYS)
    );

    DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
    LocalDateTime futureDate = LocalDateTime.now().plus(durations.get(this.scheduling_interval)).truncatedTo(ChronoUnit.SECONDS);
    this.scheduled_for = dtf.format(futureDate);
    this.scheduling_interval += 1;

    System.out.printf("Now: %d %s\n", this.scheduling_interval, this.scheduled_for);
  }

  public void fail() {
    System.out.printf("Fail! %s\n", this.uuid);
    System.out.printf("Was: %d %s\n", this.scheduling_interval, this.scheduled_for);

    this.scheduling_interval = 0;

    DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
    LocalDateTime futureDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    this.scheduled_for = dtf.format(futureDate);

    System.out.printf("Now: %d %s\n", this.scheduling_interval, this.scheduled_for);
  }

  private Integer parseInt(String str) {
    if (str == null || str.length() == 0) {
      return 0;
    }
    return Integer.parseInt(str);
  }
}