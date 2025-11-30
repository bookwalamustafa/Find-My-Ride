# 11/16/2025

## Development Strategy

As we team we focused and came up with the strategy with 3 Phases.

We are following Three Phase Strategy:

UI Development -> UI Connection Development -> Frontend to Backend Database Connection

And [ChatGPT](https://chatgpt.com/share/691a8a83-69c0-800e-be1c-aa304a8a901f) verified this is the correct process that modern companies use.

Following paging structure with react where we have folders for our features.

- I'm make the structure

```txt
feature:
  - auth/ # all authentication related stuff
  - rides/ # all main ride related stuff
  - .../ # insert other features
```

- I added a thing to allow colors to be used all over by every file in this project.
  - I think Ken is also asking me about my hex color irl so idk how to think about that. /s

Now need to focus on getting the code for login page to make sense.

- I asked a question but he left on delivered.

## KMP Notes:

### @Composable
- `@Composable` marks this as a composable fucntion - describing UI in Compose
    - annotation in Jetpack Compose (and Compose Multiplatform)
    - marks a function as part of the UI tree:
      - Tells the compiler "this function describes teh UI, not normal code"
        - It gets compiled into a declarative UI node
        - Compose knows when to recompse it when states change
        - Describes how its drawn on Android, iOS, DeskTop, and Web
        - Compose can optimize it, skip re-rendering, or redraw specific parts. 
    - Without it, function is just normal Kotlin function; Compose cannot treat it as UI.

### @Composable Function Signature
Example Code:
```kotlin
@Composable
fun LoginScreen(
    onLoginClick: (email: String, password: string) -> Unit = { _, _ -> }, 
    onForgotPasswordClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    //...
}
```
- `@Composable`: Marks this as a composable function -> describes UI in Compose.
- `fun LoginScreen(..)`: This is the login screen component.
- `onLoginClick`: A callback that takes an email and password. Default will be an empty lambda that does nothing
- `onForgotPasswordClick`: Callback when "Forgot password?" is clicked, default does nothing.
- `onSignUpClick`: Callback when "Sign Up now" is clicked, default is nothing. 
- The defaults make it easy to preview or use this composable without wiring up real logic immediately.
- `_` == means "I don't care about this input"

#### WHY it looks weird
- The function parameters are functions -- NOT Integers, Strings, or Booleans.

Example of a normal param:
```kotlin
fun doSomething(name: String) {}
```

Kotlin also allows:
```kotlin
fun doSomething(callback: () -> Unit)

// Which means callback is a function with no parameters that return nothing.
```

#### Why do this?
Because your UI composable should NOT contain business logic.

Example:
Inside your screen you will have buttons like:
```kotlin
Button(onClick = { onLoginClick(email, password) }) { //... }
```

But the actual logic (like Firebase login, Drexel API login, etc.) will come from parent screen, not this UI.

### Local state (email & password)
Example:
```kotlin
var email by remember { mutableStateOf("") 
var password by remember { mutableStateOf("") }
```
- `remember { mutableStateOf("") }`: Create state that Compose tracks and remember between recompositions.

- Ken said I'm not the right type of asian so I'm not invited to the asian friendsgiving

# 11/10/2025

Tried developing more not working

- SDK broken nothing works.
- Kennan said something weird about my skin-color.

# 10/5/2025

- Developing Kotlin Multiplatform

Got it to set up.

I went to this page to download the plugin

[Plugin Link](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform?_gl=1%2A5nmlkh%2A_gcl_aw%2AR0NMLjE3NTk2ODk2MzkuQ2owS0NRandyb2pIQmhEZEFSSXNBSmRFSl9jbzlqd2wtaE01ZUlSM3RsbnQ4OWZlRGZjREx3MGNhT3AyYTQ1MlFwa3dyUVJfZWV3SzNUZ2FBc3hGRUFMd193Y0I.%2A_gcl_au%2AMTU1OTY1NjY0OC4xNzU5NTEyNjU4LjQ2NTY5OTIyOS4xNzU5Njg5OTU3LjE3NTk2ODk5NTY.%2AFPAU%2AMjk4MjQwNjQwLjE3NTk1MTI2Nzk.%2A_ga%2AMTk1NjQ5NDYzMC4xNzU5NTEyNjU5%2A_ga_9J976DJZ68%2AczE3NTk2ODk0NjMkbzIkZzEkdDE3NTk2OTAxNTgkajU5JGwwJGgw)

Ignore the rating ;-)

I downloaded the zip package

Nvm tried pressing button computer crashed using some middle zip file they afftered.

# 10/3/2025

## Pipeline Stuff

- Create first pipeline to work off of.
    - Simple and only focuses on creating test cases for the files we start with.

- Updating README so it will be easy for us to follow professional standards when it comes to commits and etc.

- Trying this new pipeline format that adds this weekly security check.
    - It's new so I'm excited to see if I did it right and what I have done, so let's hope for the best.

## Creating Kotlin Base for Multiplatform

[Documentation Link](https://www.jetbrains.com/help/kotlin-multiplatform-dev/quickstart.html#set-up-the-environment)

# 11/2/2025

- Initialize the SQLite Database
- [How to Setup SQLite Database](https://chatgpt.com/share/6907f926-9e04-800e-8063-528f38672896)

Setting Up Database:
```bash

sqlite3 findmyride.db < database/schema.sql
sqlite3 findmyride.db
sqlite> .tables
```

Injecting Database Information:
```bash

sqlite3 findmyride.db < database/teamDeliverable2_DDL.sql
sqlite3 findmyride.db < database/populate.sql
```