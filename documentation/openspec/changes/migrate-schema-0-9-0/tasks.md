## 1. Schema changes

- [x] 1.1 Remove `<xs:element ref="author" minOccurs="0"/> <!-- DEPRECATED-->` from `<metadata>`'s sequence in `schema/src/main/resources/pml.xsd`; keep the global `<xs:element name="author" type="xs:string"/>` declaration (still used by `<authors>`).
- [x] 1.2 Remove `<xs:element ref="triggers" minOccurs="0" />` from `<prompt>`'s sequence in `schema/src/main/resources/pml.xsd`.
- [x] 1.3 Decide (per `design.md` Decision 2) whether to also delete the now-unreferenced global `triggers`, `trigger-list`, and `trigger` element declarations from `pml.xsd`, or leave them in place; apply the decision consistently.

## 2. Local fixtures

- [x] 2.1 Migrate `schema/src/test/resources/pml/pml-multiple-authors.xml` to declare only `<authors><author>...</author></authors>` (drop the standalone `<author>Juan Antonio</author>` line while preserving multi-author coverage).
- [x] 2.2 Migrate `schema/src/test/resources/pml/113-java-maven-documentation.xml` from singular `<author>` to `<authors><author>...</author></authors>`.
- [x] 2.3 Run `./mvnw -pl schema test` (or `./mvnw clean install -pl schema -am`) and confirm `SchemaPMLValidationTest` and `SchemaPMLWorkflowValidationTest` pass.

## 3. Version bump and publishing

- [x] 3.1 Run `./mvnw versions:set -DnewVersion=0.9.0` followed by `./mvnw versions:commit` to update `pom.xml`, `types/pom.xml`, `cli/pom.xml`, `schema/pom.xml`.
- [x] 3.2 Publish `docs/schemas/0.9.0/pml.xsd` and `docs/schemas/0.9.0/pml-workflow.xsd` (copies of the updated schema files), leaving `docs/schemas/0.8.0/` and all earlier versions unchanged.
- [x] 3.3 Add a version 0.9.0 entry to `docs/schemas/index.html` following the existing per-version card pattern, listed above the 0.8.0 entry.
- [x] 3.4 Add a `## [0.9.0]` entry to `CHANGELOG.md` (Keep a Changelog format) documenting removal of the deprecated `author` element and the `triggers` reference from `pml.xsd`.

## 4. Repository-wide verification

- [x] 4.1 Run a repository-wide search for the literal string `0.8.0` outside `CHANGELOG.md` and archived/historical `docs/schemas/0.8.0/` and `docs/schemas/0.7.0/`-and-earlier content, to confirm no other reference to the old version was missed.
- [x] 4.2 Run `./mvnw clean verify` (or the project's documented full build command) at the repository root and confirm it passes.
- [x] 4.3 Confirm `docs/schemas/0.8.0/pml.xsd` and `docs/schemas/0.8.0/pml-workflow.xsd` are byte-for-byte unchanged (0.8.0 remains a valid, stable, previously published version).

## 5. Downstream coordination (tracking only — not implemented by this change)

- [ ] 5.1 Notify/track that the `plinth` repository needs its own migration (schema-version references, `RemoteSchemaValidationTest.REMOTE_XSD`, `skill-references/*.xml`, `skill-indexes/*.xml`) once 0.9.0 is published here; this is out of scope for this change and requires a separate OpenSpec change in `plinth`.
- [ ] 5.2 Ensure the `plinth`-side change explicitly resolves open question U2 (whether `plinth`'s `skills.xsd` mirror keeps or drops `<triggers>`) before `plinth` adopts schema 0.9.0 — do not resolve it here.
