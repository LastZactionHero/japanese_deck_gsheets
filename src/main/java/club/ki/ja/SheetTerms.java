package club.ki.ja;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SheetTerms {
  private final SheetsService ss;
  private ArrayList<Term> all;
  private ArrayList<Term> unscheduled;
  private ArrayList<Term> scheduled;

  public SheetTerms(SheetsService ss) {
    this.ss = ss;
  }

  public ArrayList<Term> all() {
    if(this.all != null) {
      return this.all;
    }

    List<List<Object>> values;
    try {
      values = ss.fetchAll();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    // Build Terms list
    this.all = new ArrayList<Term>();
    for (int i = 1; i < values.size(); ++i) {
      this.all.add(new Term(values.get(i)));
    }

    return this.all;
  }

  public ArrayList<Term> unscheduled() {
    if (this.unscheduled != null) {
      return this.unscheduled;
    }

    this.unscheduled = new ArrayList<Term>();
    for (Term term : all()) {
      if (term.getScheduledFor().length() == 0) {
        this.unscheduled.add(term);
      }
    }

    return this.unscheduled;
  }

  public ArrayList<Term> scheduled() {
    if (this.scheduled != null) {
      return this.scheduled;
    }

    this.scheduled = new ArrayList<Term>();
    for (Term term : all()) {
      if (term.getScheduledFor().length() > 0) {
        this.scheduled.add(term);
      }
    }

    // Sort by scheduling order
    Comparator<Term> compareByScheduledFor = new Comparator<Term>() {
      @Override
      public int compare(Term o1, Term o2) {
          return o1.getScheduledFor().compareTo(o2.getScheduledFor());
      }
    };
    this.scheduled.sort(compareByScheduledFor);

    return this.scheduled;
  }

  public Term findByUUID(String uuid) {
    for (Term q : all()) {
      if (q.getUUID().equals(uuid)) {
        return q;
      }
    }
    return null;
  }

  public void reschedule(Term term) throws IOException {
    Integer index = this.all().indexOf(term);
    if (index == -1) {
      throw new IllegalAccessError("Term not found");
    }

    ss.reschedule(index, term.getScheduledFor(), term.getSchedulingInterval());
  }
}