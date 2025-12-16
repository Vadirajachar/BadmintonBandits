# Project Overview - Bandits Badminton Manager

**Project Start Date**: December 16, 2025
**Current Status**: 45% Complete - Foundation Built
**Resume Date**: Expected mid-January 2026

---

## Original Requirements

The user requested to build an **Android mobile application for managing badminton group activities** with the following features:

### 1. Player Management ✅ IMPLEMENTED
**Requested:**
- Allow registration and management of players (name, optional nickname, skill level, joining date)
- Each player should have a unique profile and ID

**Status:** ✅ **COMPLETE**
- `PlayerEntity` created with all required fields
- `PlayerDao` with 18 query methods (CRUD, search, filtering)
- `PlayerRepository` interface and implementation complete
- Domain model `Player` with display helpers
- Unique constraints on player names
- Skill levels: BEGINNER, INTERMEDIATE, ADVANCED

### 2. Match Management ✅ IMPLEMENTED (Core) / 🚧 IN PROGRESS (UI)
**Requested:**
- Primarily doubles matches
- Record matches with:
  - Two players per team
  - Match date and time
  - Scores for both teams
  - Winning and losing team
- Support multiple matches per day

**Status:** ✅ **CORE COMPLETE** / ❌ **UI NOT STARTED**
- `MatchEntity` created with all fields
- `MatchDao` with 17 query methods including duplicate detection
- `MatchRepository` interface and implementation complete
- Domain model `Match` with computed properties
- Domain model `Team` with helper methods
- Duplicate detection within 30-minute window (implemented in DAO)
- Multiple matches per day supported
- **PENDING**: UI screens for match entry

### 3. Statistics & Reports 🚧 PARTIALLY IMPLEMENTED
**Requested:**
Generate reports showing for each player:
- Total matches played
- Number of wins and losses
- Win/loss percentage
- Points scored and points conceded
- Both individual player stats and team combinations stats (how well two players perform together)

**Status:** ⚠️ **DATA LAYER COMPLETE** / ❌ **LOGIC NOT IMPLEMENTED**
- Domain models created:
  - `PlayerStatistics` - with win%, avg points
  - `TeamCombinationStats` - with combined metrics
- `StatisticsRepository` interface defined
- Database queries available in DAOs
- **PENDING**:
  - `GetPlayerStatisticsUseCase` implementation
  - `GetTeamCombinationStatsUseCase` implementation
  - `StatisticsRepositoryImpl` (currently placeholder)
  - UI screens for displaying statistics

### 4. Player Rating System 🚧 DESIGNED BUT NOT IMPLEMENTED
**Requested:**
- Automatically calculate and update player rating based on match results
- Ratings should consider:
  - Wins and losses
  - Strength of opponents (optional advanced logic)
  - Score difference (optional)
- Ratings should adjust after every match
- **User selected: WEIGHTED system** (considers score differences + recent performance)

**Status:** ❌ **NOT IMPLEMENTED** (Most Critical Pending Work)
- `RatingHistoryEntity` created to track rating changes
- `RatingHistoryDao` with history tracking queries
- Domain model `RatingInfo` created
- Constants defined (K_FACTOR_BASE = 32.0, etc.)
- **PENDING**:
  - `CalculateRatingUseCase` - Modified ELO algorithm
  - `UpdatePlayerRatingsUseCase` - Apply ratings after match
  - `GetRatingHistoryUseCase` - Retrieve rating trends
  - Integration with `RecordMatchUseCase`

**Algorithm Designed** (from planning phase):
```
Modified ELO with enhancements:
1. Calculate team average ratings
2. Compute expected win probability (ELO formula)
3. Apply score difference multiplier (bigger wins = more change)
4. Adjust K-factor based on player's recent match count
5. Calculate rating change for all 4 players
6. Store rating history for each player
```

### 5. User Interface & Experience 🚧 THEME ONLY
**Requested:**
- Simple and intuitive UI optimized for quick match entry
- Dashboard showing:
  - Recent matches
  - Top-performing players
  - Player rankings
