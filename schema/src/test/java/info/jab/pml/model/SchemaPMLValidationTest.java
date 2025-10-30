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
 * This class tests the validation of XML files against the local PML schema.
 *
 */
class SchemaPMLValidationTest {

    @ParameterizedTest(name = "[{index}] Validating {0}")
    @MethodSource("getTestResourcePaths")
    void xmlFilesAreValidAgainstLocalSchema(String resourcePath) throws Exception {
        try (InputStream xml = getClass().getResourceAsStream(resourcePath)) {
            // Given
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new StreamSource(getClass().getResourceAsStream("/pml.xsd")));
            Validator validator = schema.newValidator();
            Source source = new StreamSource(xml);

            // When
            // Then
            assertThatCode(() -> validator.validate(source))
                    .as("File under test: %s", resourcePath)
                    .doesNotThrowAnyException();
        }
    }

    private static String[] getTestResourcePaths() {
        return new String[] {
            "/pml/pml-hello-world.xml",
            "/pml/pml-hello-world-java.xml",
            "/pml/pml-tdd-example.xml",
            "/pml/pml-java25-installation.xml",
            "/pml/pml-java25-installation-v2.xml",
            "/pml/113-java-maven-documentation.xml"
        };
    }
}
