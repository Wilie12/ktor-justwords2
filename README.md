# JustWords2API
JustWords2API is a REST API, written in Kotlin with Ktor, it's serving learning words for my Android app [Get it here](https://github.com/Wilie12/JustWords2). API supports user authentication and its resources are restricted under JWT authorization.
## Resources
There are 4 main resources in JustWords2API:
- Auth
- Words/Books/WordSets
- UserInfo
- UserHistory

Note: In order to use API, you have to fetch previously created words database.
## How to prepare database
API is designed to work with previously created database, it doesn't support creating new Words/Books/Wordsets. Create Json files and insert them into MongoDB.
### Create a Book
Book holds multiple WordSets.
```json
{
  "name": "First Book",
  "color": -14928072
}
```
Note: API uses MongoDB, which autogenerates ObjectId.
### Create a WordSet
Wordset holds multiple Words divided into groups.
```json
{
  "name": "Personality",
  "bookId": "6707f56059f9f0f47acb70e3",
  "numberOfGroups": 1
}
```
Note: BookId has to be previously generated ObjectId for Book.
### Create a Word
Words are divided into groups, there can be multiple groups in one WordSet
```json
{
  "sentence": "She is an ambitious career woman.",
  "wordPl": "ambitny",
  "wordEng": "ambitious",
  "setId": "6707f58059f9f0f47acb70e6",
  "groupNumber": 1
}
```
Note: SetId has to be previously generated ObjectId for WordSet.
### Explaination
JustWords2API was originally designed in Polish but can be used to learn any language.
- wordPl stands for word in your current language
- wordEng stands for word in lanugage you desire to learn
- senstence is written in language you desire to learn

### Summary
After inserting these Jsons into MongoDB JustWords2API can be used with my application [Get it here](https://github.com/Wilie12/JustWords2).
## How to use
All resources in API are restricted with JWT authorization, in order to use API you have to get accessToken.
### Auth
#### Register new user
```
[POST] https://localhost:8080/register

#body
{
  "username": "New User",
  "email": "user@email.com",
  "password": "Password123"
}
```
#### Login
```
[POST] https://localhost:8080/register

#body
{
  "email": "user@email.com",
  "password": "Password123"
}
```
#### Refresh AccessToken
```
[POST] https://localhost:8080/accessToken

#body
{
  "refreshToken": "value obtained from login response",
  "userId": "6765ac88f033427a9ea31b1f"
}
```
### Words
In order to access following endpoints, add Authentication Header
```
Authentication: "Bearer accessToken"
```
#### Get all Books
```
[GET] https://localhost:8080/books
```
#### Get all WordSets
```
[GET] https://localhost:8080/sets
```
#### Get WordSets for given Book
```
[GET] https://localhost:8080/setsById?bookId=6707f56059f9f0f47acb70e3
```
#### Get Words for given WordSet
```
[GET] https://localhost:8080/words?setId=6707f58059f9f0f47acb70e6
```
### UserInfo
In order to access following endpoints, add Authentication Header
```
Authentication: "Bearer accessToken"
```
#### Get UserInfo for given user
```
[GET] https://localhost:8080/getUserInfo?userId=6765ac88f033427a9ea31b1f
```
#### Update UserInfo
```
[POST] https://localhost:8080/updateUserInfo

#body
{
    "userInfo": {
        "dailyStreak": 4,
        "bestStreak": 7,
        "currentGoal": 2,
        "dailyGoal": 5,
        "lastPlayedTimestamp": "312312312312312",
        "lastEditedTimestamp": "312312312312312",
        "userName": "New username",
        "userId": "6765ac88f033427a9ea31b1f"
    }
}
```
### UserHistory
In order to access following endpoints, add Authentication Header
```
Authentication: "Bearer accessToken"
```
#### Get UserHistory
```
[GET] https://localhost:8080/userHistory
```
#### Insert new UserHisory
```
[POST] https://localhost:8080/postUserHistory

#body
{
  "userWordHistorySerializable": {
    "bookName": "First Book",
    "bookColor": -14928072,
    "setName": "Personality",
    "groupNumber": 1,
    "dateTimeUtc": "2024-10-10T16:30:06.903Z",
    "perfectGuessed": 24,
    "wordListSize": 30,
    "id": "6708010e0930b24019febfbd"
    }
}
```
Note: Id isn't autogenerated, it was designed to be created inside my application [Get it here](https://github.com/Wilie12/JustWords2).
## Technologies
- Kotlin
- Ktor
- MongoDB
- Koin
- OAuth
