# Prompt Markup Language

The home of XML Schemas for PML (Prompt Markup Language), PML Workflow and related Java Types

## PML, Prompt Markup Language

### Motivation

PML was created to design good `User Prompts` & `System Prompts` to `mitigate ambiguity` in frontier model's execution.

PML is an `XML Schema` that helps software engineers model `User Prompts` & `System Prompts` effectively. Once the prompt is modeled in XML, it can be converted into `Markdown`, a format that models understand well. Using the PML Schema guidelines, it is easier to ensure that nothing important is missed when defining a prompt designed for production environments.

### Why XML format?

Java software engineers use XML daily because `Maven` is based on XML and it has a schema, so it is a familiar format for everyone.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>info.jab.pml</groupId>
    <artifactId>prompt-markup-language</artifactId>
    <version>0.3.0</version>
    <packaging>pom</packaging>

    <name>prompt-markup-language</name>

    <properties>
        <java.version>25</java.version>
        <maven.version>3.9.10</maven.version>
    </properties>

    ...
</project>
```

The XML format has several features that are pretty interesting for this project:

- Syntax validation with an XML Schema
- The capacity to transform to another format like Markdown
- The capacity to compose documents from other sources
- The capacity to mix heterogeneous sources from different technologies like bash scripts, other documents, examples written in Java inside XML comments
- Good support in Java
- Good tooling support in in JVM ecosystem

With these premises in mind, PML was designed.

### Examples

**Hello World in console:**

You could define the following prompt:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<prompt>
    <goal>
        Print "Hello World" in the console
    </goal>
</prompt>
```

But with this anemic prompt, exist a high probability that frontier models could not implement in the same way that you have in mind.

Lets iterate this idea to show how to use `PML`.

[`PML Schema`](./schema/src/main/resources/pml.xsd) define the following parts:

```xml
<!-- Root element for a prompt -->
<xs:element name="prompt">
    <xs:complexType>
        <xs:sequence>
            <xs:element ref="metadata" minOccurs="0"/>
            <xs:element ref="title" minOccurs="0"/>
            <xs:element ref="role" minOccurs="0"/>
            <xs:element ref="tone" minOccurs="0"/>
            <xs:element ref="context" minOccurs="0"/>
            <xs:element ref="goal"/>
            <xs:element ref="constraints" minOccurs="0"/>
            <xs:element ref="steps" minOccurs="0" />
            <xs:element ref="examples" minOccurs="0"/>
            <xs:element ref="output-format" minOccurs="0"/>
            <xs:element ref="safeguards" minOccurs="0"/>
            <xs:element ref="acceptance-criteria" minOccurs="0"/>
        </xs:sequence>
    </xs:complexType>
</xs:element>
```

Some parts are mandatory and others not.
Lets continue with the next example implementing the same idea in Java.

**Hello World in Java:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<prompt xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:noNamespaceSchemaLocation="https://jabrena.github.io/pml/schemas/0.3.0/pml.xsd">
    <title>Develop a HelloWorld Java Class program</title>

    <role>You are a Senior software engineer with extensive experience in Java software development</role>

    <goal>Develop a classic Java class HelloWorld.java program that print "Hello World" in the console only</goal>

    <constraints>
        <constraint-list>
            <constraint>The develop the class in the Maven module `sandbox`</constraint>
            <constraint>The develop the class in the package info.jab.examples</constraint>
            <constraint>Don't invest time in planning</constraint>
            <constraint>Don't create any test class</constraint>
            <constraint>Don't touch the build file (pom.xml)</constraint>
        </constraint-list>
    </constraints>

    <output-format>
        <output-format-list>
            <output-format-item>Don't explain anything</output-format-item>
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

In your prompts, you could have to include scripts like in this prompt:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<prompt xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:noNamespaceSchemaLocation="https://jabrena.github.io/pml/schemas/0.3.0/pml.xsd">
    <title>Install Java 25 in Cursor Cloud Agent</title>

    <role>System Administrator with expertise in Java development environments and package management for Linux systems</role>

    <goal>Update the VM to Java 25. Follow the next step to achieve the goal.</goal>

    <steps>
        <step number="1">
            <step-title>Install Java 25 with SDKMAN</step-title>
            <step-content>
                ```bash
                sudo apt update
                # Install SDKMAN
                curl -s "https://get.sdkman.io" | bash
                source "$HOME/.sdkman/bin/sdkman-init.sh"
                sdk install java 25.0.1-graalce
                sdk default java 25.0.1-graalce
                java -version
                ```
            </step-content>
            <step-constraints>
                <step-constraint-list>
                    <step-constraint>Do not invest time in planning</step-constraint>
                    <step-constraint>Only install the component with the given command</step-constraint>
                </step-constraint-list>
            </step-constraints>
        </step>
    </steps>

    <output-format>Do not explain anything</output-format>

    <safeguards>Verify that java is configured for java 25 executing `java -version`</safeguards>

    <acceptance-criteria>
        <acceptance-criteria-list>
            <acceptance-criteria-item>The java version is 25</acceptance-criteria-item>
            <acceptance-criteria-item>The java installation is successful</acceptance-criteria-item>
        </acceptance-criteria-list>
    </acceptance-criteria>
</prompt>
```

## PML Workflows

### Motivation

Using Frontier models, you could delegate coding tasks, and the final outcome of a task could be produced by executing a set of prompts.

A PML Workflow file define the way of a set prompts will be executed by an Engine

### Prompts formats

Prompts can be defined in 3 different ways:

- PML (*.pml)
- Markdown (*.md)
- Plain text (.txt)

### Agentic patterns

#### Sequence pattern

```xml
<?xml version="1.0" encoding="UTF-8"?>
<pml-workflow>
    <sequence model="default" repository="https://github.com/jabrena/wjax25-demos">
        <prompt src="prompt1.xml" />
        <prompt src="prompt2.md" />
        <prompt src="prompt3.txt" />
    </sequence>
</pml-workflow>
```

#### Parallel pattern

```xml
<?xml version="1.0" encoding="UTF-8"?>
<pml-workflow xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:noNamespaceSchemaLocation="https://jabrena.github.io/pml/schemas/0.3.0-SNAPSHOT/pml-workflow.xsd">
    <sequence
        model="default" repository="https://github.com/jabrena/wjax25-demos"
        timeout="5m" fallback-src="fallback-prompt.xml" fallback-type="pml">
        <prompt src="prompt1.xml" />
        <prompt src="prompt2.md" />
    </sequence>
</pml-workflow>
```

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

- https://www.promptml.org/
- https://www.anthropic.com/engineering/building-effective-agents

## Cursor rules ecosystem

- https://github.com/jabrena/101-cursor
- https://github.com/jabrena/pml
- https://github.com/jabrena/cursor-rules-agile
- https://github.com/jabrena/cursor-rules-java
- https://github.com/jabrena/cursor-rules-spring-boot
- https://github.com/jabrena/plantuml-to-png-cli
- https://github.com/jabrena/setup-cli

Powered by [Cursor](https://www.cursor.com/) with ❤️ from [Madrid](https://www.google.com/maps/place/Community+of+Madrid,+Madrid/@40.4983324,-6.3162283,8z/data=!3m1!4b1!4m6!3m5!1s0xd41817a40e033b9:0x10340f3be4bc880!8m2!3d40.4167088!4d-3.5812692!16zL20vMGo0eGc?entry=ttu&g_ep=EgoyMDI1MDgxOC4wIKXMDSoASAFQAw%3D%3D)
