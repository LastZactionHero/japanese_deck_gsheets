# Japanese Flashcards Backend

Rewrite of existing app using a Google Sheets database as backend for easy manual changes and review.

## Running

`./gradlew build && java -jar build/libs/gs-spring-boot-0.1.0.jar`

## TODO

- Guice injection of SheetsService
x Script populate ScheduledFor/SchedulingInterval from existing backend
- Success scheduling
- Fail scheduling
- First time scheduling
x Spring setup
x API: Deck
- API: Success
- API: Fail
- MP3 URLs

## Usage

### Basic setup

- Add `credentials.json` to `/src/main/resources`
- `gradle run`
- Go to OAuth link
- On sucess, prints some basic stats and creates `/tokens/StoredCredential`
