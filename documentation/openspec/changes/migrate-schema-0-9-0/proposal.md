## Why

`pml.xsd` still carries the deprecated singular `<author>` element inside `<metadata>` (marked `<!-- DEPRECATED-->` since the 0.8.0 release, whose CHANGELOG entry explicitly frames it as a transitional field: "preserving the legacy `author` field for the 0.8.0 transition"), and a `<triggers>` reference inside `<prompt>`'s content model that conceptually belongs to a skills-only shape rather than this repository's shared system-prompt schema. Issue #137 asks to complete the 0.8.0 transition by removing both, publishing the result as schema version 0.9.0.

Repository verification (2026-07-26) confirms the exact footprint of this change inside `jabrena/pml`:
- `schema/src/main/resources/pml.xsd` declares both `<xs:element ref="author" minOccurs="0"/> <!-- DEPRECATED-->` and `<xs:element ref="authors" minOccurs="0"/>` in `<metadata>` (lines 40-41), and `<xs:element ref="triggers" minOccurs="0"/>` in `<prompt>` (line 16).
- Exactly two local test fixtures still use the deprecated singular `<author>`: `schema/src/test/resources/pml/pml-multiple-authors.xml` (uses both `<author>` and `<authors>` together, deliberately, to exercise the 0.8.0 transition) and `schema/src/test/resources/pml/113-java-maven-documentation.xml` (uses only singular `<author>`). Both are exercised by `schema/src/test/java/info/jab/pml/model/SchemaPMLValidationTest.java`.
- No test fixture in this repository uses `<triggers>` (`grep -rl trigger schema/src` only matches `pml.xsd` itself), so removing the reference from `<prompt>` does not break any local fixture.
- The literal string `0.8.0` also appears in `pom.xml`, `types/pom.xml`, `cli/pom.xml`, `schema/pom.xml` (project version), `docs/schemas/index.html` (published schema listing), and `CHANGELOG.md` (historical entries, left untouched).

## What Changes

- Remove the deprecated `<xs:element ref="author" minOccurs="0"/>` reference from `<metadata>`'s content model in `schema/src/main/resources/pml.xsd`. The global `<xs:element name="author" type="xs:string"/>` declaration is retained because `<authors>` still references it.
- Remove the `<xs:element ref="triggers" minOccurs="0"/>` reference from `<prompt>`'s content model in `schema/src/main/resources/pml.xsd`, so `<triggers>` is no longer part of a valid `<prompt>` document.
- Migrate the two local test fixtures that still use the deprecated singular `<author>` (`pml-multiple-authors.xml`, `113-java-maven-documentation.xml`) to the `<authors><author>...</author></authors>` shape only, so `SchemaPMLValidationTest` keeps passing against the updated schema.
- Bump the published schema version from 0.8.0 to 0.9.0: publish `docs/schemas/0.9.0/pml.xsd` and `docs/schemas/0.9.0/pml-workflow.xsd`, add a 0.9.0 entry to `docs/schemas/index.html`, and set the Maven project version to `0.9.0` across `pom.xml`, `types/pom.xml`, `cli/pom.xml`, `schema/pom.xml`.
- Add a `## [0.9.0]` entry to `CHANGELOG.md` documenting the removal of the deprecated `author` element and the `triggers` reference.

### Explicitly out of scope

- Migrating `skill-references/*.xml`, `skill-indexes/*.xml`, `AGENTS.md`/`CLAUDE.md` schema-version references, `RemoteSchemaValidationTest.REMOTE_XSD`, or the local `skills.xsd` mirror. None of these files exist in `jabrena/pml`; they belong to the separate, downstream `plinth` repository (`plinth-skills-generator` module). That repository consumes the schema published by this change but requires its own, separate migration and its own OpenSpec change.
- Resolving whether `plinth`'s local `skills.xsd` mirror should also drop `<triggers>` (to stay a byte-for-byte "unchanged copy" of upstream `pml.xsd` per that repo's `pml-skills-schema/spec.md`) or intentionally diverge and keep it, given all 125 `skill-index` files in `plinth` depend on `<triggers>`. This question (flagged as U2 in the issue's Functional Specification comment) is about a contract owned by the `plinth` repository and cannot be resolved by an artifact in this repository. It is carried forward unresolved — see `design.md`.

## Capabilities

### New Capabilities

- `pml-schema`: The XML Schema contract (`pml.xsd`, `pml-workflow.xsd`) that defines valid PML prompt documents, including its metadata authorship shape, `<prompt>` content model, and published version.

### Modified Capabilities

_None — no existing OpenSpec capability specs are present in this repository yet; `pml-schema` is introduced fresh by this change._

## Impact

- **Code**: `schema/src/main/resources/pml.xsd`, `schema/src/test/resources/pml/pml-multiple-authors.xml`, `schema/src/test/resources/pml/113-java-maven-documentation.xml`.
- **Published artifacts**: new `docs/schemas/0.9.0/` directory (GitHub Pages), `docs/schemas/index.html`.
- **Build**: `pom.xml`, `types/pom.xml`, `cli/pom.xml`, `schema/pom.xml` (version `0.8.0` → `0.9.0`); `schema-0.9.0.jar` produced instead of `schema-0.8.0.jar`.
- **Docs**: `CHANGELOG.md`.
- **Tests**: `SchemaPMLValidationTest` (local schema validation) and `SchemaPMLWorkflowValidationTest` must continue to pass; no assertions currently target the deprecated `author` element or `triggers`, so no test code changes are anticipated beyond the two fixture files.
- **Downstream (out of scope here, noted for awareness)**: any consumer pinned to `https://jabrena.github.io/pml/schemas/0.8.0/pml.xsd` (e.g. `plinth`'s `RemoteSchemaValidationTest`) will keep validating against the unchanged 0.8.0 schema until it explicitly moves to 0.9.0; this change does not remove or alter the published 0.8.0 schema.

## Source Traceability

- Issue: https://github.com/jabrena/pml/issues/137 ("Remove the single node author in favor of authors/author")
- Functional Specification comment (from `/explore-problem`): https://github.com/jabrena/pml/issues/137#issuecomment-5083335738
- Acceptance Criteria comment (from `/create-acceptance-criteria`): https://github.com/jabrena/pml/issues/137#issuecomment-5083469206
- Derivation direction: issue + both comments → this OpenSpec change. No content was synchronized back into the issue or its comments.
