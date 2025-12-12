package info.jab.pml.cli;

import org.jspecify.annotations.Nullable;
import picocli.CommandLine;

@CommandLine.Command(
    name = "pml",
    description = "PML (Prompt Markup Language) CLI tool",
    subcommands = {ValidateCommand.class, ConvertCommand.class},
    mixinStandardHelpOptions = true
)
public class PmlCli implements Runnable {

    @CommandLine.Spec
    CommandLine.Model.@Nullable CommandSpec spec;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new PmlCli()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        if (spec != null) {
            spec.commandLine().usage(System.out);
        }
    }
}
