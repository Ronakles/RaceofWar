+# Race of War

**Rubontech's Race of War** - A modern Android 2D strategy game built with Kotlin and Jetpack Compose.

## Overview

Race of War is an "Age of War" style real-time strategy game where players spawn units to attack the enemy base while defending their own. Built with modern Android development practices, the game features a custom 2D engine running on SurfaceView with Jetpack Compose UI overlay.

## Features

- **Real-time Strategy Gameplay**: Spawn melee and ranged units to battle enemies
- **Modern Android Architecture**: Built with Kotlin, Jetpack Compose, and MVVM patterns
- **Custom 2D Engine**: Fixed timestep game loop with 60 FPS target performance
- **Responsive UI**: Jetpack Compose HUD with real-time game state updates
- **Performance Optimized**: Object pooling, minimal GC pressure, hardware acceleration
- **Debug Tools**: FPS counter, debug overlay for development

## Project Structure

```
app/
├── src/main/
│   ├── java/com/rubontech/raceofwar/
│   │   ├── App.kt                          # Application class
│   │   ├── MainActivity.kt                 # Main activity with fullscreen setup
│   │   ├── core/                          # Core engine components
│   │   │   ├── time/FixedTimestepClock.kt # Fixed timestep implementation
│   │   │   ├── loop/GameLoop.kt           # Main game loop thread
│   │   │   └── input/InputController.kt   # Touch input handling
│   │   ├── engine/                        # Game engine layer
│   │   │   ├── GameSurface.kt            # SurfaceView game canvas
│   │   │   ├── GameRenderer.kt           # Main renderer
│   │   │   ├── Scene.kt                  # Base scene class
│   │   │   └── SceneManager.kt           # Scene management
│   │   ├── gfx/                          # Graphics and animation
│   │   │   ├── Sprite.kt                 # 2D sprite rendering
│   │   │   ├── SpriteSheet.kt            # Sprite atlas management
│   │   │   ├── Animator.kt               # Frame-based animation
│   │   │   └── ParallaxLayer.kt          # Background parallax effects
│   │   ├── game/                         # Game logic
│   │   │   ├── GameConfig.kt             # Balance values and constants
│   │   │   ├── World.kt                  # Main game world container
│   │   │   ├── BattleScene.kt            # Main gameplay scene
│   │   │   ├── entities/                 # Game entities
│   │   │   │   ├── Entity.kt             # Base entity class
│   │   │   │   ├── UnitEntity.kt         # Combat unit base class
│   │   │   │   ├── MeleeUnit.kt          # Melee combat unit
│   │   │   │   ├── RangedUnit.kt         # Ranged combat unit
│   │   │   │   ├── Bullet.kt             # Projectile entity
│   │   │   │   └── BaseEntity.kt         # Player/enemy base
│   │   │   └── systems/                  # ECS-style systems
│   │   │       ├── MovementSystem.kt     # Entity movement logic
│   │   │       ├── CombatSystem.kt       # Combat resolution
│   │   │       ├── SpawnSystem.kt        # Unit spawning (player/AI)
│   │   │       └── CollisionSystem.kt    # Collision detection
│   │   └── ui/                           # Jetpack Compose UI
│   │       ├── hud/HudScreen.kt          # Main HUD overlay
│   │       ├── components/               # Reusable UI components
│   │       │   ├── FpsCounter.kt         # FPS display
│   │       │   ├── GoldBar.kt            # Resource counter
│   │       │   └── SpawnButtons.kt       # Unit spawn controls
│   │       └── theme/                    # Material Design theme
│   ├── res/                              # Android resources
│   │   ├── drawable/                     # Vector icons and graphics
│   │   ├── mipmap-*/                     # App icons
│   │   └── values/                       # Strings, colors, themes
│   └── AndroidManifest.xml               # App configuration
├── build.gradle.kts                      # Module build configuration
└── proguard-rules.pro                    # Code obfuscation rules
```

## Technical Architecture

### Core Engine
- **Fixed Timestep Loop**: Ensures consistent game simulation at 60 FPS
- **Separate Game Thread**: Rendering and logic run on dedicated thread
- **Object Reuse**: Minimal garbage collection pressure through object pooling
- **Input Handling**: Thread-safe touch event processing

### Graphics System
- **2D Canvas Rendering**: Direct Canvas2D operations for performance
- **Sprite System**: Extensible sprite rendering with placeholder rectangles
- **Animation Framework**: Frame-based animation system ready for sprite sheets
- **Parallax Backgrounds**: Multi-layer scrolling backgrounds

### Game Logic
- **Entity-Component-System**: Modular entity system for units, bullets, bases
- **Physics Systems**: Movement, collision detection, combat resolution
- **AI Behavior**: Simple enemy spawning and targeting logic
- **State Management**: Reactive game state with Kotlin StateFlow

### UI Architecture
- **Jetpack Compose HUD**: Modern declarative UI overlay
- **Reactive Updates**: Real-time UI updates via StateFlow
- **Touch Integration**: Seamless touch handling between Canvas and Compose
- **Responsive Design**: Adapts to different screen sizes

## Game Mechanics

