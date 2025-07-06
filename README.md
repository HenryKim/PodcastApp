# 🎧 Podcast App

A sample **Podcast Browsing Application** built entirely with **Kotlin** and the latest **Android development tools**.  
This project showcases a **scalable and maintainable architecture** suitable for modern Android applications.

---

## ✨ Features

- 🔄 **Infinite Scrolling** of top podcasts
- 🔃 **Pull to Refresh** to fetch the latest data from the server
- 🔍 **Podcast Detail View** with full descriptions and high-quality images
- ⭐ **Mark as Favorite** to save your favorite podcasts locally

---

## 🛠️ Tech Stack

- **100% Kotlin**
- **Jetpack Compose** for declarative UI
- **MVVM Architecture** for clear separation of concerns
- **Repository Pattern** to abstract data sources

---

## 🧩 Jetpack & Third-Party Libraries

| Library       | Purpose                                      |
|---------------|----------------------------------------------|
| **Hilt**      | Dependency Injection                         |
| **Retrofit**  | Type-safe HTTP client for API calls          |
| **Paging 3**  | Efficient data pagination and scroll handling |
| **ViewModel** | Lifecycle-aware UI data management           |

---

## 📦 Architecture

The app follows **Clean Architecture principles** with:

- **UI Layer**: Built using Jetpack Compose
- **ViewModel Layer**: Handles UI logic and exposes state
- **Repository Layer**: Abstracts network and local data sources
- **Data Layer**: Interacts with remote API using Retrofit

---

## 🚀 Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/HenryKim/PodcastApp.git
2. Open in Android Studio (recommended version: Hedgehog or newer)

3. Build & run the app on an emulator or device
