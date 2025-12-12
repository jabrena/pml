package info.jab.pml.cli.command;

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

@DisplayName("ValidateCommand tests")
class ValidateCommandTest {

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
    void validate_withValidPmlFile_shouldReturnSuccess() throws Exception {
        // Given
        Path validPmlFile = Paths.get(getClass().getResource("/pml/pml-hello-world.xml").toURI());
        ValidateCommand command = new ValidateCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {"--file", validPmlFile.toString()};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
    }

    @Test
    void validate_withInvalidPmlFile_shouldReturnFailure() throws Exception {
        // Given
        Path invalidPmlFile = Paths.get(getClass().getResource("/pml/invalid-pml.xml").toURI());
        ValidateCommand command = new ValidateCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {"--file", invalidPmlFile.toString()};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(1);
        assertThat(errContent.toString(UTF_8)).contains("Validation failed");
    }

    @Test
    void validate_withNonExistentFile_shouldReturnFailure() {
        // Given
        ValidateCommand command = new ValidateCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {"--file", "/nonexistent/file.xml"};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isEqualTo(1);
        assertThat(errContent.toString(UTF_8)).contains("File not found");
    }

    @Test
    void validate_withMissingFileOption_shouldShowError() {
        // Given
        ValidateCommand command = new ValidateCommand();
        CommandLine cmd = new CommandLine(command);
        String[] args = {};

        // When
        int exitCode = cmd.execute(args);

        // Then
        assertThat(exitCode).isNotEqualTo(0);
        assertThat(errContent.toString(UTF_8)).contains("--file");
    }
}
