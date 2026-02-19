# Contributing to CrucialLib

## Development Environment Setup

### Prerequisites
- **Java 21** (JDK, not just JRE)
- **Maven 3.8+**
- An IDE with Java support (IntelliJ IDEA recommended)

### Getting Started

```bash
git clone https://github.com/ChafficPlugins/CrucialLib.git
cd CrucialLib
mvn clean verify
```

This will compile the project, run all tests, and package the JAR.

### IDE Setup
- Import as a Maven project
- Set the project SDK to Java 21
- Maven should auto-import dependencies

## Branch Naming

- `feature/<description>` — New features
- `fix/<description>` — Bug fixes
- `chore/<description>` — Maintenance, dependencies, CI, docs

## Making Changes

1. Create a branch from `master` following the naming convention above
2. Make your changes
3. Write or update tests for any new/changed public methods
4. Run `mvn clean verify` and ensure all tests pass
5. Open a pull request against `master`

## Pull Request Requirements

- All tests must pass (`mvn clean verify` exits 0)
- No breaking changes to public API without a MAJOR version bump
- New public methods must have corresponding unit tests
- PR description should explain what changed and why

## Code Style

- Follow existing code conventions in the project
- Use meaningful variable and method names
- Use `Server.log()` and `Server.error()` for console output, not `System.out`
- Keep classes focused — one responsibility per class

## Testing

- Tests live in `src/test/java/` mirroring the main package structure
- Use JUnit 5 and MockBukkit for Bukkit API testing
- Every new public method needs at least one test
- Test normal input, edge cases, and null handling where applicable

```bash
# Run tests
mvn test

# Run a specific test class
mvn test -Dtest=CrucialItemTest
```

## Versioning (SemVer)

This project follows [Semantic Versioning](https://semver.org/):

- **PATCH** (2.2.x → 2.2.y): Bug fixes, no API changes
- **MINOR** (2.x.0 → 2.y.0): New features, backwards compatible
- **MAJOR** (x.0.0 → y.0.0): Breaking API changes

## Important Notes

- CrucialLib is a **library** used by multiple plugins — API stability is critical
- The plugin registers as "CrucialLib" in plugin.yml
- NMS-dependent features (`Border`, `Package`) are version-specific and may need updates when Spigot versions change
- Distribution is via JitPack — publishing happens automatically when a GitHub release/tag is created