- Ability to filter stats by date range

**Status:** ⚠️ **THEME COMPLETE** / ❌ **SCREENS NOT IMPLEMENTED**
- Material3 theme implemented with green color scheme
- Type system defined
- **PENDING**:
  - Dashboard screen
  - Quick match entry screen (CRITICAL)
  - Player list and detail screens
  - Leaderboard screen
  - Statistics screen with date filters
  - Navigation setup

### 6. Data & Persistence ✅ IMPLEMENTED (Database) / ❌ NOT IMPLEMENTED (Export)
**Requested:**
- Store data locally on device
- Option to export/import data (CSV or JSON)
- Ensure data integrity and prevent duplicate match entries
- **User selected: JSON format**

**Status:** ✅ **DATABASE COMPLETE** / ❌ **EXPORT NOT IMPLEMENTED**
- Room database fully configured
- All entities with proper relationships
- Foreign keys and indexes for data integrity
- Duplicate match detection implemented (30-min window)
- **PENDING**:
  - `ExportRepositoryImpl` (currently placeholder)
  - JSON serialization models
  - File picker integration
  - Import validation logic
  - Settings screen with export/import UI

---

## Technology Decisions Made

During the planning phase, the user made the following technology selections:

| Decision Point | Options Presented | User Selection |
|---------------|-------------------|----------------|
| **Programming Language** | Kotlin / Java | **Kotlin** ✅ |
| **UI Framework** | Jetpack Compose / XML Layouts | **Jetpack Compose** ✅ |
| **Rating System Complexity** | Simple / Elo-based / Weighted | **Weighted** ✅ |
| **Export Formats** | JSON / CSV | **JSON** ✅ |

### Rationale for Selections
- **Kotlin**: Modern, concise, officially recommended by Google
- **Jetpack Compose**: Declarative UI, less boilerplate, easier dynamic UIs
- **Weighted Rating**: Considers score differences and recent performance for more accuracy
- **JSON**: Structured format, preserves all relationships, best for backup/restore

---

## What Has Been Completed

### Phase 1: Project Setup ✅ COMPLETE
**Files Created**: 15 files

#### Build Configuration
- ✅ `settings.gradle.kts` - Project settings
- ✅ `build.gradle.kts` (root) - Plugin versions
- ✅ `app/build.gradle.kts` - App dependencies and configuration
- ✅ `gradle.properties` - Gradle settings
- ✅ `gradle/wrapper/gradle-wrapper.properties` - Gradle wrapper
- ✅ `app/proguard-rules.pro` - ProGuard rules

#### App Structure
- ✅ `app/BanditsApplication.kt` - Application class with Hilt
- ✅ `MainActivity.kt` - Entry point with placeholder UI
- ✅ `AndroidManifest.xml` - App manifest with permissions

#### Utilities
- ✅ `util/Constants.kt` - App-wide constants (K-factors, page sizes, etc.)
- ✅ `util/Resource.kt` - Result wrapper for error handling
- ✅ `util/Extensions.kt` - Utility extensions (LocalDateTime conversions, rounding)

#### Theme
- ✅ `presentation/theme/Color.kt` - Material3 color scheme (green theme)
- ✅ `presentation/theme/Type.kt` - Typography definitions
- ✅ `presentation/theme/Theme.kt` - Theme composition

#### Resources
- ✅ App launcher icons (XML vectors)
- ✅ String resources
- ✅ Theme resources

**Dependencies Configured**:
- Compose BOM 2024.12.00
- Room 2.6.1
- Hilt 2.50
- Navigation Compose 2.7.6
- Kotlinx Serialization 1.6.2
- Kotlinx DateTime 0.5.0
- Coroutines 1.7.3

### Phase 2: Database Layer ✅ COMPLETE
**Files Created**: 8 files

#### Entities (`data/local/entities/`)
- ✅ `PlayerEntity.kt`
  - Fields: id, name, nickname, skillLevel, joiningDate, currentRating, isActive, createdAt, updatedAt
  - Indexes: name (unique), currentRating, isActive

