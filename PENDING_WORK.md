# Bandits Badminton Manager - Pending Work

**Last Updated**: December 16, 2025
**Project Status**: 45% Complete (Foundation & Basic Infrastructure Ready)

---

## Quick Overview

This Android app manages badminton group activities with player management, match tracking, statistics, and a weighted rating system. The project uses **Kotlin, Jetpack Compose, Room, Hilt, and MVVM + Clean Architecture**.

### Current State
- ✅ Project setup complete
- ✅ Database layer fully implemented
- ✅ Domain models created
- ✅ Repository interfaces defined
- ⚠️ Repository implementations partially complete (Player & Match done, Statistics & Export are placeholders)
- ❌ Rating algorithm not implemented
- ❌ Use cases not implemented
- ❌ UI screens not implemented
- ❌ Navigation not implemented

---

## How to Build the APK

### Prerequisites
1. Install Android Studio (latest version)
2. Install Android SDK (API 26-34)
3. Install JDK 17

### Build Steps
```bash
cd /path/to/Bandits
./gradlew assembleDebug
```

**Output**: `app/build/outputs/apk/debug/app-debug.apk`

For release build:
```bash
./gradlew assembleRelease
```

### First Time Setup in Android Studio
1. Open Android Studio
2. File → Open → Select `/mnt/c/gitRepos/Bandits` folder
3. Let Gradle sync (may take 5-10 minutes first time)
4. Tools → SDK Manager → Ensure Android 14.0 (API 34) is installed
5. Run on emulator or physical device

---

## Completed Work (45 files)

### Phase 1: Project Setup ✅
- `build.gradle.kts` (root and app level)
- `settings.gradle.kts`
- `gradle.properties`
- `gradle/wrapper/gradle-wrapper.properties`
- `app/BanditsApplication.kt` (Hilt setup)
- `AndroidManifest.xml`
- Material3 theme (Color, Type, Theme)
- `util/Constants.kt`, `util/Resource.kt`, `util/Extensions.kt`
- App launcher icons and resources

### Phase 2: Database Layer ✅
**Entities** (`data/local/entities/`):
- `PlayerEntity.kt` - Player data with ratings, skill levels
- `MatchEntity.kt` - Match records with team compositions
- `RatingHistoryEntity.kt` - Rating change history

**DAOs** (`data/local/dao/`):
- `PlayerDao.kt` - 18 query methods (CRUD, search, top players, etc.)
- `MatchDao.kt` - 17 query methods (by date, player, team combo, duplicate detection)
- `RatingHistoryDao.kt` - 9 query methods (history tracking)

**Database**:
- `BanditsDatabase.kt` - Room database with 3 entities
- `Converters.kt` - Type converters for timestamp handling

**DI**:
- `di/DatabaseModule.kt` - Provides database and DAOs

### Phase 3: Domain Layer (Partial) ✅
**Models** (`domain/model/`):
- `Player.kt` - Domain player model with display helpers
- `Team.kt` - Team model with partner lookup, ratings
- `Match.kt` - Match model with win/loss calculations
- `PlayerStatistics.kt` - Stats with percentages and averages
- `TeamCombinationStats.kt` - Team pair performance
- `RatingInfo.kt` - Rating change tracking
- `SkillLevel.kt` - Enum (Beginner, Intermediate, Advanced)
- `RatingTrend.kt` - Enum (UP, DOWN, STABLE)

**Repository Interfaces** (`domain/repository/`):
- `PlayerRepository.kt` - 14 methods
- `MatchRepository.kt` - 13 methods
- `StatisticsRepository.kt` - 5 methods
- `ExportRepository.kt` - 4 methods

**Repository Implementations** (`data/repository/`):
- ✅ `PlayerRepositoryImpl.kt` - COMPLETE
- ✅ `MatchRepositoryImpl.kt` - COMPLETE
- ⚠️ `StatisticsRepositoryImpl.kt` - PLACEHOLDER (throws NotImplementedError)
- ⚠️ `ExportRepositoryImpl.kt` - PLACEHOLDER (returns errors)

**Mappers** (`data/mapper/`):
- ✅ `PlayerMapper.kt` - Entity ↔ Domain conversions
- ✅ `MatchMapper.kt` - Entity ↔ Domain conversions

