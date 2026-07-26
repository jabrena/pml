# pml-schema Specification

## Purpose
TBD - created by archiving change migrate-schema-0-9-0. Update Purpose after archive.
## Requirements
### Requirement: Metadata authorship uses only the authors element
`pml.xsd`'s `<metadata>` content model SHALL NOT accept a bare, singular `<author>` element as a direct child of `<metadata>`. Multiple authorship SHALL continue to be expressed only through `<authors>`, which contains one or more `<author>` entries.

#### Scenario: Metadata declaring authors validates
- **WHEN** a PML document's `<metadata>` declares `<authors><author>Name</author></authors>`
- **THEN** the document validates successfully against `pml.xsd`

#### Scenario: Metadata declaring the deprecated singular author fails validation
- **WHEN** a PML document's `<metadata>` declares a bare `<author>` element directly (not nested inside `<authors>`)
- **THEN** the document fails validation against `pml.xsd`, because `<metadata>`'s content model no longer permits `<author>` as a direct child

### Requirement: Prompt content model excludes triggers
`pml.xsd`'s `<prompt>` content model SHALL NOT reference `<triggers>`. `<triggers>` is a skills-specific concept and does not belong to the shared `<prompt>` shape.

#### Scenario: Prompt without triggers validates
- **WHEN** a PML document's `<prompt>` root element omits `<triggers>` entirely
- **THEN** the document validates successfully against `pml.xsd`

#### Scenario: Prompt containing triggers fails validation
- **WHEN** a PML document's `<prompt>` root element includes a `<triggers>` child
- **THEN** the document fails validation against `pml.xsd`, because `<prompt>`'s content model no longer permits `<triggers>`

### Requirement: Schema version 0.9.0 is published
The PML schema SHALL be published as version 0.9.0 alongside all previously published versions, following the repository's existing per-version publishing convention.

#### Scenario: 0.9.0 schema files are published under the versioned docs path
- **WHEN** the 0.9.0 release is published
- **THEN** `docs/schemas/0.9.0/pml.xsd` and `docs/schemas/0.9.0/pml-workflow.xsd` exist and are reachable at `https://jabrena.github.io/pml/schemas/0.9.0/pml.xsd` and `https://jabrena.github.io/pml/schemas/0.9.0/pml-workflow.xsd`
- **AND** `docs/schemas/index.html` lists version 0.9.0 for both schema files
- **AND** the 0.8.0 published schema files remain unchanged and reachable at their existing 0.8.0 paths

### Requirement: Local fixtures and build metadata reflect 0.9.0
Every local PML test fixture in this repository SHALL validate against the updated schema using only `<authors>`, and the project's own build metadata SHALL reflect the 0.9.0 version.

#### Scenario: Local schema validation test suite passes using only authors
- **WHEN** `SchemaPMLValidationTest` runs against `schema/src/test/resources/pml/pml-multiple-authors.xml` and `schema/src/test/resources/pml/113-java-maven-documentation.xml` after migration
- **THEN** both fixtures validate successfully against the local `pml.xsd`
- **AND** neither fixture contains a bare `<author>` element as a direct child of `<metadata>`

#### Scenario: Project version reflects 0.9.0
- **WHEN** the 0.9.0 release is prepared
- **THEN** `pom.xml`, `types/pom.xml`, `cli/pom.xml`, and `schema/pom.xml` all declare version `0.9.0`