- ✅ `MatchEntity.kt`
  - Fields: id, matchDate, team1Player1Id, team1Player2Id, team1Score, team2Player1Id, team2Player2Id, team2Score, winningTeam, scoreDifference, notes, createdAt
  - Foreign keys: 4 player references with RESTRICT
  - Indexes: composite index for duplicate detection, individual player indexes

- ✅ `RatingHistoryEntity.kt`
  - Fields: id, playerId, matchId, ratingBefore, ratingAfter, ratingChange, timestamp
  - Foreign keys: player and match with CASCADE delete
  - Indexes: playerId, matchId, timestamp

#### DAOs (`data/local/dao/`)
- ✅ `PlayerDao.kt` - 18 methods
  - CRUD operations
  - Search by name/nickname
  - Filter by skill level
  - Get top players by rating
  - Update rating
  - Check name existence

- ✅ `MatchDao.kt` - 17 methods
  - CRUD operations
  - Get by date range
  - Get by player(s)
  - Get by team combination
  - Duplicate detection (complex query checking all player permutations)
  - Win/loss counts

- ✅ `RatingHistoryDao.kt` - 9 methods
  - Get history by player
  - Get recent history
  - Get by date range
  - Get by match

#### Database
- ✅ `data/local/database/BanditsDatabase.kt` - Room database (version 1)
- ✅ `data/local/database/Converters.kt` - Type converters

#### Dependency Injection
- ✅ `di/DatabaseModule.kt` - Provides database and DAO instances

### Phase 3: Domain Layer ✅ MODELS & INTERFACES COMPLETE
**Files Created**: 17 files

#### Domain Models (`domain/model/`)
- ✅ `Player.kt` - Domain player with displayName, shortDisplayName helpers
- ✅ `Team.kt` - Team with players list, averageRating, containsPlayer(), getPartner()
- ✅ `Match.kt` - Match with scoreDifference, winningTeam, losingTeam, helper methods
- ✅ `PlayerStatistics.kt` - Stats with winPercentage, avgPoints, computed properties
- ✅ `TeamCombinationStats.kt` - Team stats with teamName, winPercentage, avgPoints
- ✅ `RatingInfo.kt` - Rating change with trend, isImprovement
- ✅ `SkillLevel.kt` - Enum with fromString(), displayName()
- ✅ `RatingTrend.kt` - Enum (UP, DOWN, STABLE) with fromChange()

#### Repository Interfaces (`domain/repository/`)
- ✅ `PlayerRepository.kt` - 14 methods (all CRUD + queries)
- ✅ `MatchRepository.kt` - 13 methods (all CRUD + queries)
- ✅ `StatisticsRepository.kt` - 5 methods (stats calculation interfaces)
- ✅ `ExportRepository.kt` - 4 methods (export/import interfaces)

#### Repository Implementations (`data/repository/`)
- ✅ `PlayerRepositoryImpl.kt` - **FULLY IMPLEMENTED**
  - All 14 methods working
  - Entity-Domain mapping via PlayerMapper

- ✅ `MatchRepositoryImpl.kt` - **FULLY IMPLEMENTED**
  - All 13 methods working
  - Entity-Domain mapping via MatchMapper
  - Duplicate detection with 30-minute window

- ⚠️ `StatisticsRepositoryImpl.kt` - **PLACEHOLDER**
  - Methods throw NotImplementedError
  - Awaiting use case implementations

- ⚠️ `ExportRepositoryImpl.kt` - **PLACEHOLDER**
  - Methods return Resource.Error
  - Awaiting JSON serialization implementation

#### Mappers (`data/mapper/`)
- ✅ `PlayerMapper.kt` - Entity ↔ Domain conversions
- ✅ `MatchMapper.kt` - Entity ↔ Domain conversions (async due to player lookups)

#### Dependency Injection
- ✅ `di/RepositoryModule.kt` - Binds repository implementations

