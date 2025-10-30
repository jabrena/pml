package info.jab.pml.model;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * This class tests the validation of XML workflow files against the local PML workflow schema.
 *
 */
class SchemaPMLWorkflowValidationTest {

    @ParameterizedTest(name = "[{index}] Validating workflow {0}")
    @MethodSource("getWorkflowTestResourcePaths")
    void workflowXmlFilesAreValidAgainstWorkflowSchema(String resourcePath) throws Exception {
        try (InputStream xml = getClass().getResourceAsStream(resourcePath)) {
            // Given
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory
                    .newSchema(new StreamSource(getClass().getResourceAsStream("/pml-workflow.xsd")));
            Validator validator = schema.newValidator();
            Source source = new StreamSource(xml);

            // When
            // Then
            assertThatCode(() -> validator.validate(source))
                    .as("Workflow file under test: %s", resourcePath)
                    .doesNotThrowAnyException();
        }
    }

    private static String[] getWorkflowTestResourcePaths() {
        return new String[] {
            "/pml-workflow/hello-world/workflow-hello-world.xml",
            "/pml-workflow/cis194/workflow-cis194.xml",
            "/pml-workflow/pi/workflow-pi.xml"
        };
    }
}
