<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Properties Placeholder Resolver Changelog

## [Unreleased]

## [1.4.0] - 2025-07-01

### Added

- **Placeholder-aware copy:** When the caret sits inside a `${…}` placeholder in a `.properties` value
  (with no selection), pressing **Ctrl/Cmd + C** now copies only the key inside the braces.
- **Settings toggle:** A new checkbox **“Enable placeholder-aware copy”** in
  *Settings ▸ Tools ▸ Properties Placeholder* lets you turn this behavior on or off. Disabling it restores the IDE’s
  default copy action.

## [1.3.0] - 2025-06-28

### Fixed

- **Quick fix action:** Quick-fix no longer fails when adding a property to an empty or comments-only **.properties** 
  file.

### Changed

- Placeholders are now accepted only if they match valid key syntax (`[A-Za-z0-9_.]+`); all other patterns are ignored.

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

[Unreleased]: https://github.com/DaNizz97/extended-prop-searcher/compare/v1.4.0...HEAD
[1.4.0]: https://github.com/DaNizz97/extended-prop-searcher/compare/v1.3.0...v1.4.0
[1.3.0]: https://github.com/DaNizz97/extended-prop-searcher/compare/v1.2.0...v1.3.0
[1.2.0]: https://github.com/DaNizz97/extended-prop-searcher/compare/v1.1.1...v1.2.0
[1.1.1]: https://github.com/DaNizz97/extended-prop-searcher/compare/v1.1.0...v1.1.1
[1.1.0]: https://github.com/DaNizz97/extended-prop-searcher/compare/v1.0.1...v1.1.0
[1.0.1]: https://github.com/DaNizz97/extended-prop-searcher/commits/v1.0.1
