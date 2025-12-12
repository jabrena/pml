package info.jab.pml.cli;

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
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "validate", description = "Validates a PML file against the XSD schema")
public class ValidateCommand implements Callable<Integer> {

    @Option(names = "--file", required = true, description = "Path to the PML file to validate")
    private String filePath;

    @Override
    public Integer call() throws Exception {
        try {
            Path pmlFile = Paths.get(filePath);
            if (!Files.exists(pmlFile)) {
                System.err.println("Error: File not found: " + filePath);
                return 1;
            }

            // Load XSD schemas from resources
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            // Load pml.xsd from resources (copied from schema module)
            InputStream pmlXsdStream = getClass().getClassLoader()
                .getResourceAsStream("pml.xsd");

            // Try loading from docs/schemas/0.3.0/ as fallback
            if (pmlXsdStream == null) {
                Path xsdPath = Paths.get("docs/schemas/0.3.0/pml.xsd");
                if (Files.exists(xsdPath)) {
                    Schema schema = schemaFactory.newSchema(xsdPath.toFile());
                    Validator validator = schema.newValidator();
                    validator.validate(new StreamSource(pmlFile.toFile()));
                    System.out.println("Validation successful: " + filePath);
                    return 0;
                } else {
                    System.err.println("Error: Could not find pml.xsd schema file");
                    return 1;
                }
            }

            // Load pml-workflow.xsd if available
            InputStream workflowXsdStream = getClass().getClassLoader()
                .getResourceAsStream("pml-workflow.xsd");
            if (workflowXsdStream == null) {
                Path workflowXsdPath = Paths.get("docs/schemas/0.3.0/pml-workflow.xsd");
                if (Files.exists(workflowXsdPath)) {
                    workflowXsdStream = Files.newInputStream(workflowXsdPath);
                }
            }

            StreamSource[] schemaSources;
            if (workflowXsdStream != null) {
                schemaSources = new StreamSource[]{
                    new StreamSource(pmlXsdStream, "pml.xsd"),
                    new StreamSource(workflowXsdStream, "pml-workflow.xsd")
                };
            } else {
                schemaSources = new StreamSource[]{
                    new StreamSource(pmlXsdStream, "pml.xsd")
                };
            }

            Schema schema = schemaFactory.newSchema(schemaSources);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(pmlFile.toFile()));

            System.out.println("Validation successful: " + filePath);
            return 0;
        } catch (Exception e) {
            System.err.println("Validation failed: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
