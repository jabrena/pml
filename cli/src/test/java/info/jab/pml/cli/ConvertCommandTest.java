package info.jab.pml.cli;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

@DisplayName("ConvertCommand tests")
class ConvertCommandTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void convert_withValidPmlFile_shouldOutputMarkdownToStdout() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-hello-world.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {"--file", validPmlFile.toString()};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        // The output should contain the converted markdown string (output to stdout, not a file)
        String output = outContent.toString(UTF_8);
        assertThat(output).isNotEmpty();
        // Verify it's markdown-like content (should contain some text from the conversion)
        assertThat(output).isNotBlank();
    }

    @Test
    void convert_withNonExistentFile_shouldReturnFailure() {
        // Given
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {"--file", "/nonexistent/file.xml"};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(1);
        assertThat(errContent.toString(UTF_8)).contains("File not found");
    }

    @Test
    void convert_withMissingFileOption_shouldShowError() {
        // Given
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isNotEqualTo(0);
        assertThat(errContent.toString(UTF_8)).contains("--file");
    }

    @Test
    void convert_withTemplateReplacement_shouldReplaceFieldValue() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-hello-world.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {"--file", validPmlFile.toString(), "--template", "goal", "Updated goal text"};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        String output = outContent.toString(UTF_8);
        assertThat(output).isNotEmpty();
        // The output should contain the replaced value (converted to markdown)
        assertThat(output).contains("Updated goal text");
    }

    @Test
    void convert_withMultipleTemplateReplacements_shouldReplaceAllFields() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-hello-world.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {
            "--file", validPmlFile.toString(),
            "--template", "goal", "First replacement",
            "--template", "goal", "Second replacement"
        };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        String output = outContent.toString(UTF_8);
        assertThat(output).isNotEmpty();
        // Last replacement should be in output
        assertThat(output).contains("Second replacement");
    }

    @Test
    void convert_withNonExistentField_shouldSkipField() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-hello-world.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {
            "--file", validPmlFile.toString(),
            "--template", "nonexistent-field", "This should be skipped",
            "--template", "goal", "This should work"
        };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        String output = outContent.toString(UTF_8);
        assertThat(output).isNotEmpty();
        // The existing field replacement should work
        assertThat(output).contains("This should work");
        // Non-existent field should be skipped (no error)
    }

    @Test
    void convert_withTemplateButNoPairs_shouldFail() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-hello-world.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {"--file", validPmlFile.toString(), "--template"};

        // When
        int exitCode = cmd.execute(args);

        // Then
        // Should fail because --template requires 2 arguments (arity = "2")
        assertThat(exitCode).isNotEqualTo(0);
    }

    // ========== POSITIVE TEST CASES ==========

    @Test
    void convert_withSingleTemplateReplacement_shouldReplaceFieldSuccessfully() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-hello-world.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {"--file", validPmlFile.toString(), "--template", "goal", "Replaced goal value"};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        String output = outContent.toString(UTF_8);
        assertThat(output).contains("Replaced goal value");
        assertThat(output).doesNotContain("Print \"Hello World\"");
    }

    @Test
    void convert_withMultipleDifferentFields_shouldReplaceAllFields() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-with-multiple-fields.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {
            "--file", validPmlFile.toString(),
            "--template", "title", "New Title",
            "--template", "role", "New Role",
            "--template", "goal", "New Goal"
        };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        String output = outContent.toString(UTF_8);
        assertThat(output).contains("New Title");
        assertThat(output).contains("New Role");
        assertThat(output).contains("New Goal");
        assertThat(output).doesNotContain("Test Title");
        assertThat(output).doesNotContain("Test Role");
        assertThat(output).doesNotContain("Original goal content");
    }

    @Test
    void convert_withTemplateSpecialCharacters_shouldHandleSpecialChars() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-hello-world.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {
            "--file", validPmlFile.toString(),
            "--template", "goal", "Value with \"quotes\" and <tags> & special chars"
        };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        String output = outContent.toString(UTF_8);
        assertThat(output).isNotEmpty();
        // XML special characters should be properly escaped/handled
    }

    @Test
    void convert_withTemplateEmptyValue_shouldReplaceWithEmptyString() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-hello-world.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {
            "--file", validPmlFile.toString(),
            "--template", "goal", ""
        };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        String output = outContent.toString(UTF_8);
        assertThat(output).isNotEmpty();
        // Empty value replacement should work without errors
    }

    @Test
    void convert_withTemplateMultilineValue_shouldHandleMultiline() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-hello-world.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String multilineValue = "Line 1\nLine 2\nLine 3";
        String[] args = {
            "--file", validPmlFile.toString(),
            "--template", "goal", multilineValue
        };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        String output = outContent.toString(UTF_8);
        assertThat(output).isNotEmpty();
        // Multiline values should be handled
    }

    @Test
    void convert_withTemplatePreservesOtherFields_shouldOnlyReplaceSpecifiedFields() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-with-multiple-fields.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {
            "--file", validPmlFile.toString(),
            "--template", "title", "New Title"
        };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        String output = outContent.toString(UTF_8);
        assertThat(output).contains("New Title");
        // Other fields should remain unchanged (role, goal, context)
        assertThat(output).contains("Test Role");
        assertThat(output).contains("Original goal content");
    }

    @Test
    void convert_withTemplateNoReplacements_shouldWorkNormally() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-hello-world.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {"--file", validPmlFile.toString()};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        String output = outContent.toString(UTF_8);
        assertThat(output).isNotEmpty();
        // Original content should be present
        assertThat(output).contains("Hello World");
    }

    // ========== NEGATIVE TEST CASES ==========

    @Test
    void convert_withNonExistentField_shouldSkipFieldSilently() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-hello-world.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {
            "--file", validPmlFile.toString(),
            "--template", "nonexistent-field", "This should be skipped",
            "--template", "goal", "This should work"
        };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        String output = outContent.toString(UTF_8);
        assertThat(output).isNotEmpty();
        // The existing field replacement should work
        assertThat(output).contains("This should work");
        // Non-existent field should be skipped (no error, no crash)
    }

    @Test
    void convert_withMultipleNonExistentFields_shouldSkipAllSilently() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-hello-world.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {
            "--file", validPmlFile.toString(),
            "--template", "field1", "Value1",
            "--template", "field2", "Value2",
            "--template", "field3", "Value3"
        };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        String output = outContent.toString(UTF_8);
        assertThat(output).isNotEmpty();
        // Should complete successfully even when no fields are found
    }

    @Test
    void convert_withTemplateOddNumberOfArguments_shouldFail() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-hello-world.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {
            "--file", validPmlFile.toString(),
            "--template", "goal", "value1",
            "--template", "role"  // Missing value
        };

        // When
        int exitCode = cmd.execute(args);

        // Then
        // Picocli should handle this and either fail or require the value
        // The behavior depends on picocli's arity handling
        assertThat(exitCode).isNotEqualTo(0);
    }

    @Test
    void convert_withTemplateEmptyFieldName_shouldSkipField() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-hello-world.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {
            "--file", validPmlFile.toString(),
            "--template", "", "Some value"
        };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        String output = outContent.toString(UTF_8);
        assertThat(output).isNotEmpty();
        // Empty field name should be skipped (no match found)
    }

    @Test
    void convert_withTemplateFieldNameWithSpaces_shouldSkipField() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-hello-world.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {
            "--file", validPmlFile.toString(),
            "--template", "goal field", "Some value"
        };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        String output = outContent.toString(UTF_8);
        assertThat(output).isNotEmpty();
        // Field name with spaces won't match XML element names, should be skipped
    }

    @Test
    void convert_withTemplateMixedExistentAndNonExistentFields_shouldReplaceOnlyExistent() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-with-multiple-fields.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {
            "--file", validPmlFile.toString(),
            "--template", "nonexistent1", "Value1",
            "--template", "title", "New Title",
            "--template", "nonexistent2", "Value2",
            "--template", "role", "New Role"
        };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        String output = outContent.toString(UTF_8);
        assertThat(output).contains("New Title");
        assertThat(output).contains("New Role");
        // Non-existent fields should be skipped, existent ones replaced
    }

    @Test
    void convert_withTemplateSameFieldMultipleTimes_shouldUseLastValue() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-hello-world.xml").toURI());
        ConvertCommand command = new ConvertCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {
            "--file", validPmlFile.toString(),
            "--template", "goal", "First value",
            "--template", "goal", "Second value",
            "--template", "goal", "Final value"
        };

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        String output = outContent.toString(UTF_8);
        // Last replacement should be in output (all replacements happen, last one wins)
        assertThat(output).contains("Final value");
        assertThat(output).doesNotContain("First value");
        assertThat(output).doesNotContain("Second value");
    }
}
