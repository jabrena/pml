# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
