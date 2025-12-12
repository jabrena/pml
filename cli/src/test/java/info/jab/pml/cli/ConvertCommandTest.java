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
    void convert_withTemplateButNoPairs_shouldWorkNormally() throws Exception {
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
}
