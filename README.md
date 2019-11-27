# Japanese Flashcards Backend

Rewrite of existing app using a Google Sheets database as backend for easy manual changes and review.


## TODO

- Script populate ScheduledFor/SchedulingInterval from existing backend
- Success scheduling
- Fail scheduling
- First time scheduling
- Serverless setup
- API: Deck
- API: Success
- API: Fail
- API: Dump

## Usage

### Basic setup

- Add `credentials.json` to `/src/main/resources`
- `gradle run`
- Go to OAuth link
- On sucess, prints some basic stats and creates `/tokens/StoredCredential`
