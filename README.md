# Bandits Badminton Manager

A comprehensive Android application for managing badminton group activities with player tracking, match recording, intelligent rating system, and detailed statistics.

## Features

✅ **Player Management**
- Register players with name, nickname, skill level
- Track joining dates and activity status
- Search and filter players

✅ **Match Tracking**
- Record doubles matches (2v2)
- Automatic duplicate detection
- Match history with date filtering

🚧 **Weighted Rating System** (In Progress)
- Modified ELO algorithm
- Considers score differences
- Adjusts for player experience
- Automatic rating updates after each match

🚧 **Statistics & Analytics** (In Progress)
- Individual player statistics
- Team combination performance
- Win/loss percentages
- Points scored/conceded tracking
- Rating history and trends

🚧 **Data Management** (In Progress)
- JSON export/import
- Backup and restore functionality
- Data integrity validation

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose with Material3
- **Database**: Room (SQLite)
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Async**: Kotlin Coroutines + Flow
- **Serialization**: Kotlinx Serialization
- **Navigation**: Navigation Compose

## Project Status

**Current Progress**: 45% Complete

### Completed ✅
- Project setup and configuration
- Database layer (entities, DAOs, Room)
- Domain models and repository interfaces
- Player & Match repository implementations
- Material3 theme
- Basic app structure

### In Progress 🚧
- Rating algorithm implementation
- Use cases (business logic)
- UI screens and navigation
- Statistics calculation
- Export/Import functionality

### Pending ❌
- UI implementation
- Testing suite
- Performance optimization
- Documentation

## Build Instructions

### Prerequisites
- Android Studio (latest version)
- Android SDK (API 26-34)
- JDK 17

### Building
```bash
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### Running
1. Open project in Android Studio
2. Sync Gradle
3. Run on emulator or device (API 26+)

## Project Structure

```
com.bandits.badmintonmanager/
├── data/               # Data layer
│   ├── local/         # Room database, DAOs, entities
│   ├── repository/    # Repository implementations
│   └── mapper/        # Entity-Domain mappers
├── domain/            # Business logic layer
│   ├── model/        # Domain models
│   ├── repository/   # Repository interfaces
│   └── usecase/      # Business logic use cases
├── presentation/      # UI layer
│   ├── screens/      # Compose screens
│   ├── components/   # Reusable UI components
│   ├── navigation/   # Navigation setup
│   └── theme/        # Material3 theme
└── di/               # Dependency injection modules
```

## Architecture

This app follows **Clean Architecture** principles with three main layers:

1. **Presentation Layer**: Jetpack Compose UI + ViewModels
2. **Domain Layer**: Business logic, use cases, domain models
3. **Data Layer**: Room database, repositories, mappers

**Data Flow**: `UI → ViewModel → UseCase → Repository → DAO → Database`

## Database Schema

### Players
- Player info (name, nickname, skill level)
- Current rating (starts at 1000.0)
- Joining date and active status

### Matches
- Match date/time
- Two teams (2 players each)
- Scores and winning team
- Linked to 4 players via foreign keys

### Rating History
- Historical rating changes
- Links to player and match
- Tracks rating before/after each match

## Rating Algorithm

Modified ELO system with enhancements:
- Base K-factor: 32.0
- Score difference multiplier: 0.1 per point
- Experience-based K-factor adjustment
- Team average used for calculations

## Development Roadmap

See [PENDING_WORK.md](PENDING_WORK.md) for detailed implementation plan.

### Priority Order
1. **Phase 4**: Rating system (CRITICAL)
2. **Phase 5**: Core use cases
3. **Phase 8**: Match entry UI
4. **Phase 9**: Dashboard & statistics
5. **Phase 6-7**: Player management UI
6. **Phase 10-11**: Export & polish

## Contributing

When resuming development:
1. Review [PENDING_WORK.md](PENDING_WORK.md)
2. Start with Phase 4 (Rating Algorithm)
3. Follow the architecture patterns established
4. Write unit tests for business logic
5. Test thoroughly before moving to next phase

## License

This project is for personal/group use.

## Support

For Android development questions:
- [Android Developers](https://developer.android.com/)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/android)
- [Kotlin Slack](https://kotlinlang.org/community/)

---

**Last Updated**: December 16, 2025
**Version**: 0.5.0 (Pre-release)
**Status**: Active Development
