# Project Plan

Build a streaming platform called GetSungTV for Android TV devices using Jetpack Compose for TV, Clean Architecture, and Media3.

## Project Brief

# GetSungTV - Android TV Streaming Platform

GetSungTV is a high-performance, modern streaming application designed specifically for Android TV. Built with Jetpack Compose for TV and following Clean Architecture principles, it provides a cinematic experience with a "10-foot UI" optimized for D-pad navigation.

## Features
- **Home Screen**: Features a high-impact `FeaturedCarousel` for spotlight content and categorized rows using `ImmersiveList` for a dynamic browsing experience.
- **Content Discovery**: Intuitive side navigation drawer providing quick access to Home, Search, Movies, TV Shows, and Settings.
- **Detail View**: Immersive content pages featuring high-resolution backdrop art, detailed metadata, and integrated "Play" and "Watchlist" actions.
- **Media Playback**: Seamless video streaming powered by Jetpack Media3 ExoPlayer, featuring custom TV-optimized playback controls.
- **Search**: Dedicated search functionality with a TV-friendly input interface.
- **Performance Optimized**: Built with state-of-the-art Jetpack libraries to ensure smooth transitions and responsive UI on TV hardware.

## Tech Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose for TV (`androidx.tv:tv-material`)
- **Architecture**: Clean Architecture + MVVM (Model-View-ViewModel)
- **Media Playback**: Jetpack Media3 (ExoPlayer & MediaSession)
- **Dependency Injection**: Hilt
- **Networking**: Retrofit & OkHttp
- **Image Loading**: Coil (TV-optimized)
- **Local Storage**: Room (for favorites/watchlist)
- **Navigation**: Navigation Compose for TV

## UI Design
- **Theme**: Vibrant and energetic Material 3 theme using a "Deep Purple" and "Electric Blue" palette.
- **Navigation**: Fully optimized for D-pad (Remote Control) navigation with clear, animated focus states.
- **Display**: Full Edge-to-Edge support for a truly immersive cinematic feel.
- **Assets**: Adaptive app icon and Leanback-compliant home screen banner.

## Implementation Steps

### 1: Configure project dependencies and manifest for Android TV
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - Required TV libraries (Compose TV, Media3, etc.) are added
  - Manifest includes CATEGORY_LEANBACK_LAUNCHER and TV feature declarations
  - Project syncs successfully
- **StartTime:** 2026-07-11 00:50:56 PKT

### 2: Implement Theme and Base UI Components
- **Status:** PENDING
- **Acceptance Criteria:**
  - Material 3 theme with Deep Purple/Electric Blue palette is implemented
  - Base TV components (Cards, Focus states) are defined
  - Edge-to-edge support is enabled

### 3: Setup Clean Architecture Layers and DI
- **Status:** PENDING
- **Acceptance Criteria:**
  - Domain, Data, and Presentation layers are structured
  - Hilt is configured for dependency injection
  - Basic Room database and Retrofit setup are ready

### 4: Implement Home Screen with FeaturedCarousel and ImmersiveList
- **Status:** PENDING
- **Acceptance Criteria:**
  - Home screen displays featured content in a carousel
  - Content rows are displayed using ImmersiveList
  - D-pad navigation works across home screen elements

### 5: Implement Content Discovery (Navigation Drawer and Search)
- **Status:** PENDING
- **Acceptance Criteria:**
  - Side navigation drawer allows switching between sections
  - Search screen with TV-optimized input is functional
  - Navigation between screens is smooth and handled by Navigation Compose

### 6: Implement Detail View and Playback Logic
- **Status:** PENDING
- **Acceptance Criteria:**
  - Detail screen shows metadata and backdrop
  - Media3 ExoPlayer is integrated for video playback
  - Custom TV playback controls are functional

### 7: Final Verification and Polish
- **Status:** PENDING
- **Acceptance Criteria:**
  - App icon and TV banner are implemented
  - Critic agent verifies stability and UI consistency
  - No critical crashes or UI issues found

