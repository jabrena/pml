# Maintenance

Some **User prompts** designed to help in the maintenance of this repository.

```bash
# Prompt to provide a release changelog
Can you update the current changelog for 0.2.0 comparing git commits in relation to 0.1.0 tag. Use  @https://keepachangelog.com/en/1.1.0/  rules
```

## Release process

- [ ] Update CHANGELOG.md
- [ ] Remove SNAPSHOT from pom.xml
- [ ] Last review in docs (Manual)
- [ ] Review git changes for hidden issues (Manual) https://github.com/jabrena/pml/compare/0.1.0...0.2.0
- [ ] Tag repository
- [ ] Create article
- [ ] Communicate in social media

---

```bash
# Prompt to provide a release changelog
Can you update the current changelog for 0.2.0 comparing git commits in relation to 0.1.0 tag. Use  @https://keepachangelog.com/en/1.1.0/  rules

## Note: Refactor a bit more to include all pom.xml

## Tagging process
git tag --list
git tag 0.2.0
git push --tags
```
