# MIT License - Copyright (c) 2024 Rubontech
# Race of War ProGuard rules

# Keep game classes that might be accessed via reflection
-keep class com.rubontech.raceofwar.** { *; }

# Keep enum classes (used for GameState, UnitType, etc.)
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Compose related classes
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }

# Keep kotlinx.coroutines for StateFlow
-keep class kotlinx.coroutines.** { *; }

# Preserve line numbers for debugging
-keepattributes SourceFile,LineNumberTable

# Game performance optimizations
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}