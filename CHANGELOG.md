# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