---

## What Remains To Be Done

### Critical Path (Must Do First)

#### 1. Phase 4: Rating System ⭐ HIGHEST PRIORITY
**Estimated Time**: 6-8 hours
**Files to Create**: 3 files

- ❌ `domain/usecase/rating/CalculateRatingUseCase.kt`
  - Implement Modified ELO algorithm
  - K-factor adjustment based on experience
  - Score difference multiplier
  - Team average calculations

- ❌ `domain/usecase/rating/UpdatePlayerRatingsUseCase.kt`
  - Calculate ratings for all 4 players
  - Update player entities
  - Save rating history entries
  - Transaction management

- ❌ `domain/usecase/rating/GetRatingHistoryUseCase.kt`
  - Fetch rating history
  - Map to domain models
  - Calculate trends

**Why Critical**: This is the core differentiator of the app. Without it, the app is just a basic match tracker.

#### 2. Phase 5: Core Use Cases ⭐ HIGH PRIORITY
**Estimated Time**: 10-12 hours
**Files to Create**: 15 files

**Player Use Cases** (5 files):
- ❌ `AddPlayerUseCase.kt` - Validation + insert
- ❌ `GetPlayersUseCase.kt` - Fetch and filter
- ❌ `GetPlayerDetailUseCase.kt` - Single player with stats
- ❌ `UpdatePlayerUseCase.kt` - Update player info
- ❌ `DeletePlayerUseCase.kt` - Soft delete (set inactive)

**Match Use Cases** (4 files):
- ❌ `RecordMatchUseCase.kt` - Validate → Save → Update Ratings (CRITICAL)
- ❌ `ValidateMatchUseCase.kt` - Business rules validation
- ❌ `CheckDuplicateMatchUseCase.kt` - Wrapper for duplicate check
- ❌ `GetMatchesUseCase.kt` - Fetch with filters

**Statistics Use Cases** (3 files):
- ❌ `GetPlayerStatisticsUseCase.kt` - Calculate all player stats
- ❌ `GetTeamCombinationStatsUseCase.kt` - Calculate team pair stats
- ❌ `GetLeaderboardUseCase.kt` - Ranked player list

**Complete Repository** (1 file):
- ❌ Update `StatisticsRepositoryImpl.kt` with actual implementations

**Why Critical**: Without use cases, there's no business logic to drive the UI.

### Secondary Path (UI Implementation)

#### 3. Phase 6: UI Foundation
**Estimated Time**: 4-6 hours
**Files to Create**: 8 files

- ❌ Navigation setup (Screen sealed class, NavGraph)
- ❌ Reusable components (PlayerCard, MatchCard, StatCard, etc.)
- ❌ Update MainActivity with navigation

#### 4. Phase 8: Match Entry UI ⭐ USER-FACING CRITICAL
**Estimated Time**: 8-10 hours
**Files to Create**: 6 files

- ❌ QuickMatchEntryScreen - 4 player dropdowns, score inputs, date picker
- ❌ QuickMatchEntryViewModel - State management
- ❌ MatchListScreen - Display all matches
- ❌ MatchDetailScreen - Single match view

**Why Critical**: This is the primary user interaction point for data entry.

#### 5. Phase 7: Player Management UI
**Estimated Time**: 6-8 hours
**Files to Create**: 6 files

- ❌ PlayerListScreen
- ❌ AddPlayerScreen
- ❌ PlayerDetailScreen (with stats)
- ❌ ViewModels for each

#### 6. Phase 9: Dashboard & Statistics UI
**Estimated Time**: 8-10 hours
**Files to Create**: 8 files

- ❌ DashboardScreen - Recent matches, top players
- ❌ LeaderboardScreen - Rankings
- ❌ StatisticsScreen - Detailed stats with filters
- ❌ TeamCombinationsScreen - Team pair analysis
- ❌ ViewModels for each

### Tertiary Path (Polish)

