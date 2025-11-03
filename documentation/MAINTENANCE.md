# Maintenance

Some **User prompts** designed to help in the maintenance of this repository.

```bash
# Prompt to provide a release changelog
Can you update the current changelog for 0.2.0 comparing git commits in relation to 0.1.0 tag. Use  @https://keepachangelog.com/en/1.1.0/  rules
```

## Release process

- [ ] Update changelog
- [ ] Remove SNAPSHOT from .xml, .md & pom.xml
- [ ] Last review in docs (Manual)
- [ ] Review git changes for hidden issues (Manual) https://github.com/jabrena/cursor-rules-java/compare/0.10.0...feature/release-0110
- [ ] Tag repository
- [ ] Create article
- [ ] Communicate in social media

---

```bash
# Prompt to provide a release changelog
Can you update the current changelog for 0.11.0 comparing git commits in relation to 0.10.0 tag. Use  @https://keepachangelog.com/en/1.1.0/  rules

# Prompt to update the project to a new version
Update xml files from @resources/ and update the version to 0.11.0 removing snapshot. Update @pom.xml with the new version 0.11.0 Generate system prompts again with ./mvnw clean install -pl system-prompts-generator

## Note: Refactor a bit more to include all pom.xml

## Tagging process
git tag --list
git tag 0.11.0
git push --tags
```
