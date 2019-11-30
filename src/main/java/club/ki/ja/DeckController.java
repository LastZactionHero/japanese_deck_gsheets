package club.ki.ja;

import java.util.ArrayList;

import com.google.gson.Gson;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeckController {
  @RequestMapping(value = "/deck", method = RequestMethod.GET, produces = "application/json")
  public String deck() throws Exception {
    Deck deck = new Deck(new SheetsService());
    ArrayList<Term> terms = deck.fetch();

    Gson gson = new Gson();
    String body = gson.toJson(terms);   
    return body;
  }

  @RequestMapping(value = "/terms/{uuid}/success", method = RequestMethod.GET, produces = "application/json")
  public String success(@PathVariable String uuid) {
    System.out.println(uuid);

    // Find Term by UUID
    // Reschedule later, increment scheduling interval
    // Save to Sheet

    return "{}";
  }

  @RequestMapping(value = "/terms/{uuid}/fail", method = RequestMethod.GET, produces = "application/json")
  public String fail(@PathVariable String uuid) {
    System.out.println(uuid);

    // Find Term by UUID
    // Reschedule now, clear scheduling interval
    // Save to Sheet

    return "{}";
  }
}