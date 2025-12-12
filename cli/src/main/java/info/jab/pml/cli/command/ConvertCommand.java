package info.jab.pml.cli.command;

import info.jab.pml.cli.xml.PmlUtils;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.jspecify.annotations.Nullable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "convert",
    description = "Converts a PML file to Markdown using XSLT and outputs the result to stdout",
    mixinStandardHelpOptions = true,
    usageHelpAutoWidth = true)
public class ConvertCommand implements Callable<Integer> {

    private final PmlUtils pmlUtils;

    @Parameters(index = "0", description = "Path to the PML file to convert")
    @SuppressWarnings("NullAway.Init")
    private String filePath;

    @Option(names = "--template", arity = "2..*", description = "Template replacement: FIELD VALUE [FIELD VALUE ...] (e.g., --template goal \"New goal\" role \"assistant\")")
    private @Nullable List<String> templatePairs = new ArrayList<>();

    /**
     * Default constructor for normal usage.
     */
    public ConvertCommand() {
        this(new PmlUtils());
    }

    /**
     * Constructor for testing purposes, allowing injection of a PmlUtils.
     *
     * @param pmlUtils the utils instance to use
     */
    public ConvertCommand(PmlUtils pmlUtils) {
        this.pmlUtils = pmlUtils;
    }

    @Override
    public Integer call() throws Exception {
        try {
            Path pmlFile = Paths.get(filePath);
            if (!Files.exists(pmlFile)) {
                System.err.println("Error: File not found: " + filePath);
                return 1;
            }

            // Read PML file into string
            String pmlContent = Files.readString(pmlFile, StandardCharsets.UTF_8);

            // Parse template pairs into a map
            Map<String, String> templateMap = parseTemplatePairs();
            if (templateMap == null) {
                System.err.println("Error: --template requires an even number of arguments (pairs of FIELD VALUE)");
                return 1;
            }

            // Apply template replacements if templates are provided
            String processedPml = pmlUtils.applyTemplates(pmlContent, templateMap);

            // Validate the processed XML string
            if (!pmlUtils.validate(processedPml)) {
                System.err.println("Error: The processed PML file is not valid");
                return 1;
            }

            // Convert to Markdown
            if (pmlUtils.convertToMarkdown(processedPml, System.out)) {
                return 0;
            } else {
                System.err.println("Error: Conversion failed");
                return 1;
            }
        } catch (Exception e) {
            System.err.println("Conversion failed: " + e.getMessage());
            return 1;
        }
    }

    private @Nullable Map<String, String> parseTemplatePairs() {
        Map<String, String> templateMap = new HashMap<>();
        if (templatePairs == null || templatePairs.isEmpty()) {
            return templateMap;
        }

        // Validate that we have an even number of arguments (pairs)
        if (templatePairs.size() % 2 != 0) {
            return null;
        }

        // Parse pairs: [field1, value1, field2, value2, ...]
        for (int i = 0; i < templatePairs.size(); i += 2) {
            String field = templatePairs.get(i);
            String value = templatePairs.get(i + 1);
            templateMap.put(field, value);
        }

        return templateMap;
    }
}
