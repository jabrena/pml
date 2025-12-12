package info.jab.pml.cli.command;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.jspecify.annotations.Nullable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "validate",
    description = "Validates a PML file against the XSD schema",
    mixinStandardHelpOptions = true,
    usageHelpAutoWidth = true)
public class ValidateCommand implements Callable<Integer> {

    private static final String PML_XSD_SCHEMA = "pml.xsd";

    @Option(names = "--file", required = true, description = "Path to the PML file to validate")
    private @Nullable String filePath;

    @Override
    public Integer call() {
        if (filePath == null) {
            System.err.println("Error: File path is required");
            return 1;
        }
        Path pmlFile = Paths.get(filePath);
        if (!Files.exists(pmlFile)) {
            System.err.println("Error: File not found: " + filePath);
            return 1;
        }

        if (validatePmlFile(pmlFile)) {
            return 0;
        } else {
            return 1;
        }
    }

    private boolean validatePmlFile(Path pmlFile) {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            // Load pml.xsd from resources (copied from schema module)
            InputStream pmlXsdStream = getClass().getClassLoader()
                .getResourceAsStream(PML_XSD_SCHEMA);

            if (pmlXsdStream == null) {
                System.err.println("Error: Could not find " + PML_XSD_SCHEMA + " schema file in resources");
                return false;
            }

            Schema schema = schemaFactory.newSchema(new StreamSource(pmlXsdStream, PML_XSD_SCHEMA));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(pmlFile.toFile()));
            return true;
        } catch (Exception e) {
            System.err.println("Validation failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
