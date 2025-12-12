# Maintenance

Some **User prompts** designed to help in the maintenance of this repository.

```bash
# Prompt to provide a release changelog
Can you update the current changelog for 0.2.0 comparing git commits in relation to 0.1.0 tag. Use  @https://keepachangelog.com/en/1.1.0/  rules
```

## Release process

- [ ] Update CHANGELOG.md
- [ ] Review/Update pipeline
- [ ] Remove SNAPSHOT from pom.xml
- [ ] Last review in docs (Manual)
- [ ] Update website /docs/index.html
- [ ] Review git changes for hidden issues (Manual) https://github.com/jabrena/pml/compare/0.2.0...0.3.0 https://github.com/jabrena/pml/compare/0.2.0...feature/release-030
- [ ] Tag repository
- [ ] Release Snapshot/Release
- [ ] Update Jbang-catalog with new cli pml-to-md
- [ ] Create article
- [ ] Communicate in social media

---

```bash
# Prompt to provide a release changelog
Can you update the current changelog for 0.4.0-SNAPSHOT comparing git commits in relation to 0.3.0 tag. Use  @https://keepachangelog.com/en/1.1.0/  rules

./mvnw versions:set -DnewVersion=0.4.0-SNAPSHOT
./mvnw versions:commit
## Note: Refactor a bit more to include all pom.xml

## Tagging process
git tag --list
git tag 0.4.0-SNAPSHOT
git push --tags
```
## Undo Tags if something goes wrong

```bash
# Delete locally
git tag -d 0.4.0-SNAPSHOT

# Delete from remote
git push origin --delete 0.4.0-SNAPSHOT
```
