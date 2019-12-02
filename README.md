# Japanese Flashcards Backend

Rewrite of existing app using a Google Sheets database as backend for easy manual changes and review.

## Running

`./gradlew build && java -jar build/libs/gs-spring-boot-0.1.0.jar`

## Dockerizing

1. Copy `credentials.json` to application directory
2. `docker build -t ja_flashcards .`
3. `docker run -d -e JA_SHEET_ID=1-Oe3qrFv3mhPnpY-48Y5BXHj2f6VzVhLQQT2OUx7KsM -p 8080:8080 -p 8888:8888 -v /application/resources:/application/src/main/resources -v /application/tokens:/application/tokens -it ja_flashcards`

## TODO

- Guice injection of SheetsService
x Script populate ScheduledFor/SchedulingInterval from existing backend
x Success scheduling
x Fail scheduling
x First time scheduling
x Spring setup
x API: Deck
x API: Success
x API: Fail
- MP3 URLs

## Usage

### Basic setup

- Add `credentials.json` to `/src/main/resources`
- `gradle run`
- Go to OAuth link
- On sucess, prints some basic stats and creates `/tokens/StoredCredential`
