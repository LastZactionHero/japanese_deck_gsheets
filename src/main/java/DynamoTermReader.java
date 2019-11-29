import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DynamoTermReader {
  public DynamoTermReader() {
  }

  public ArrayList<DynamoTerm> fetchTerms() throws Exception {
    String urlStr = System.getenv("JA_DYNAMO_TERMS_URL");
    if (urlStr == null || urlStr.length() == 0) {
      throw new IllegalArgumentException("DynamoDB Terms URL not found");
    }

    URL url = new URL(urlStr);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    int status = con.getResponseCode();
    System.out.printf("Response Code: %d\n", status);

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer content = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }
    in.close();
    con.disconnect();
    Gson gson = new Gson();
    Type collectionType = new TypeToken<ArrayList<DynamoTerm>>() {
    }.getType();
    ArrayList<DynamoTerm> terms = gson.fromJson(content.toString(), collectionType);

    terms.removeIf(term -> term.scheduled_for.contains("9999"));

    return terms;
  }
}