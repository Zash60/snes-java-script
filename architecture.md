# SNES Emulator Android App Architecture Plan

## Overview
This document outlines the high-level architecture for a functional SNES emulator Android app built with Kotlin, targeting Android 14. The app integrates an open-source emulator core (Snes9x) to run Super Nintendo Entertainment System games, providing a user-friendly interface for game selection, virtual controls, and seamless gameplay experience.

## Key Components

### 1. Emulator Core Integration
- **Core Library**: Utilize Snes9x, a C++-based open-source SNES emulator core.
- **JNI Bridge**: Implement Java Native Interface (JNI) to interface Kotlin code with the native C++ core.
- **Core Wrapper**: Create a Kotlin wrapper class to manage core lifecycle, ROM loading, emulation state, and frame rendering.
- **Modularity**: Isolate core logic in a separate module for easy updates and maintenance.

### 2. User Interface (UI)
- **Game Selection Screen**: RecyclerView displaying a list of available ROM files from device storage.
- **Gameplay Screen**: Full-screen SurfaceView for video rendering with overlay virtual buttons for controls.
- **Settings Screen**: Options for audio/video settings, input mapping, and emulator configurations.
- **Navigation**: Use Jetpack Navigation for seamless screen transitions.

### 3. Input Handling
- **Virtual Controls**: Custom ViewGroup with touch-sensitive buttons mimicking SNES controller layout (A, B, X, Y, Start, Select, D-pad).
- **Touch Mapping**: Map touch events to emulator input states, supporting multi-touch for simultaneous button presses.
- **Hardware Input**: Optional support for external controllers via Android's InputManager.

### 4. Audio/Video Rendering
- **Video Rendering**: Use OpenGL ES or Android's SurfaceView for efficient frame rendering at 60 FPS.
- **Audio Rendering**: Integrate with Android's AudioTrack for low-latency audio output.
- **Performance Optimization**: Implement frame skipping and audio buffering to maintain smooth playback on various devices.

### 5. ROM File Loading
- **File Picker**: Use Android's Storage Access Framework (SAF) for secure ROM file selection.
- **ROM Validation**: Check file format and integrity before loading into the emulator core.
- **Caching**: Optional caching of recently played games for faster loading.

### 6. Permissions and Security
- **Storage Permissions**: Request READ_EXTERNAL_STORAGE and MANAGE_EXTERNAL_STORAGE (for Android 11+) to access ROM files.
- **Runtime Permissions**: Handle permission requests gracefully with user education.
- **Data Security**: Ensure ROM files are not stored persistently; process in-memory only.

## Architecture Structure

```
app/
├── core/                    # Emulator core module
│   ├── jni/                # JNI bindings
│   ├── wrapper/            # Kotlin core wrapper
│   └── models/             # Core-related data models
├── ui/                     # UI module
│   ├── screens/            # Activity/Fragment classes
│   ├── views/              # Custom views (virtual controls)
│   └── adapters/           # RecyclerView adapters
├── data/                   # Data management
│   ├── repository/         # ROM and settings repositories
│   └── storage/            # File handling utilities
├── utils/                  # Utility classes
│   ├── permissions/        # Permission helpers
│   └── audio/              # Audio processing
└── di/                     # Dependency injection (Hilt)
```

## Dependencies
- **Android SDK**: Minimum API 24 (Android 7.0), Target API 34 (Android 14)
- **Kotlin**: 1.9+
- **Android Jetpack**: ViewModel, LiveData, Navigation, Room (for settings persistence)
- **Snes9x Core**: Integrated via JNI (pre-compiled shared library)
- **Graphics**: OpenGL ES 2.0+ for rendering
- **Build Tools**: Gradle with Kotlin DSL

## Workflow
1. **App Launch**: Check permissions, initialize core, load settings.
2. **ROM Selection**: User browses and selects ROM file via SAF.
3. **Emulation Start**: Load ROM into core, initialize audio/video, display gameplay screen.
4. **Gameplay Loop**: Continuously render frames, process input, output audio.
5. **Settings Management**: Allow runtime adjustments to emulator parameters.
6. **App Exit**: Save state if supported, clean up resources.

## Performance Considerations
- **Threading**: Use background threads for emulation loop to avoid blocking UI.
- **Memory Management**: Monitor heap usage, implement efficient bitmap recycling.
- **Battery Optimization**: Leverage Android's Doze mode compatibility.
- **Device Compatibility**: Test on various screen sizes and performance tiers.

## Compatibility
- **Android Versions**: Support from API 24 to 34, with graceful degradation.
- **Device Types**: Phones and tablets, landscape orientation preferred.
- **ROM Formats**: Standard .smc, .sfc, and .zip archives.
- **Legal Compliance**: App will include disclaimers about ROM ownership and copyright.

This architecture ensures modularity for maintainability, performance optimization for smooth gameplay, and compatibility across Android devices while integrating the powerful Snes9x emulator core.