**DI**:
- `di/RepositoryModule.kt` - Binds repository implementations

---

## Pending Work - Detailed Breakdown

### Phase 4: Rating System Implementation ⭐ CRITICAL
**Priority**: HIGH - This is the core differentiator of the app

#### 4.1 Rating Calculator (`domain/usecase/rating/`)
**File to create**: `CalculateRatingUseCase.kt`

**Algorithm** (Modified ELO with enhancements):
```kotlin
class CalculateRatingUseCase @Inject constructor() {

    companion object {
        const val K_FACTOR_BASE = 32.0
        const val SCORE_DIFF_MULTIPLIER = 0.1
        const val MAX_MATCHES_FOR_RECENCY = 10
    }

    suspend operator fun invoke(
        match: Match,
        playerRecentMatchCounts: Map<Long, Int>
    ): Map<Long, Double> {
        // 1. Calculate team average ratings
        val team1Avg = (match.team1.player1.currentRating + match.team1.player2.currentRating) / 2.0
        val team2Avg = (match.team2.player1.currentRating + match.team2.player2.currentRating) / 2.0

        // 2. Expected win probability (ELO formula)
        val team1Expected = 1.0 / (1.0 + Math.pow(10.0, (team2Avg - team1Avg) / 400.0))
        val team2Expected = 1.0 - team1Expected

        // 3. Actual outcome
        val team1Actual = if (match.winningTeamNumber == 1) 1.0 else 0.0
        val team2Actual = 1.0 - team1Actual

        // 4. Score difference multiplier (bigger wins = more rating change)
        val scoreDiff = match.scoreDifference
        val scoreDiffMultiplier = 1.0 + (scoreDiff * SCORE_DIFF_MULTIPLIER)

        // 5. Calculate rating changes for each player
        val newRatings = mutableMapOf<Long, Double>()

        match.team1.players.forEach { player ->
            val kFactor = calculateKFactor(playerRecentMatchCounts[player.id] ?: 0)
            val ratingChange = kFactor * scoreDiffMultiplier * (team1Actual - team1Expected)
            newRatings[player.id] = player.currentRating + ratingChange
        }

        match.team2.players.forEach { player ->
            val kFactor = calculateKFactor(playerRecentMatchCounts[player.id] ?: 0)
            val ratingChange = kFactor * scoreDiffMultiplier * (team2Actual - team2Expected)
            newRatings[player.id] = player.currentRating + ratingChange
        }

        return newRatings
    }

    private fun calculateKFactor(recentMatchCount: Int): Double {
        // New players: higher K-factor (faster adjustment)
        // Veterans: lower K-factor (more stable)
        return if (recentMatchCount < MAX_MATCHES_FOR_RECENCY) {
            K_FACTOR_BASE * (1.0 + ((MAX_MATCHES_FOR_RECENCY - recentMatchCount) * 0.05))
        } else {
            K_FACTOR_BASE
        }
    }
}
```

#### 4.2 Update Player Ratings Use Case
**File to create**: `UpdatePlayerRatingsUseCase.kt`

```kotlin
class UpdatePlayerRatingsUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val matchRepository: MatchRepository,
    private val ratingHistoryDao: RatingHistoryDao,
    private val calculateRatingUseCase: CalculateRatingUseCase
) {
    suspend operator fun invoke(match: Match) {
        // 1. Get recent match counts for all 4 players
        val recentMatchCounts = match.allPlayers.associate { player ->
            player.id to matchRepository.getMatchCountForPlayer(player.id).coerceAtMost(10)
        }

        // 2. Calculate new ratings
        val newRatings = calculateRatingUseCase(match, recentMatchCounts)

        // 3. Update player ratings and save history
        newRatings.forEach { (playerId, newRating) ->
            val player = playerRepository.getPlayerByIdSuspend(playerId) ?: return@forEach
            val oldRating = player.currentRating

            // Update player rating
            playerRepository.updatePlayerRating(playerId, newRating)

            // Save rating history
            ratingHistoryDao.insert(
                RatingHistoryEntity(
                    playerId = playerId,
                    matchId = match.id,
                    ratingBefore = oldRating,
                    ratingAfter = newRating,
                    ratingChange = newRating - oldRating,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}
```

