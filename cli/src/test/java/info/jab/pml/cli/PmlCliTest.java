package info.jab.pml.cli;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

@DisplayName("PmlCli main class tests")
class PmlCliTest {

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
    void main_withNoArguments_shouldShowUsage() {
        // Given
        String[] args = {};

        // When
        int exitCode = new CommandLine(new PmlCli()).execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        assertThat(outContent.toString(UTF_8)).contains("Usage:");
    }

    @Test
    void main_withHelpOption_shouldShowUsage() {
        // Given
        String[] args = {"--help"};

        // When
        int exitCode = new CommandLine(new PmlCli()).execute(args);

        // Then
        assertThat(exitCode).isEqualTo(0);
        assertThat(outContent.toString(UTF_8)).contains("Usage:");
    }

    @Test
    void main_withValidateSubcommand_shouldExecuteValidate() {
        // Given
        PmlCli cli = new PmlCli();
        CommandLine cmd = new CommandLine(cli);

        // When/Then
        // Verify that validate subcommand is registered
        assertThat(cmd.getSubcommands().containsKey("validate")).isTrue();
        CommandLine validateSubcommand = cmd.getSubcommands().get("validate");
        assertThat(validateSubcommand).isNotNull();
        Object command = validateSubcommand.getCommand();
        assertThat(command).isInstanceOf(ValidateCommand.class);
    }

    @Test
    void main_withConvertSubcommand_shouldExecuteConvert() {
        // Given
        PmlCli cli = new PmlCli();
        CommandLine cmd = new CommandLine(cli);

        // When/Then
        // Verify that convert subcommand is registered
        assertThat(cmd.getSubcommands().containsKey("convert")).isTrue();
        CommandLine convertSubcommand = cmd.getSubcommands().get("convert");
        assertThat(convertSubcommand).isNotNull();
        Object command = convertSubcommand.getCommand();
        assertThat(command).isInstanceOf(ConvertCommand.class);
    }
}
