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

## XML Schema

https://jabrena.github.io/pml/pml.xsd

Powered by [Cursor](https://www.cursor.com/) with ❤️ from [Madrid](https://www.google.com/maps/place/Community+of+Madrid,+Madrid/@40.4983324,-6.3162283,8z/data=!3m1!4b1!4m6!3m5!1s0xd41817a40e033b9:0x10340f3be4bc880!8m2!3d40.4167088!4d-3.5812692!16zL20vMGo0eGc?entry=ttu&g_ep=EgoyMDI1MDgxOC4wIKXMDSoASAFQAw%3D%3D)
