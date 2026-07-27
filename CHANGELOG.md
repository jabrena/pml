# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.9.0] - 2026-07-27

### Removed

- Deprecated singular `author` element from the `metadata` content model in the PML schema (#137); use `authors/author` instead. The global `author` element declaration is retained because `authors` still references it.
- `triggers` reference from the `prompt` content model in the PML schema (#137), along with the now-unreferenced `triggers`, `trigger-list`, and `trigger` element declarations; `triggers` conceptually belongs to a skills-only shape rather than this repository's shared system-prompt schema.

## [0.8.0] - 2026-07-08

### Added

- Support for multiple metadata authors in the PML schema with `authors/author`, while preserving the legacy `author` field for the 0.8.0 transition.
- Schema validation coverage for metadata that includes both `author` and multiple `authors/author` entries.

## [0.7.0] - 2025-03-18

### Added

- `id` attribute on prompt element in PML schema for uniquely identifying prompts

## [0.6.0] - 2025-03-18

### Added

- Support for skills syntax in PML schema (#109):
  - `triggers` element with `trigger-list` and `trigger` for defining when skills apply
  - `references` element with `reference-list` and `reference` for external references
  - `interactive` attribute on prompt element (`true`/`false`) for interactive mode

## [0.5.0] - 2026-02-24

### Added

- `license` field in metadata element for PML schema

## [0.4.0] - 2026-02-23

### Added

- PML CLI tool (`pml`) for command-line operations with PML files (validate and convert commands)
- `description` field in metadata element for PML schema (#89) to support Skills

### Removed

- `instructions` element from PML schema (#89) — use `metadata/description` instead

## [0.3.0] - 2025-11-10

### Changed

- Enhanced PML schema with mixed content support for better flexibility
- Made `output-format-list` and `safeguards-list` elements optional in PML schema
- Improved documentation and README with clearer examples and explanations

### Removed (Many thanks for the feedback @maxandersen)

- Removed optional `type` attribute from `PromptType` element in PML Workflow schema
- Removed `fallback-type` attribute from `SequenceType` in PML Workflow schema
- Removed `type` attribute from `ParallelType` in PML Workflow schema
- Removed `PromptTypeEnum` type definition from PML Workflow schema

## [0.2.0] - 2025-11-03

### Added
- New PML Workflow schema (`pml-workflow.xsd`) supporting workflow orchestration patterns
- Sequence pattern for executing prompts in sequential order
- Parallel pattern for executing multiple prompt sequences concurrently
- Timeout support for workflow sequences with configurable timeout duration
- Fallback prompt support with `fallback-src` and `fallback-type` attributes for error handling
- `steps` element in PML schema for defining step-by-step instructions
- `acceptance-criteria` element enhancements in PML schema
- Comprehensive test resources including workflow examples (hello-world, pi calculation, CIS194 course)
- Test validation for both PML and PML Workflow schemas

### Changed
- Improved documentation and README with workflow examples
- Enhanced schema validation tests

### Deprecated
- `instructions` element in PML schema (marked for deprecation in future releases)

[0.8.0]: https://github.com/jabrena/pml/compare/0.7.0...0.8.0
[0.7.0]: https://github.com/jabrena/pml/compare/0.6.0...0.7.0
[0.6.0]: https://github.com/jabrena/pml/compare/0.5.0...0.6.0
[0.5.0]: https://github.com/jabrena/pml/compare/0.4.0...0.5.0
[0.4.0]: https://github.com/jabrena/pml/compare/0.3.0...0.4.0
[0.3.0]: https://github.com/jabrena/pml/compare/0.2.0...0.3.0
[0.2.0]: https://github.com/jabrena/pml/compare/0.1.0...0.2.0
