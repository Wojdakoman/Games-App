# Games-App
Simple Android app to search for games and save them to "Favourite" or "Played" lists.

## Features
* Logging in and creating a new account
* Data synchronization between devices
* Showing top and latest games
* Searching for games
* Showing game details (creators, availability on platforms )
* Adding games to favourites or/and finished lists

## Technologies
* Kotlin
* Firebase
* [Retrofit](https://github.com/square/retrofit)
* [Glide](https://github.com/bumptech/glide)

## API
Application uses [IGDB.com API](https://api-docs.igdb.com/) to retrieve games data.
It is also neccessary to call Twitch API, in order to authorize access to IGDB.com API.

## Interface

| Login screen | Sign up screen |
|-------------|-------------|
| ![login screen](https://user-images.githubusercontent.com/7689591/119647717-e4b17c80-be20-11eb-9365-587ac04b410c.png) | ![signup screen](https://user-images.githubusercontent.com/7689591/119647796-fbf06a00-be20-11eb-9e31-f7a3a699b8a5.png) |

| Home screen | Game screen |
|-------------|-------------|
| ![home screen](https://user-images.githubusercontent.com/7689591/119647852-0d397680-be21-11eb-8ada-67cf6edd3f4a.png) | ![game screen](https://user-images.githubusercontent.com/7689591/119647899-1f1b1980-be21-11eb-8e15-4a755ca363ad.png) |

| Lists screen | Search results list |
|-------------|-------------|
| ![lists screen](https://user-images.githubusercontent.com/7689591/119647959-30642600-be21-11eb-9050-7014b7c7fe46.png) | ![search results screen](https://user-images.githubusercontent.com/7689591/119648011-3eb24200-be21-11eb-941c-6e4744434025.png) |
