## Context

`jabrena/pml` publishes the PML XML Schema (`pml.xsd`, `pml-workflow.xsd`) consumed by this repository's own fixtures and by at least one external, separately-maintained repository (`plinth`, specifically its `plinth-skills-generator` module). The 0.8.0 release intentionally kept both the deprecated singular `<author>` and the new `<authors>` element side by side as a transition aid (see `CHANGELOG.md` 0.8.0 entry). Issue #137 asks to complete that transition in 0.9.0 by removing the deprecated `author` reference from `<metadata>` and the `<triggers>` reference from `<prompt>`, since `<triggers>` conceptually belongs to a skills-only shape.

The Functional Specification comment posted on the issue (`/explore-problem`, comment `#issuecomment-5083335738`) analyzed this change primarily from the perspective of the downstream `plinth` repository — `plinth-skills-generator`, `skill-references/*.xml`, `skill-indexes/*.xml`, `AGENTS.md`/`CLAUDE.md`, and `RemoteSchemaValidationTest`. Repository inspection of `jabrena/pml` (this repository) on 2026-07-26 confirms none of those paths exist here; they are exclusively part of `plinth`. This design therefore scopes the change to what `jabrena/pml` actually owns: the schema definition itself, its local test fixtures, its published/versioned artifacts, and its build metadata. The cross-repository consequences the Functional Specification raised remain valid and are recorded below as an explicit, unresolved carry-forward — not silently dropped.

## Goals / Non-Goals

**Goals:**
- Remove the deprecated singular `<author>` reference from `<metadata>`'s content model in `pml.xsd`, retaining the reusable `<authors>`/`<author>` shape.
- Remove the `<triggers>` reference from `<prompt>`'s content model in `pml.xsd`.
- Publish the updated schema as version 0.9.0 (`docs/schemas/0.9.0/`), following the existing versioning/publishing pattern used for 0.1.0 through 0.8.0.
- Keep this repository's own fixtures and tests (`SchemaPMLValidationTest`, `SchemaPMLWorkflowValidationTest`) green against the updated schema.

**Non-Goals:**
- Migrating any file in the `plinth` repository (out of this repository's control and outside this change's file footprint).
- Resolving whether `plinth`'s local `skills.xsd` mirror keeps or drops `<triggers>` (see Open Question below).
- Removing the 0.8.0 published schema or breaking existing consumers pinned to it; 0.8.0 remains published unchanged.
- Introducing a single source of truth for the version string beyond what `./mvnw versions:set` already centralizes across the Maven modules (a maintainability concern the Functional Specification raised as QA-4, but not requested as an acceptance criterion by the issue or the Acceptance Criteria comment).

## Decisions

1. **Retain the global `<xs:element name="author" type="xs:string"/>` declaration.** Only the `<xs:element ref="author" minOccurs="0"/>` line inside `<metadata>`'s sequence is removed. The global `author` element stays because `<authors>` still composes it (`<xs:element ref="author" minOccurs="1" maxOccurs="unbounded"/>`). This matches the issue's literal acceptance criterion ("the deprecated `<xs:element ref="author" minOccurs="0"/>` element is removed") without breaking `<authors>`.

2. **Scope the `<triggers>` removal to the `<prompt>` content-model reference only**, matching the issue's literal acceptance criterion ("the `<xs:element ref="triggers" minOccurs="0"/>` element is removed"). Whether the now-unreferenced global `<xs:element name="triggers">`, `trigger-list`, and `trigger` declarations should also be deleted from `pml.xsd` entirely, versus left in place as reusable building blocks, is left as an implementation-time judgment call for `/explore-design` — it does not change validation behavior for any document (an orphaned global element declaration that nothing references does not appear in, or invalidate, a `<prompt>` document) and is not covered by an acceptance criterion.

3. **Scope this OpenSpec change to `jabrena/pml` only.** The Functional Specification's downstream analysis (plinth's `RemoteSchemaValidationTest`, `AGENTS.md`/`CLAUDE.md`, skill XML sources) describes real consequences, but none of those artifacts exist in this repository. A separate OpenSpec change in the `plinth` repository is required to carry out that migration once 0.9.0 is published here.

## Open Question (carried forward, unresolved — do not resolve in this change)

**U2 (from the Functional Specification):** Should `plinth`'s local `skills.xsd` mirror also drop `<triggers>` to match the new upstream `pml.xsd`, or intentionally diverge and keep it, given all 125 `skill-index` files in `plinth` currently depend on `<triggers>`? `plinth`'s `pml-skills-schema/spec.md` currently requires `skills.xsd` to be a "complete, unchanged copy" of upstream `pml.xsd` — a contract written before this removal was proposed, and one this repository (`pml`) has no authority to amend.

This question is **not answered by this change**. It becomes actionable only once this change publishes `pml.xsd` 0.9.0; at that point it must be resolved in the `plinth` repository (via its own `/explore-design` and OpenSpec change) before `plinth` can adopt 0.9.0. Publishing 0.9.0 from `jabrena/pml` does not force `plinth` to upgrade immediately — `plinth` can remain pinned to 0.8.0 until that decision is made.

## Risks / Trade-offs

- **Downstream breakage if `plinth` upgrades before U2 is resolved.** If `plinth` is repointed to 0.9.0 without first deciding U2, its `skills.xsd` mirror (if kept "unchanged") would silently lose `<triggers>`, breaking all 125 `skill-index` files. Mitigation: this change does not touch `plinth`, does not remove 0.8.0, and this design explicitly flags the dependency so it is visible to whoever plans the `plinth`-side change.
- **Two local fixtures change meaning.** `pml-multiple-authors.xml` currently exercises the transitional "both `author` and `authors` present" case. After migration it will only exercise multiple `<author>` entries under `<authors>`; the "legacy singular author" case it also covered will no longer exist as a passing example. This is the intended effect of finishing the 0.8.0→0.9.0 transition and matches the Acceptance Criteria comment's scenario "Skill-reference XML still using the deprecated singular author fails validation" (same principle, applied to this repo's own fixtures).
- **Version bump touches four `pom.xml` files and generates a new `schema-0.9.0.jar`.** Low risk; this repeats the exact mechanical steps already documented in `documentation/MAINTENANCE.md`'s release process (`./mvnw versions:set -DnewVersion=0.9.0`, `./mvnw versions:commit`).
