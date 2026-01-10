# Readify

Readify is an Android application that displays articles. The app is built using **MVI (Model-View-Intent) architecture** and **Jetpack Compose** for modern UI development.

## Architecture

The app follows a **modular architecture** organized by features, implementing the **MVI pattern** for state management. The architecture includes:

- **MVI (Model-View-Intent) Architecture**: State, Events, and Effects pattern for predictable state management
- **Modularization by Feature**: Each feature is separated into independent modules
- **Room Database**: Local data persistence and caching
- **Hilt**: Dependency injection for managing dependencies across modules
- **Custom Pagination**: Full control over pagination logic without external pagination libraries

## Project Structure

### Core Module

The core module contains shared logic and infrastructure used across the entire application:

#### `core:presentation`
Contains common presentation layer classes and utilities:
- **BaseViewModel**: Abstract base ViewModel implementing MVI pattern with State, Event, and Effect handling
- Shared presentation logic and utilities

#### `core:data`
Contains common data layer infrastructure:
- **Retrofit**: HTTP client configuration and setup
- Common data models and network utilities
- Shared repository interfaces

### Design System Module

The `designsystem` module provides a centralized design system for the app:
- Shared UI composables used across all features
- Theme configuration (colors, typography, spacing)
- Reusable components (buttons, cards, indicators, etc.)
- Design tokens and style guidelines

### Features Module

Each feature follows a clean architecture approach with three layers:

#### Feature Structure (Example: `articles`)

```
features/
  └── articles/
      ├── data/
      │   ├── local/        # Room database, entities, DAOs
      │   ├── remote/       # API services, models
      │   ├── repository/   # Repository implementation
      │   └── di/           # Data layer dependency injection
      │
      ├── domain/
      │   ├── model/        # Domain models
      │   └── usecase/      # Business logic use cases
      │
      └── presentation/
          ├── ui/           # Compose screens and components
          ├── viewmodel/    # ViewModels extending BaseViewModel
          ├── model/        # UI models (State, Event, Effect)
          └── navigation/   # Feature navigation
```

#### `features:articles:data`
- Room database entities and DAOs
- Retrofit API service interfaces
- Data models (DTOs)
- Repository implementation
- Data mappers for converting between layers

#### `features:articles:domain`
- Domain models (business entities)
- Use cases (business logic)
- Repository interfaces

#### `features:articles:presentation`
- Compose UI screens and components
- ViewModels implementing MVI pattern
- UI models (State, Event, Effect)
- Navigation logic


## Modules Overview

```
Readify/
├── app/                           # Main application module
├── core/
│   ├── presentation/              # BaseViewModel and shared presentation logic
│   └── data/                      # Retrofit and shared data infrastructure
├── designsystem/                  # Shared UI components and design system
└── features/
    └── articles/
        ├── data/                  # Data layer (Room, Retrofit, Repository)
        ├── domain/                # Domain layer (Models, Use Cases)
        └── presentation/          # Presentation layer (UI, ViewModel, Navigation)
```

## Technology Stack

- **Kotlin**: Primary programming language
- **Jetpack Compose**: Modern declarative UI framework
- **MVI Architecture**: State management pattern
- **Hilt**: Dependency injection
- **Room**: Local database
- **Retrofit**: HTTP client
- **Kotlin Coroutines & Flow**: Asynchronous programming
- **Kotlinx Serialization**: JSON serialization
- **Material 3**: Design system components
- **Navigation Compose**: Type-safe navigation


## Running the App

To run the app, you should add the following to your `local.properties` file:

```properties
API_KEY=your_actual_api_key_here
```

