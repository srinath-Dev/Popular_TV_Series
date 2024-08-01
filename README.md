Popular TV Series App
A Kotlin-based Android app that showcases popular TV series using a clean architecture pattern. The app features an infinite scrolling grid of TV series, detailed information about each series, and functionality for pagination and error handling.

Features
Infinite Scrolling: Load TV series data dynamically as the user scrolls.
Detailed View: Display detailed information about each TV series, including poster image, overview, release date, rating, and popularity.
Search and Sort: Search and sort TV series by name or rating.
Error Handling: Display appropriate messages for loading errors.
Libraries and Tools
Jetpack Compose: For building the UI with a declarative approach.
Coil: For image loading and caching.
Kotlin Coroutines: For managing asynchronous operations.
Flow: For handling reactive data streams.
Room: For local database storage of TV series data.
Retrofit: For network operations and API interactions.
Hilt: For dependency injection.
Architecture
The app follows the clean architecture principles, separating concerns into distinct layers:

UI Layer: Composed of @Composable functions that render the app's user interface.
ViewModel Layer: Manages UI-related data and state, performs business logic, and interacts with repositories.
Repository Layer: Handles data operations and provides data to the ViewModel.
Data Layer: Consists of API interfaces and local database entities for data storage and retrieval.
Setup
Clone the repository:

bash
Copy code
git clone https://github.com/your-username/popular-tv-series-app.git
Open the project in Android Studio.

Sync the project with Gradle files and build the project.

Run the app on an emulator or physical device.

Usage
Main Screen: Displays a grid of TV series. Use the search bar to filter series and the sort option to reorder them.
Detail Screen: Provides detailed information about a selected TV series.
Contributing
Feel free to submit issues or pull requests. Contributions are welcome!

License
This project is licensed under the MIT License.

Developed by
Srinath - srinathdev.me