#### 4.3 Get Rating History Use Case
**File to create**: `GetRatingHistoryUseCase.kt`

```kotlin
class GetRatingHistoryUseCase @Inject constructor(
    private val ratingHistoryDao: RatingHistoryDao
) {
    operator fun invoke(playerId: Long, limit: Int = 10): Flow<List<RatingInfo>> {
        return ratingHistoryDao.getRecentRatingHistoryForPlayer(playerId, limit)
            .map { entities ->
                entities.map { entity ->
                    RatingInfo(
                        playerId = entity.playerId,
                        currentRating = entity.ratingAfter,
                        ratingChange = entity.ratingChange,
                        previousRating = entity.ratingBefore,
                        matchId = entity.matchId,
                        timestamp = entity.timestamp.toLocalDateTime()
                    )
                }
            }
    }
}
```

### Phase 5: Core Use Cases Implementation

#### 5.1 Player Use Cases (`domain/usecase/player/`)

**Files to create**:

1. **`AddPlayerUseCase.kt`**
```kotlin
class AddPlayerUseCase @Inject constructor(
    private val playerRepository: PlayerRepository
) {
    suspend operator fun invoke(
        name: String,
        nickname: String?,
        skillLevel: SkillLevel,
        joiningDate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    ): Result<Long> {
        // Validation
        if (name.isBlank()) {
            return Result.failure(Exception("Player name cannot be empty"))
        }

        if (playerRepository.playerNameExists(name)) {
            return Result.failure(Exception("Player with name '$name' already exists"))
        }

        val player = Player(
            name = name.trim(),
            nickname = nickname?.trim(),
            skillLevel = skillLevel,
            joiningDate = joiningDate,
            currentRating = Constants.DEFAULT_RATING
        )

        return try {
            val id = playerRepository.addPlayer(player)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

2. **`GetPlayersUseCase.kt`**
3. **`GetPlayerDetailUseCase.kt`**
4. **`UpdatePlayerUseCase.kt`**
5. **`DeletePlayerUseCase.kt`** (set isActive = false)

#### 5.2 Match Use Cases (`domain/usecase/match/`)

**Files to create**:

1. **`RecordMatchUseCase.kt`** ⭐ CRITICAL
```kotlin
class RecordMatchUseCase @Inject constructor(
    private val matchRepository: MatchRepository,
    private val updatePlayerRatingsUseCase: UpdatePlayerRatingsUseCase,
    private val validateMatchUseCase: ValidateMatchUseCase
) {
    suspend operator fun invoke(match: Match): Result<Long> {
        // 1. Validate match
        val validationResult = validateMatchUseCase(match)
        if (validationResult.isFailure) {
            return validationResult.map { 0L }
        }

        // 2. Check for duplicates
        if (matchRepository.isDuplicateMatch(match)) {
            return Result.failure(Exception("Duplicate match detected"))
        }

        // 3. Save match
        val matchId = matchRepository.addMatch(match)
        val savedMatch = match.copy(id = matchId)

        // 4. Update player ratings
        try {
            updatePlayerRatingsUseCase(savedMatch)
        } catch (e: Exception) {
            // Rollback match if rating update fails
            matchRepository.deleteMatch(matchId)
            return Result.failure(Exception("Failed to update ratings: ${e.message}"))
        }

        return Result.success(matchId)
    }
}
```

2. **`ValidateMatchUseCase.kt`**
```kotlin
class ValidateMatchUseCase @Inject constructor() {
    operator fun invoke(match: Match): Result<Unit> {
        // Check scores are positive
        if (match.team1.score < 0 || match.team2.score < 0) {
            return Result.failure(Exception("Scores must be positive"))
        }

        // Check no player is on both teams
        val allPlayerIds = match.allPlayerIds
        if (allPlayerIds.size != allPlayerIds.distinct().size) {
            return Result.failure(Exception("A player cannot be on both teams"))
        }

        // Check match date is not in future
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        if (match.matchDate > now) {
            return Result.failure(Exception("Match date cannot be in the future"))
        }

        // Check winning team is correct
        val actualWinner = if (match.team1.score > match.team2.score) 1 else 2
        if (match.winningTeamNumber != actualWinner) {
            return Result.failure(Exception("Winning team does not match scores"))
        }

        return Result.success(Unit)
    }
}
```

3. **`GetMatchesUseCase.kt`**
4. **`CheckDuplicateMatchUseCase.kt`**

#### 5.3 Statistics Use Cases (`domain/usecase/statistics/`)

**Files to create**:

1. **`GetPlayerStatisticsUseCase.kt`** ⭐ CRITICAL
```kotlin
class GetPlayerStatisticsUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke(
        playerId: Long,
        startDate: LocalDateTime? = null,
        endDate: LocalDateTime? = null
    ): PlayerStatistics? {
        val player = playerRepository.getPlayerByIdSuspend(playerId) ?: return null

        // Get matches (with date filter if provided)
        val matches = if (startDate != null && endDate != null) {
            matchRepository.getMatchesByPlayerAndDateRange(playerId, startDate, endDate).first()
        } else {
            matchRepository.getMatchesByPlayer(playerId).first()
        }

        var wins = 0
        var losses = 0
        var pointsScored = 0
        var pointsConceded = 0

        matches.forEach { match ->
            val playerTeam = match.getTeamForPlayer(playerId)
            val opponentTeam = match.getOpposingTeam(playerId)

            if (playerTeam != null && opponentTeam != null) {
                pointsScored += playerTeam.score
                pointsConceded += opponentTeam.score

                if (match.didPlayerWin(playerId)) {
                    wins++
                } else {
                    losses++
                }
            }
        }

        return PlayerStatistics(
            player = player,
            totalMatches = matches.size,
            wins = wins,
            losses = losses,
            pointsScored = pointsScored,
            pointsConceded = pointsConceded,
            recentMatches = matches.take(10)
        )
    }
}
```

2. **`GetTeamCombinationStatsUseCase.kt`**
```kotlin
class GetTeamCombinationStatsUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke(
        player1Id: Long,
        player2Id: Long,
        startDate: LocalDateTime? = null,
        endDate: LocalDateTime? = null
    ): TeamCombinationStats? {
        val player1 = playerRepository.getPlayerByIdSuspend(player1Id) ?: return null
        val player2 = playerRepository.getPlayerByIdSuspend(player2Id) ?: return null

        val matches = matchRepository.getMatchesByTeamCombination(player1Id, player2Id).first()

        var wins = 0
        var losses = 0
        var pointsScored = 0
        var pointsConceded = 0

        matches.forEach { match ->
            val team = if (match.team1.containsPlayer(player1Id)) match.team1 else match.team2
            val opponentTeam = if (match.team1.containsPlayer(player1Id)) match.team2 else match.team1

            pointsScored += team.score
            pointsConceded += opponentTeam.score

            if ((team == match.team1 && match.winningTeamNumber == 1) ||
                (team == match.team2 && match.winningTeamNumber == 2)) {
                wins++
            } else {
                losses++
            }
        }

        return TeamCombinationStats(
            player1 = player1,
            player2 = player2,
            matchesPlayed = matches.size,
            wins = wins,
            losses = losses,
            pointsScored = pointsScored,
            pointsConceded = pointsConceded
        )
    }
}
```

3. **`GetLeaderboardUseCase.kt`**

#### 5.4 Complete StatisticsRepositoryImpl

**File to update**: `data/repository/StatisticsRepositoryImpl.kt`

Add dependencies:
```kotlin
class StatisticsRepositoryImpl @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val matchRepository: MatchRepository,
    private val ratingHistoryDao: RatingHistoryDao
) : StatisticsRepository {
    // Implement all methods using the use cases as reference
}
```

### Phase 6: UI Foundation

#### 6.1 Navigation (`presentation/navigation/`)

**Files to create**:

1. **`Screen.kt`**
```kotlin
sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object PlayerList : Screen("players")
    object PlayerDetail : Screen("players/{playerId}") {
        fun createRoute(playerId: Long) = "players/$playerId"
    }
    object AddPlayer : Screen("players/add")
    object QuickMatchEntry : Screen("match/quick")
    object MatchList : Screen("matches")
    object MatchDetail : Screen("matches/{matchId}") {
        fun createRoute(matchId: Long) = "matches/$matchId"
    }
    object Leaderboard : Screen("leaderboard")
    object Statistics : Screen("statistics")
    object TeamCombinations : Screen("statistics/teams")
    object Settings : Screen("settings")
}
```

2. **`NavGraph.kt`**
```kotlin
@Composable
fun BanditsNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(Screen.PlayerList.route) {
            PlayerListScreen(navController = navController)
        }
        composable(Screen.AddPlayer.route) {
            AddPlayerScreen(navController = navController)
        }
        composable(Screen.QuickMatchEntry.route) {
            QuickMatchEntryScreen(navController = navController)
        }
        composable(Screen.MatchList.route) {
            MatchListScreen(navController = navController)
        }
        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(navController = navController)
        }
        composable(Screen.Statistics.route) {
            StatisticsScreen(navController = navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        // Add detail screens with arguments
    }
}
```

#### 6.2 Reusable Components (`presentation/components/`)

**Files to create**:

1. **`PlayerCard.kt`** - Display player info in lists
2. **`MatchCard.kt`** - Display match summary
3. **`StatCard.kt`** - Display statistics in card format
4. **`RatingBadge.kt`** - Show rating with trend indicator
5. **`LoadingIndicator.kt`** - Loading state
6. **`ErrorMessage.kt`** - Error display
7. **`EmptyState.kt`** - Empty list placeholder

#### 6.3 Update MainActivity

**File to update**: `MainActivity.kt`

```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BanditsTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
                    }
                ) { padding ->
                    BanditsNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}
