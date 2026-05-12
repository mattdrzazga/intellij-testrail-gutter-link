# TestRail Gutter Link

An IntelliJ Platform plugin that adds a gutter icon next to every
`@TestCase(testCaseId = "...")` annotation in Java and Kotlin test sources.
Clicking the icon opens the matching TestRail case in your default browser.

## How it works

The plugin registers a `LineMarkerProvider` for Java and Kotlin. For every
annotation whose simple name is `TestCase` and that is applied to a method
(regardless of package), it reads the `testCaseId` string attribute and builds
a URL of the form:

```
<base-url>/index.php?/cases/view/<testCaseId>
```

Example - with the default base URL `https://example.testrail.io`:

```kotlin
@TestCase(testCaseId = "12345")
fun `login succeeds with valid credentials`() { ... }
```

The gutter icon on the `@TestCase` line opens
`https://example.testrail.io/index.php?/cases/view/12345`.

Matching is based purely on the simple annotation name, so any `@TestCase`
declaration with a `testCaseId: String` attribute works - you do not need a
specific import or framework.

## Configuration

Settings are **per project** and live under:

**Settings → Tools → TestRail Gutter Link**

There is a single field, **Base URL**. Set it to the root of your TestRail
instance, e.g.:

```
https://your-company.testrail.io
```

Trailing slashes are trimmed automatically. The value is stored in
`.idea/testRailGutterLink.xml`. Changes take effect on the next gutter click -
no IDE restart required.

## Installation

1. Build or download the distribution zip
   (`build/distributions/testrail-gutter-link-<version>.zip`).
2. In the IDE, open **Settings → Plugins → ⚙ → Install Plugin from Disk…**.
3. Select the zip and restart when prompted.

## Building from source

Requirements: JDK 17.

```
./gradlew buildPlugin
```

The installable plugin zip is written to:

```
build/distributions/testrail-gutter-link-<version>.zip
```

where `<version>` is the `pluginVersion` from `gradle.properties` (currently
`1.0.0`, so the file is `build/distributions/testrail-gutter-link-1.0.0.zip`).
Feed that path to **Settings → Plugins → ⚙ → Install Plugin from Disk…** in
any compatible IDE.

To run a sandbox IDE with the plugin pre-installed (useful for iterating
during development):

```
./gradlew runIde
```

## Compatibility

- Target platform: IntelliJ IDEA Community (`IC`) 2024.1 and later
  (`sinceBuild = 241`).
- Works in any IDE on that platform: IntelliJ IDEA, Android Studio Koala+,
  etc.
- Requires the bundled Java and Kotlin plugins.
- Supports both K1 and K2 Kotlin compiler modes.

## Project layout

```
src/main/kotlin/com/mateo/plugins/testraillink/
  TestCaseLineMarkerProvider.kt   - gutter icon + click handler
  TestRailSettings.kt             - per-project PersistentStateComponent
  TestRailSettingsConfigurable.kt - Settings UI page
src/main/resources/
  META-INF/plugin.xml             - plugin manifest, extension points
  icons/testrail.svg              - gutter icon
```

## License

See [LICENSE](LICENSE).
