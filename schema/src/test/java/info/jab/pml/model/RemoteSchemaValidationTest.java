package info.jab.pml.model;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.InputStream;
import java.net.URI;
import java.util.Objects;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RemoteSchemaValidationTest {

    private static final String REMOTE_XSD = "https://jabrena.github.io/pml/schemas/0.1.0-SNAPSHOT/pml.xsd";

    @ParameterizedTest
    @ValueSource(strings = {"/pml-example-remote.xml", "/113-java-maven-documentation.xml"})
    void xmlFilesAreValidAgainstRemoteSchema(String resourcePath) throws Exception {
        try (InputStream xml = getClass().getResourceAsStream(resourcePath)) {
            if (Objects.isNull(xml)) {
                throw new IllegalStateException("Test resource not found: " + resourcePath);
            }

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "all");
            schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "all");

            Schema schema = schemaFactory.newSchema(URI.create(REMOTE_XSD).toURL());
            Validator validator = schema.newValidator();

            Source source = new StreamSource(xml);
            assertThatCode(() -> validator.validate(source)).doesNotThrowAnyException();
        }
    }
}