```

### Phase 7: Player Management Screens

#### 7.1 Player List Screen
**Files to create**:
- `presentation/screens/player/PlayerListScreen.kt`
- `presentation/screens/player/PlayerListViewModel.kt`
- `presentation/screens/player/PlayerListState.kt`

#### 7.2 Add Player Screen
**Files to create**:
- `presentation/screens/player/AddPlayerScreen.kt`
- `presentation/screens/player/AddPlayerViewModel.kt`

#### 7.3 Player Detail Screen
**Files to create**:
- `presentation/screens/player/PlayerDetailScreen.kt`
- `presentation/screens/player/PlayerDetailViewModel.kt`
- `presentation/screens/player/PlayerDetailState.kt`

### Phase 8: Match Management Screens

#### 8.1 Quick Match Entry Screen ⭐ CRITICAL
**Files to create**:
- `presentation/screens/match/QuickMatchEntryScreen.kt`
- `presentation/screens/match/QuickMatchEntryViewModel.kt`
- `presentation/screens/match/QuickMatchEntryState.kt`

**Key Features**:
- 4 player dropdowns with search
- Score input fields
- Date/time picker
- Duplicate detection warning
- Validation messages
- Quick submit button

#### 8.2 Match List Screen
**Files to create**:
- `presentation/screens/match/MatchListScreen.kt`
- `presentation/screens/match/MatchListViewModel.kt`

#### 8.3 Match Detail Screen
**Files to create**:
- `presentation/screens/match/MatchDetailScreen.kt`

### Phase 9: Dashboard & Statistics Screens

#### 9.1 Dashboard Screen
**Files to create**:
- `presentation/screens/dashboard/DashboardScreen.kt`
- `presentation/screens/dashboard/DashboardViewModel.kt`
- `presentation/screens/dashboard/DashboardState.kt`

**Display**:
- Recent matches (last 10)
- Top 5 players by rating
- Quick stats (total players, matches, today's matches)
- FAB for quick match entry

#### 9.2 Leaderboard Screen
**Files to create**:
- `presentation/screens/statistics/LeaderboardScreen.kt`
- `presentation/screens/statistics/LeaderboardViewModel.kt`

#### 9.3 Statistics Screen
**Files to create**:
- `presentation/screens/statistics/StatisticsScreen.kt`
- `presentation/screens/statistics/StatisticsViewModel.kt`

#### 9.4 Team Combinations Screen
**Files to create**:
- `presentation/screens/statistics/TeamCombinationsScreen.kt`

### Phase 10: Export/Import Functionality

#### 10.1 Export Data Models
**File to create**: `data/export/ExportModels.kt`

```kotlin
@Serializable
data class ExportData(
    val version: String,
    val exportDate: String,
    val players: List<PlayerDto>,
    val matches: List<MatchDto>,
    val ratingHistory: List<RatingHistoryDto>
)

