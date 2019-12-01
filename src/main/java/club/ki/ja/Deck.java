package club.ki.ja;

import java.util.ArrayList;
import java.util.Collections;
public class Deck {
  static final Integer MAX_DECK_SIZE = 20;
  
  private final SheetTerms st;

  public Deck(SheetTerms st) {
    this.st = st;
  }

  public ArrayList<Term> fetch() throws Exception {
    // Append scheduled terms to the deck
    Integer deckSize = Math.min(MAX_DECK_SIZE, this.st.scheduled().size() - 1);
    ArrayList<Term> deckTerms = new ArrayList<Term>();
    for (int i = 0; i < deckSize; ++i) {
      deckTerms.add(this.st.scheduled().get(i));
    }

    // Append one new, unscheduled term
    if (this.st.unscheduled().size() > 0) {
      deckTerms.add(this.st.unscheduled().get(0));
    }
    
    // Randomize order
    Collections.shuffle(deckTerms);

    return deckTerms;
  }
}