### Units
- **Melee Units**: 🗡️ High health, close combat (50 gold)
- **Ranged Units**: 🏹 Projectile attacks, longer range (75 gold)

### Economy
- **Gold Generation**: +10 gold per second
- **Unit Costs**: Balanced resource management

### Victory Conditions
- **Destroy Enemy Base**: Reduce enemy base HP to 0
- **Defend Your Base**: Prevent your base from being destroyed

### Controls
- **Spawn Units**: Tap melee/ranged buttons (bottom of screen)
- **Pause/Resume**: Tap pause button (top center)
- **Restart**: Tap "Play Again" after game over

## Setup and Installation

### Prerequisites
- Android Studio Arctic Fox (2020.3.1) or later
- Android SDK API 24+ (minimum)
- Android SDK API 34 (target)
- Kotlin 2.0.21+

### Building the Project

1. **Clone Repository**
   ```bash
   git clone <repository-url>
   cd RaceOfWar
   ```

2. **Open in Android Studio**
   - File → Open → Select project folder
   - Wait for Gradle sync to complete

3. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   # OR click Run in Android Studio
   ```

### Gradle Configuration

The project uses Gradle version catalog for dependency management:

```kotlin
// gradle/libs.versions.toml
[versions]
agp = "8.7.3"
kotlin = "2.0.21"
compose = "2024.09.00"

[libraries]
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose" }
// ... other dependencies
```

### Build Types

- **Debug**: Development build with debug overlay enabled
- **Release**: Optimized build with ProGuard obfuscation

## Performance Optimizations

### Engine Performance
- **Fixed Delta Time**: Consistent 16.67ms update intervals
- **Object Pooling**: Reused Paint, Rect, Path objects
- **Minimal Allocations**: No object creation in render loop
- **Thread Separation**: Game logic and rendering on separate threads

### Memory Management
- **Texture Atlasing**: Ready for sprite sheet implementation
- **Entity Cleanup**: Automatic dead entity removal
- **Bounded Collections**: Prevented memory leaks in entity lists

### Rendering Optimizations
- **Hardware Acceleration**: Enabled in manifest
- **Canvas Reuse**: Efficient Canvas2D operations
- **Culling Ready**: Framework for off-screen culling

## Development and Debugging

### Debug Features
- **FPS Counter**: Real-time performance monitoring
- **Debug Overlay**: Hitbox visualization (BuildConfig.DEBUG)
- **State Inspection**: Game state logging and monitoring

### Configuration
All game balance values are centralized in `GameConfig.kt`:

```kotlin
object GameConfig {
    const val UNIT_SPEED = 60f
    const val MELEE_HP = 100f
    const val RANGED_RANGE = 150f
    const val GOLD_PER_SEC = 10
    // ... more configurable values
}
```

### Extending the Game

The architecture supports easy extension:

1. **New Unit Types**: Extend `UnitEntity` with custom behavior
2. **New Weapons**: Add bullet types in `Bullet.kt`
3. **Power-ups**: Implement as entities with collision systems
4. **Multiple Lanes**: Extend `GameConfig` with lane definitions
5. **Sound Effects**: Add `SoundPool` integration to game events

## Expansion Roadmap

### Phase 1: Enhanced Gameplay
- [ ] Multiple unit types (tanks, healers, siege)
- [ ] Special abilities and cooldowns
- [ ] Multiple battle lanes
- [ ] Unit upgrades and technology tree

### Phase 2: Graphics Enhancement
- [ ] OpenGL ES 2.0 renderer migration
- [ ] Sprite atlas and animations
- [ ] Particle effects for combat
- [ ] Dynamic lighting and shadows

### Phase 3: Audio and Polish
- [ ] Sound effects with `SoundPool`
- [ ] Background music system
- [ ] Haptic feedback for combat
- [ ] Screen transitions and UI polish

### Phase 4: Content and Progression
- [ ] Campaign mode with levels
- [ ] Save/load with `DataStore`
- [ ] Achievement system
- [ ] Multiplayer foundation

### Phase 5: Advanced Features
- [ ] Procedural map generation
- [ ] AI difficulty levels
- [ ] Custom unit editor
- [ ] Mod support framework

## Technical Specifications

### Dependencies (Zero Cost)
- **Kotlin Standard Library**: Core language features
- **AndroidX Core**: Modern Android compatibility
- **AndroidX Activity**: Activity lifecycle management
- **Jetpack Compose**: Declarative UI framework
- **Material3**: UI component library

### Minimum Requirements
- **Android API 24** (Android 7.0, 2016)
- **64MB RAM** for game assets
- **ARM/x86 CPU** with hardware acceleration
- **Landscape orientation** (auto-handled)

### Performance Targets
- **60 FPS** sustained gameplay
- **< 100ms** input latency
- **< 50MB** peak memory usage
- **< 1% frame drops** during normal gameplay

## License

```
MIT License

Copyright (c) 2024 Rubontech

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Support

For technical support or game suggestions:
- Create an issue in the repository
- Contact: [Your Contact Information]
- Documentation: This README and inline code comments

---

**Rubontech Race of War** - Built with ❤️ for Android gaming