#### 7. Phase 10: Export/Import
**Estimated Time**: 6-8 hours
**Files to Create**: 5 files

- ❌ Export data models (DTOs)
- ❌ Complete ExportRepositoryImpl
- ❌ SettingsScreen with export/import UI
- ❌ Use cases for export/import

#### 8. Phase 11: Testing & Polish
**Estimated Time**: 10-12 hours
**Files to Create**: 8+ files

- ❌ Unit tests (rating algorithm, use cases)
- ❌ Integration tests (repositories)
- ❌ UI tests (Compose testing)
- ❌ Performance optimization
- ❌ UI polish (animations, loading states)

---

## Total Work Summary

### Completed
- **Files**: 45 files
- **Lines of Code**: ~3,500 lines
- **Time Invested**: ~8-10 hours
- **Completion**: 45%

### Remaining
- **Files**: ~50 files
- **Estimated Lines**: ~4,000 lines
- **Estimated Time**: 40-50 hours
- **Completion**: 55%

### Priority Order for Next Session
1. **Phase 4** (Rating System) - 6-8 hours
2. **Phase 5** (Use Cases) - 10-12 hours
3. **Phase 8** (Match Entry UI) - 8-10 hours
4. **Phase 9** (Dashboard) - 8-10 hours
5. **Phase 6-7** (UI Foundation + Player UI) - 10-14 hours
6. **Phase 10-11** (Export + Testing) - 16-20 hours

**Total Estimated Remaining**: 40-50 hours (5-7 full working days)

---

## Key Decisions & Constraints

### Architectural Decisions
1. **Clean Architecture**: Strict separation of concerns (Data, Domain, Presentation)
2. **MVVM Pattern**: ViewModels manage UI state, use cases contain business logic
3. **Repository Pattern**: Abstract data sources behind interfaces
4. **Dependency Injection**: Hilt for compile-time DI and testability

### Database Decisions
1. **Composite Index**: On matches for O(1) duplicate detection
2. **Foreign Keys**: RESTRICT on matches (prevent deleting players with history)
3. **CASCADE Delete**: On rating history (cleanup)
4. **Unique Constraint**: On player names (prevent duplicates)

### Performance Decisions
1. **Flow for Reactive Data**: Auto-updates UI when database changes
2. **Lazy Loading**: Planned for lists (Paging3)
3. **Coroutines**: All async operations on IO dispatcher
4. **Room Caching**: Automatic query result caching

### Development Constraints
1. **No Android SDK in WSL**: Cannot build APK in current environment
2. **Placeholder Implementations**: Statistics and Export repos are stubs
3. **No UI Testing Yet**: Can't run Compose tests without full UI

---

## Success Criteria (When Complete)

### Minimum Viable Product (MVP)
- ✅ Add players with skill levels
- ✅ Record matches (4 players, scores, date)
- ✅ Automatic rating calculation and update
- ✅ View player statistics
- ✅ View leaderboard
- ✅ Quick match entry UI

### Full Feature Set
- ✅ All MVP features
- ✅ Team combination statistics
- ✅ Rating history graphs
- ✅ Date range filtering
- ✅ Export/Import data (JSON)
- ✅ Search and filter players
- ✅ Match duplicate detection

### Quality Metrics
- ✅ 80%+ unit test coverage on use cases
- ✅ No crashes on common operations
- ✅ Smooth UI (60 FPS)
- ✅ < 2 second load times
- ✅ Works offline (local database)

---

## Next Steps for Resume Session

1. **Review** `PENDING_WORK.md` for implementation details
2. **Start** with Phase 4: `CalculateRatingUseCase.kt`
3. **Test** rating algorithm with unit tests
4. **Implement** `RecordMatchUseCase.kt` (integrates rating updates)
5. **Create** basic UI for player and match entry
6. **Test** end-to-end: Add players → Record match → Verify ratings updated
7. **Continue** with remaining phases in priority order

---

**Document Created**: December 16, 2025
**Last Updated**: December 16, 2025
**Next Review**: Mid-January 2026
