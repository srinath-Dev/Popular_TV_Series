<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

</head>
<body>

<h1>Popular TV Series App</h1>


<table>
  <tr>
    <td>Home Screen With Pagination</td>
     <td>Series Detail</td>
     <td>Search</td>
    <td>Sort</td>
  </tr>
  <tr>
<td><img src="https://github.com/user-attachments/assets/49dd6c6f-4c1d-4d8c-937c-c3b38c4b03c9" width="200" /> </td>
<td><img src="https://github.com/user-attachments/assets/1eef03c8-b31d-4630-bf73-60c9fb28f789" width="200" /></td>
<td><img src="https://github.com/user-attachments/assets/52ae40b3-0ddf-4df9-93f9-f00bfd662a26" width="200" /></td>
<td><img src="https://github.com/user-attachments/assets/a0966346-7140-4125-bf9f-3430a611de61" width="200" /></td>

 </tr>
 
 </table>

<p>A Kotlin-based Android app that showcases popular TV series using a clean architecture pattern. The app features an infinite scrolling grid of TV series, detailed information about each series, and functionality for pagination and error handling.</p>

<h2>Features</h2>
<ul>
    <li><strong>Infinite Scrolling</strong>: Load TV series data dynamically as the user scrolls.</li>
    <li><strong>Detailed View</strong>: Display detailed information about each TV series, including poster image, overview, release date, rating, and popularity.</li>
    <li><strong>Search and Sort</strong>: Search and sort TV series by name or rating.</li>
    <li><strong>Error Handling</strong>: Display appropriate messages for loading errors.</li>
</ul>

<h2>Libraries and Tools</h2>
<ul>
    <li><strong>Jetpack Compose</strong>: For building the UI with a declarative approach.</li>
    <li><strong>Coil</strong>: For image loading and caching.</li>
    <li><strong>Kotlin Coroutines</strong>: For managing asynchronous operations.</li>
    <li><strong>Flow</strong>: For handling reactive data streams.</li>
    <li><strong>Room</strong>: For local database storage of TV series data.</li>
    <li><strong>Retrofit</strong>: For network operations and API interactions.</li>
    <li><strong>Hilt</strong>: For dependency injection.</li>
</ul>

<h2>Architecture</h2>
<p>The app follows the clean architecture principles, separating concerns into distinct layers:</p>
<ul>
    <li><strong>UI Layer</strong>: Composed of <code>@Composable</code> functions that render the app's user interface.</li>
    <li><strong>ViewModel Layer</strong>: Manages UI-related data and state, performs business logic, and interacts with repositories.</li>
    <li><strong>Repository Layer</strong>: Handles data operations and provides data to the ViewModel.</li>
    <li><strong>Data Layer</strong>: Consists of API interfaces and local database entities for data storage and retrieval.</li>
</ul>

<h2>Setup</h2>
<ol>
    <li>Clone the repository:
        <pre><code>git clone https://github.com/your-username/popular-tv-series-app.git</code></pre>
    </li>
    <li>Open the project in Android Studio.</li>
    <li>Sync the project with Gradle files and build the project.</li>
    <li>Run the app on an emulator or physical device.</li>
</ol>

<h2>Usage</h2>
<ul>
    <li><strong>Main Screen</strong>: Displays a grid of TV series. Use the search bar to filter series and the sort option to reorder them.</li>
    <li><strong>Detail Screen</strong>: Provides detailed information about a selected TV series.</li>
</ul>

<h2>Contributing</h2>
<p>Feel free to submit issues or pull requests. Contributions are welcome!</p>

<h2>License</h2>
<p>This project is licensed under the MIT License.</p>

<h2>Developed by</h2>
<p>Srinath - <a href="https://www.srinathdev.me/">srinathdev.me</a></p>

</body>
</html>
