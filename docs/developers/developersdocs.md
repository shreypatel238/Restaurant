# Developer Documentation - Restaurant Catalog

This documentation provides developer the necessary information to maintain, extend, and contribute the Restaurant Catalog.

## System Architecture
The code base is split into two main part: The backend and the frontend. Our backend code handles storing data, as well as modification and additions of data. Our backend also has helper classes to help store the data. Our frontend code takes in the data from the back and shows it to the user through a GUI. Backend functions are called when the user interact with certain parts. This will be expaneded in the next section. </br>

The system is written in Java, using Java Swing for the GUI. JUnit is also used for our testing suites.

## Codebase Documentation
Our Code files consist of:
- `HomePage.java`
   - Front end code of the main page containing a list of restaurant, and where user can interact with the data.
   - **This where `main()` function is, and the entry point of the program. Execute this file to run the program**
- `LoginPage.java`
  - Front end code for user login and register for account
- `Backend.java`
  - Main back end code to handle data. Functions here interact with the data. Functions here are called by front end.
- `Restaurant.java`
  - Class file to represent restaurant data as objects
- `User.java`
  - Class file to represent a user as a object

## Build Instructions

## Testing Instructions
