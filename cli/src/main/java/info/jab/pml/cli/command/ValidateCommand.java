package info.jab.pml.cli.command;

import info.jab.pml.cli.xml.PmlUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
    name = "validate",
    description = "Validates a PML file against the XSD schema",
    mixinStandardHelpOptions = true,
    usageHelpAutoWidth = true)
public class ValidateCommand implements Callable<Integer> {

    private final PmlUtils pmlUtils;

    @Parameters(index = "0", description = "Path to the PML file to validate")
    @SuppressWarnings("NullAway.Init")
    private String filePath;

    /**
     * Default constructor for normal usage.
     */
    public ValidateCommand() {
        this(new PmlUtils());
    }

    /**
     * Constructor for testing purposes, allowing injection of a PmlUtils.
     *
     * @param pmlUtils the utils instance to use
     */
    public ValidateCommand(PmlUtils pmlUtils) {
        this.pmlUtils = pmlUtils;
    }

    @Override
    public Integer call() {
        Path pmlFile = Paths.get(filePath);
        if (!Files.exists(pmlFile)) {
            System.err.println("Error: File not found: " + filePath);
            return 1;
        }

        if (pmlUtils.validate(pmlFile)) {
            return 0;
        } else {
            return 1;
        }
    }
}
