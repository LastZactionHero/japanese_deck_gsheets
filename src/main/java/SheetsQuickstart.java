import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.mortbay.io.Buffer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.SysexMessage;

public class SheetsQuickstart {
  private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final String TOKENS_DIRECTORY_PATH = "tokens";

  /**
   * Global instance of the scopes required by this quickstart. If modifying these
   * scopes, delete your previously saved tokens/ folder.
   */
  private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
  private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

  /**
   * Creates an authorized Credential object.
   * 
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json files cannot be found.
   */
  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
    // Load client secrets.
    InputStream in = SheetsQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    if (in == null) {
      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
    }
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
        clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline").build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
  }

  /**
   * @throws Exception
   */
  public static void main(String... args) throws Exception {
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = System.getenv("JA_SHEET_ID");
    if (spreadsheetId == null || spreadsheetId.length() == 0) {
      throw new IllegalArgumentException("Sheet ID not set");
    }

    String range = "Terms";

    Instant instantStart = Instant.now();

    Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME).build();

    ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
    } else {
      Instant instantEnd = Instant.now();
      long duration = instantEnd.toEpochMilli() - instantStart.toEpochMilli();

      System.out.printf("Terms: %d, %dms\n", values.size(), duration);

      DynamoTermReader dtr = new DynamoTermReader();
      ArrayList<DynamoTerm> dynamoTerms = dtr.fetchTerms();

      System.out.printf("Scheduled terms: %d\n", dynamoTerms.size());

      Map<Integer, DynamoTerm> dynamoMap = new HashMap<Integer, DynamoTerm>();
      for (DynamoTerm dt : dynamoTerms) {
        dynamoMap.put(dt.meta.core_index, dt);
      }

      for (int i = 1; i < values.size(); ++i) {
        System.out.printf("Row: %d\n", i);
        List row = values.get(i);

        String coreIdxStr = (String) row.get(3);
        Integer coreIdx = Integer.parseInt(coreIdxStr);
        if (coreIdx <= 0)
          continue;

        System.out.printf("CoreIdx: %d\n", coreIdx);
        DynamoTerm dt = dynamoMap.get(coreIdx);
        if (dt == null)
          continue;

        System.out.printf("Row: %d UUID: %s, Scheduled: %s, Interval %d\n", i, row.get(0), dt.scheduled_for,
            dt.scheduling_interval);

        range = String.format("Terms!B%d:C%d", i + 1, i + 1);
        ValueRange updateRequestBody = new ValueRange();
        updateRequestBody.setRange(range);
        updateRequestBody.setMajorDimension("ROWS");
        updateRequestBody.setValues(Arrays.asList(Arrays.asList(dt.scheduled_for, dt.scheduling_interval)));

        service.spreadsheets().values().update(spreadsheetId, range, updateRequestBody).setValueInputOption("RAW")
            .execute();
        Thread.sleep(1000);
        // if( i > 10 ) { break; }
      }
    }
  }
}