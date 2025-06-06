<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Properties Placeholder Resolver Changelog

## [Unreleased]

## [1.1.0] - 2025-06-06

### Added

- **Multiple references:** A single property value may now contain any number of placeholders, including repeated ones.

## [1.0.1] - 2025-04-06

### Added

- **Navigation:** Jump directly from property placeholders (e.g. `propKey=${refToProp}` or 
`propKey=${refToProp:defaultVal}`) to their declaration or usage.
- **Highlighting:** Correctly highlights references: normal for valid ones, warning for unresolved.

[Unreleased]: https://github.com/DaNizz97/extended-prop-searcher/compare/v1.1.0...HEAD
[1.1.0]: https://github.com/DaNizz97/extended-prop-searcher/compare/v1.0.1...v1.1.0
[1.0.1]: https://github.com/DaNizz97/extended-prop-searcher/commits/v1.0.1