@Serializable
data class PlayerDto(
    val id: Long,
    val name: String,
    val nickname: String?,
    val skillLevel: String,
    val joiningDate: String,
    val currentRating: Double,
    val isActive: Boolean
)

@Serializable
data class MatchDto(
    val id: Long,
    val matchDate: String,
    val team1Player1Id: Long,
    val team1Player2Id: Long,
    val team1Score: Int,
    val team2Player1Id: Long,
    val team2Player2Id: Long,
    val team2Score: Int,
    val winningTeam: Int,
    val notes: String?
)

@Serializable
data class RatingHistoryDto(
    val id: Long,
    val playerId: Long,
    val matchId: Long,
    val ratingBefore: Double,
    val ratingAfter: Double,
    val ratingChange: Double,
    val timestamp: String
)
```

#### 10.2 Complete ExportRepositoryImpl

**File to update**: `data/repository/ExportRepositoryImpl.kt`

Add dependencies and implement:
```kotlin
class ExportRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: BanditsDatabase
) : ExportRepository {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    override suspend fun exportToJson(): Resource<String> {
        // Query all entities
        // Map to DTOs
        // Serialize with kotlinx.serialization
    }

    override suspend fun importFromJson(json: String): Resource<Unit> {
        // Deserialize JSON
        // Validate version
        // Begin transaction
        // Delete existing data (with confirmation?)
        // Insert all data
        // Commit or rollback
    }

    override suspend fun saveJsonToFile(json: String, uri: Uri): Resource<Unit> {
        // Use ContentResolver to write to URI
    }

    override suspend fun readJsonFromFile(uri: Uri): Resource<String> {
        // Use ContentResolver to read from URI
    }
}
```

#### 10.3 Settings Screen
**Files to create**:
- `presentation/screens/settings/SettingsScreen.kt`
- `presentation/screens/settings/SettingsViewModel.kt`

**Features**:
- Export button (launches document picker)
- Import button (launches document picker, shows preview dialog)
- About section (version, attribution)

### Phase 11: Testing & Polish

#### 11.1 Unit Tests
**Files to create** (`test/` directory):
- `CalculateRatingUseCaseTest.kt` ⭐ CRITICAL
- `RecordMatchUseCaseTest.kt`
- `ValidateMatchUseCaseTest.kt`
- `GetPlayerStatisticsUseCaseTest.kt`
- `PlayerRepositoryImplTest.kt`
- `MatchRepositoryImplTest.kt`

#### 11.2 UI Polish
- Add animations (AnimatedVisibility, AnimatedContent)
- Loading states for all async operations
- Error handling with Snackbars
- Pull-to-refresh on lists
- Swipe actions (delete, edit)
- Confirmation dialogs for destructive actions
- Empty state illustrations
- Shimmer loading effects

#### 11.3 Performance
- Add pagination to match/player lists (Paging3)
- Optimize database queries (check with SQL EXPLAIN)
- Profile memory usage (LeakCanary)
- Reduce recompositions (remember, derivedStateOf)

---

## File Count Summary

### Created: 45 files
- Kotlin source files: 39
- XML resources: 6
- Gradle files: 4

### Pending: ~50 files
- Use cases: 15 files
- UI screens: 20 files
- Components: 10 files
- Tests: 8 files
- Export models: 2 files

### Total Project: ~95 files

---

## Key Architecture Decisions

### 1. Database Schema
- **Composite index** on matches for duplicate detection
- **Foreign keys** with RESTRICT on matches (prevent deleting players with matches)
- **CASCADE delete** on rating history (cleanup when player/match deleted)

### 2. Rating Algorithm
- Modified ELO system
- K-factor adjusts based on player experience
- Score difference multiplier for accurate rating changes
- Team average used for expected probability

### 3. Repository Pattern
- Interfaces in domain layer (clean architecture)
- Implementations in data layer
- Entity-Domain mappers for separation of concerns
- Flow for reactive data (auto-updates UI)

### 4. Use Cases
- Single responsibility (one action per use case)
- Testable business logic
- Injected via Hilt
- Return Result<T> for error handling

### 5. UI State Management
- StateFlow in ViewModels
- Immutable UiState data classes
- One-time events via sealed class
- Combine multiple Flows for complex states

---

## Common Issues & Solutions

### Issue 1: Build Fails - "Unresolved reference"
**Solution**: Sync Gradle (File → Sync Project with Gradle Files)

### Issue 2: App crashes on launch
**Solution**: Check Hilt is properly configured in Application class and all modules are installed

### Issue 3: Database migration needed
**Solution**: Increment version in BanditsDatabase and add migration, or use `fallbackToDestructiveMigration()`

### Issue 4: Compose preview not working
**Solution**: Invalidate caches (File → Invalidate Caches → Invalidate and Restart)

### Issue 5: Slow build times
**Solution**:
- Increase Gradle memory: `org.gradle.jvmargs=-Xmx4096m` in gradle.properties
- Enable parallel builds: `org.gradle.parallel=true`
- Enable Gradle daemon: `org.gradle.daemon=true`

---

## Testing Strategy

### Unit Tests (Use JUnit 4 + Truth)
```kotlin
@Test
fun `calculate rating - team1 wins with high score difference - increases rating significantly`() {
    // Given
    val match = Match(...)
    val recentMatchCounts = mapOf(...)

    // When
    val result = calculateRatingUseCase(match, recentMatchCounts)

    // Then
    assertThat(result[team1Player1Id]).isGreaterThan(1000.0)
}
```

### Integration Tests (Use AndroidX Test)
```kotlin
@RunWith(AndroidJUnit4::class)
class MatchRepositoryImplTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: BanditsDatabase
    private lateinit var repository: MatchRepositoryImpl

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, BanditsDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = MatchRepositoryImpl(database.matchDao(), database.playerDao())
    }
}
```

### UI Tests (Use Compose Testing)
```kotlin
@Test
fun quickMatchEntry_allFieldsFilled_submitEnabled() {
    composeTestRule.setContent {
        QuickMatchEntryScreen()
    }

    composeTestRule.onNodeWithText("Team 1 Player 1").performClick()
    // ... fill all fields

    composeTestRule.onNodeWithText("Submit").assertIsEnabled()
}
```

---

## Resources & References

### Official Documentation
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Hilt Dependency Injection](https://developer.android.com/training/dependency-injection/hilt-android)
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- [Kotlinx Serialization](https://kotlinlang.org/docs/serialization.html)

### Rating Systems
- [ELO Rating System](https://en.wikipedia.org/wiki/Elo_rating_system)
- [K-factor in ELO](https://www.omnicalculator.com/sports/elo#elo-rating-formula-and-k-factor)

### Sample Apps
- [Now in Android](https://github.com/android/nowinandroid) - Architecture reference
- [Tivi](https://github.com/chrisbanes/tivi) - Compose + Room example

---

## Next Session Checklist

When you return to this project:

1. ✅ Open in Android Studio
2. ✅ Sync Gradle (may take 10 minutes)
3. ✅ Review this document
4. ✅ Start with Phase 4 (Rating System) - most critical
5. ✅ Test rating algorithm with unit tests
6. ✅ Implement RecordMatchUseCase
7. ✅ Create basic UI for player and match entry
8. ✅ Test end-to-end: Add players → Record match → Check ratings updated
9. ✅ Continue with remaining phases

---

## Contact & Support

For questions about Android development:
- [Stack Overflow - Android](https://stackoverflow.com/questions/tagged/android)
- [Android Developers Community](https://developer.android.com/community)
- [Kotlin Slack](https://surveys.jetbrains.com/s3/kotlin-slack-sign-up)

---

**Last Updated**: December 16, 2025
**Estimated Remaining Work**: 40-50 hours
**Priority Order**: Phase 4 → Phase 5 → Phase 8 → Phase 9 → Phase 6-7 → Phase 10-11
