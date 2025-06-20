<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Properties Placeholder Resolver Changelog

## [Unreleased]

## [1.2.0] - 2025-06-20

### Added

- **Quick fix action:** From now on if a property reference key was not found, it is possible to create the property 
  with a provided quick fix in a selected file

## [1.1.1] - 2025-06-20

### Fixed

- **Unresolved link display message:** Now the error message shows only useful information
- **Description updated**

## [1.1.0] - 2025-06-06

### Added

- **Multiple references:** A single property value may now contain any number of placeholders, including repeated ones.

## [1.0.1] - 2025-04-06

### Added

- **Navigation:** Jump directly from property placeholders (e.g. `propKey=${refToProp}` or 
`propKey=${refToProp:defaultVal}`) to their declaration or usage.
- **Highlighting:** Correctly highlights references: normal for valid ones, warning for unresolved.

[Unreleased]: https://github.com/DaNizz97/extended-prop-searcher/compare/v1.2.0...HEAD
[1.2.0]: https://github.com/DaNizz97/extended-prop-searcher/compare/v1.1.1...v1.2.0
[1.1.1]: https://github.com/DaNizz97/extended-prop-searcher/compare/v1.1.0...v1.1.1
[1.1.0]: https://github.com/DaNizz97/extended-prop-searcher/compare/v1.0.1...v1.1.0
[1.0.1]: https://github.com/DaNizz97/extended-prop-searcher/commits/v1.0.1
