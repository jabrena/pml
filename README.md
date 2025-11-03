# Prompt Markup Language

The home of XML Schemas for PML (Prompt Markup Language), PML Workflow and related Java Types

## XML Schemas

## PML, Prompt Markup Language

### Examples

**Hello World in console:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<prompt>
    <goal>
        Print "Hello World" in the console
    </goal>
</prompt>
```

**Hello World in Java:**

```java
<?xml version="1.0" encoding="UTF-8"?>
<prompt xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:noNamespaceSchemaLocation="https://jabrena.github.io/pml/schemas/0.2.0/pml.xsd">
    <title>Develop a HelloWorld Java Class program</title>

    <role>You are a Senior software engineer with extensive experience in Java software development</role>

    <goal>
Develop a classic Java class HelloWorld.java program
that print "Hello World" in the console only
    </goal>

    <constraints>
        <constraint-list>
            <constraint>The develop the class in the Maven module `sandbox`</constraint>
            <constraint>The develop the class in the package info.jab.examples</constraint>
            <constraint>Do not invest time in planning</constraint>
            <constraint>Do not create any test class</constraint>
            <constraint>Do not touch the build file (pom.xml)</constraint>
        </constraint-list>
    </constraints>

    <output-format>
        <output-format-list>
            <output-format-item>Don not explain anything</output-format-item>
        </output-format-list>
    </output-format>

    <safeguards>
        <safeguards-list>
            <safeguards-item>Build the solution with Maven Only</safeguards-item>
        </safeguards-list>
    </safeguards>

    <acceptance-criteria>
        <acceptance-criteria-list>
            <acceptance-criteria-item>The solution is compiled successfully with `./mvnw clean compile -pl sandbox`</acceptance-criteria-item>
            <acceptance-criteria-item>The solution only prints "Hello World" in the console</acceptance-criteria-item>
            <acceptance-criteria-item>The solution is committed and pushed to the branch to create the PR</acceptance-criteria-item>
            <acceptance-criteria-item>Only commit java sources only and push the changes to the branch to create the PR</acceptance-criteria-item>
        </acceptance-criteria-list>
    </acceptance-criteria>
</prompt>
```

**Java 25 Installation:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<prompt>
    <title>Install Java 25 in Cursor Cloud Agent</title>

    <role>System Administrator with expertise in Java development environments and package management for Linux systems</role>

    <goal>Update the VM to Java 25 using the following commands:

    ```bash
    sudo apt update
    # Install SDKMAN
    curl -s "https://get.sdkman.io" | bash
    source "$HOME/.sdkman/bin/sdkman-init.sh"
    sdk install java 25.0.1-graalce
    sdk default java 25.0.1-graalce
    java -version
    ```
    </goal>

    <constraints>
        <constraint-list>
            <constraint>Do not invest time in planning</constraint>
            <constraint>Only install the component with the given command</constraint>
        </constraint-list>
    </constraints>

    <output-format>
        <output-format-list>
            <output-format-item>Do not explain anything</output-format-item>
        </output-format-list>
    </output-format>

    <safeguards>
        <safeguards-list>
            <safeguards-item>verify that java is configured for java 25 executing `java -version`</safeguards-item>
        </safeguards-list>
    </safeguards>

    <acceptance-criteria>
        <acceptance-criteria-list>
            <acceptance-criteria-item>The java version is 25</acceptance-criteria-item>
            <acceptance-criteria-item>The java installation is successful</acceptance-criteria-item>
        </acceptance-criteria-list>
    </acceptance-criteria>
</prompt>
```

## PML Workflow

### Sequence pattern

```xml
<?xml version="1.0" encoding="UTF-8"?>
<pml-workflow>
    <sequence model="default" repository="https://github.com/jabrena/wjax25-demos">
        <prompt src="prompt1.xml"   type="pml" />
        <prompt src="prompt2.md"    type="md" />
        <prompt src="prompt3.txt"   type="txt" />
    </sequence>
</pml-workflow>
```

### Parallel pattern

```xml
<?xml version="1.0" encoding="UTF-8"?>
<pml-workflow xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:noNamespaceSchemaLocation="https://jabrena.github.io/pml/schemas/0.2.0/pml-workflow.xsd">
    <sequence
        model="default" repository="https://github.com/jabrena/wjax25-demos"
        timeout="5m" fallback-src="fallback-prompt.xml" fallback-type="pml">
        <prompt src="prompt1.xml" type="pml" />
        <prompt src="prompt2.md" type="pml"/>
    </sequence>
</pml-workflow>
```

New Agentic patterns soon.

## Java Bindings

```java
import info.jab.pml.model.*;

Prompt prompt = PromptBuilder.create()
    .withRole("""
        You are a Senior software engineer
        with extensive experience in Java software development
        """)
    .withGoal("Help the Java developer ecosystem to make life easier")
    .build();
```

## Changelog

- Review the [CHANGELOG](./CHANGELOG.md) for further details

## References

- https://github.com/microsoft/poml
- https://www.promptml.org/
- https://www.anthropic.com/engineering/building-effective-agents

Powered by [Cursor](https://www.cursor.com/) with ❤️ from [Madrid](https://www.google.com/maps/place/Community+of+Madrid,+Madrid/@40.4983324,-6.3162283,8z/data=!3m1!4b1!4m6!3m5!1s0xd41817a40e033b9:0x10340f3be4bc880!8m2!3d40.4167088!4d-3.5812692!16zL20vMGo0eGc?entry=ttu&g_ep=EgoyMDI1MDgxOC4wIKXMDSoASAFQAw%3D%3D)
