package club.ki.ja;

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SheetsService {
  private static final String APPLICATION_NAME = "Japanese Flashcards";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final String TOKENS_DIRECTORY_PATH = "tokens";

  /**
   * Global instance of the scopes required by this quickstart. If modifying these
   * scopes, delete your previously saved tokens/ folder.
   */
  private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
  private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

  private final String SPREADSHEET_ID = System.getenv("JA_SHEET_ID");
  private final String RANGE_PREFIX = "Terms";
  
  private Sheets service = null;
  private List<List<Object>> rows = null;

  /**
   * Creates an authorized Credential object.
   * 
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json files cannot be found.
   */
  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
    // Load client secrets.
    InputStream in = SheetsService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
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
  public List<List<Object>> fetchAll() throws Exception {
    if (this.rows != null) { return this.rows; } // Memoize

    ValueRange response = this.service().spreadsheets().values().get(SPREADSHEET_ID, RANGE_PREFIX).execute();
    this.rows = response.getValues();
    return this.rows;
  }

  public void reschedule(Integer rowIdx, String scheduledFor, Integer schedulingInterval) throws IOException {
    String range = String.format("Terms!B%d:C%d", rowIdx + 2, rowIdx + 2);
    ValueRange updateRequestBody = new ValueRange();
    updateRequestBody.setRange(range);
    updateRequestBody.setMajorDimension("ROWS");
    updateRequestBody.setValues(Arrays.asList(Arrays.asList(scheduledFor, schedulingInterval)));

    service.spreadsheets().values().update(SPREADSHEET_ID, range, updateRequestBody).setValueInputOption("RAW")
        .execute();
  }

  private Sheets service() throws GeneralSecurityException, IOException {
    if (this.service != null) { return this.service; } // Memoize

    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    this.service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME).build();
    return service;
  }
}