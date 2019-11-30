package club.ki.ja;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Deck {
  static final Integer MAX_DECK_SIZE = 20;
  
  private final SheetsQuickstart sq;

  public Deck(SheetsQuickstart sq) {
    this.sq = sq;
  }

  public ArrayList<Term> fetch() throws Exception {
    List<List<Object>> values = sq.fetchAll();

    // Build Terms list
    ArrayList<Term> terms = new ArrayList<Term>();
    for (int i = 1; i < values.size(); ++i) {
      terms.add(new Term(values.get(i)));
    }

    // Remove any unscheduled terms
    ArrayList<Term> termsUnscheduled = new ArrayList<Term>();
    ArrayList<Term> termsScheduled = new ArrayList<Term>();
    
    for (Term term : terms) {
      if (term.getScheduledFor().length() == 0) {
        termsUnscheduled.add(term);
      } else {
        termsScheduled.add(term);
      }
    }

    // Sort by scheduling order
    Comparator<Term> compareByScheduledFor = new Comparator<Term>() {
      @Override
      public int compare(Term o1, Term o2) {
          return o1.getScheduledFor().compareTo(o2.getScheduledFor());
      }
    };
    termsScheduled.sort(compareByScheduledFor);

    // Append scheduled terms to the deck
    Integer deckSize = Math.min(MAX_DECK_SIZE, termsScheduled.size() - 1);
    ArrayList<Term> deckTerms = new ArrayList<Term>();
    for (int i = 0; i < deckSize; ++i) {
      deckTerms.add(termsScheduled.get(i));
    }

    // Append one new, unscheduled term
    deckTerms.add(termsUnscheduled.get(0));

    // Randomize order
    Collections.shuffle(deckTerms);

    return deckTerms;
  }
}