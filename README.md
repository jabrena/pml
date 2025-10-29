# Prompt Markup Language

The home of XML Schema for Prompt Markup Language and related Java Types

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

## XML Schemas

## PML

```xml
<?xml version="1.0" encoding="UTF-8"?>
<prompt>
    <goal>
        <goal>
            Print "Hello World" in the console
        </goal>
    </goal>
</prompt>
```

## PML Worflow

### Sequence pattern

```xml
<?xml version="1.0" encoding="UTF-8"?>
<pml-workflow>
    <sequence model="default" repository="https://github.com/jabrena/wjax25-demos">
        <prompt src="prompt1.xml" type="pml" />
        <prompt src="prompt2.md" type="md" />
    </sequence>
</pml-workflow>
```

### Parallel pattern

```xml
<?xml version="1.0" encoding="UTF-8"?>
<pml-workflow>
    <parallel src="prompt-toc.xml" type="pml" bindResultType="List_Integer">
        <sequence model="default" repository="https://github.com/jabrena/wjax25-demos">
            <prompt src="prompt2.xml" type="pml" bindResultExp="$get()"/>
        </sequence>
    </parallel>
</pml-workflow>
```

### Router pattern

PENDING

### Judge pattern

PENDING

Powered by [Cursor](https://www.cursor.com/) with ❤️ from [Madrid](https://www.google.com/maps/place/Community+of+Madrid,+Madrid/@40.4983324,-6.3162283,8z/data=!3m1!4b1!4m6!3m5!1s0xd41817a40e033b9:0x10340f3be4bc880!8m2!3d40.4167088!4d-3.5812692!16zL20vMGo0eGc?entry=ttu&g_ep=EgoyMDI1MDgxOC4wIKXMDSoASAFQAw%3D%3D)
