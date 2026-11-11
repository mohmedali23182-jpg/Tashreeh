# Tashreeh — execution roadmap

## Phase 0 (this commit)
- Native Android Kotlin/Compose app; Android 8+ (minSdk 26), arm64 target.
- Room SQLite educational organ seed, local search, persisted favorites, one quiz.
- Original reference HTML kept under reference/, not executed or bundled yet.
- Neon migration draft, not applied; no credentials or cloud integration claimed.
- Build: JDK 17, Android SDK 35, Gradle 8.9. Run `gradle :app:assembleDebug`.
- Build output: app/build/outputs/apk/debug/app-debug.apk. No APK signed or tested yet.

## Stage 1 — native offline anatomy
Audit original HTML and extract licensed BodyParts3D meshes with attribution; preserve reference untouched. Replace CDN dependencies with licensed local copies of required Three.js/MediaPipe WASM/models or implement native renderer after measuring. Implement a working offline 3D viewer, layer toggles, selection, labels, pan/zoom/rotate, Arabic/English accessibility. Add real device and airplane-mode tests. Do not claim full-body tracking: reference tracks face/hands.

## Stage 2 — mirror and educational experience
CameraX or securely scoped WebView bridge; explicit camera permission; face/hand tracking, calibrated transforms, local inference, no camera uploads. Extend body tracking only with appropriate models and validated anatomy alignment. Build lessons, quizzes, bookmarks, progress, searchable atlas, content provenance and medical review. Profile on low-end ARM64 Android 8 devices.

## Stage 3 — Neon/Vercel hybrid
Provision dedicated Neon development DB, apply reviewed migrations, add authenticated HTTPS API and admin CMS; server-only DB credentials. Object storage for licensed 3D assets with hashes and version manifests. Implement WorkManager sync with Room source of truth, per-user identity, retry/backoff, idempotency and conflict rules. Downloads optional; airplane-mode baseline preserved.

## Stage 4 — polish and release
Run unit/instrumentation, offline, camera denial, API security, content and accessibility checks. Pin build tools and add CI, establish signing and release keys in GitHub secrets (never commit). Produce signed 64-bit APK, Android 8+, release notes, checksums, privacy policy, attribution and verified upgrade path. No fake features or releases.
