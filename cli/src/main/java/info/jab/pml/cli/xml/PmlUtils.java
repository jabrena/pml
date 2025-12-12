package info.jab.pml.cli.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utility class for PML file operations including validation and conversion.
 */
public class PmlUtils {

    private static final String PML_XSD_SCHEMA = "pml.xsd";
    private static final String XSLT_TRANSFORMATION = "xslt/pml-to-md.xsl";

    /**
     * Validates a PML file against the XSD schema.
     *
     * @param pmlFile the path to the PML file to validate
     * @return true if validation succeeds, false otherwise
     */
    public boolean validate(Path pmlFile) {
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

    /**
     * Validates a PML XML string against the XSD schema.
     *
     * @param pmlContent the PML XML content as a string
     * @return true if validation succeeds, false otherwise
     */
    public boolean validate(String pmlContent) {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            // Load pml.xsd from resources
            InputStream pmlXsdStream = getClass().getClassLoader()
                .getResourceAsStream(PML_XSD_SCHEMA);

            if (pmlXsdStream == null) {
                System.err.println("Error: Could not find " + PML_XSD_SCHEMA + " schema file in resources");
                return false;
            }

            Schema schema = schemaFactory.newSchema(new StreamSource(pmlXsdStream, PML_XSD_SCHEMA));
            Validator validator = schema.newValidator();
            StreamSource xmlSource = new StreamSource(
                new ByteArrayInputStream(pmlContent.getBytes(StandardCharsets.UTF_8)));
            validator.validate(xmlSource);
            return true;
        } catch (Exception e) {
            System.err.println("Validation failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Applies template replacements to PML content.
     *
     * @param pmlContent the original PML XML content
     * @param templateMap map of field names to replacement values
     * @return the processed PML content with replacements applied
     * @throws Exception if XML parsing or processing fails
     */
    public String applyTemplates(String pmlContent, Map<String, String> templateMap) throws Exception {
        if (templateMap == null || templateMap.isEmpty()) {
            return pmlContent;
        }

        // Parse XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(pmlContent.getBytes(StandardCharsets.UTF_8)));

        // Apply template replacements
        Element root = doc.getDocumentElement();
        for (Map.Entry<String, String> entry : templateMap.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();

            // First try to replace as an element name
            replaceFieldValue(root, fieldName, fieldValue);

            // Also replace text content patterns (e.g., "MESSAGE" in text nodes)
            replaceTextContent(root, fieldName, fieldValue);
        }

        // Convert back to string
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(doc), new StreamResult(outputStream));
        return outputStream.toString(StandardCharsets.UTF_8);
    }

    /**
     * Converts PML XML content to Markdown using XSLT transformation.
     *
     * @param pmlContent the PML XML content to convert
     * @param output the PrintStream to write the Markdown output to
     * @return true if conversion succeeds, false otherwise
     */
    public boolean convertToMarkdown(String pmlContent, PrintStream output) {
        try {
            // Load XSLT from resources
            InputStream xsltStream = getClass().getClassLoader()
                .getResourceAsStream(XSLT_TRANSFORMATION);

            if (xsltStream == null) {
                System.err.println("Error: Could not find " + XSLT_TRANSFORMATION + " transformation file");
                return false;
            }

            // Create transformer
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltStream));

            // Transform PML to Markdown and output to the provided stream
            StreamSource xmlSource = new StreamSource(
                new ByteArrayInputStream(pmlContent.getBytes(StandardCharsets.UTF_8)));
            StreamResult result = new StreamResult(output);
            transformer.transform(xmlSource, result);

            return true;
        } catch (Exception e) {
            System.err.println("Conversion failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void replaceFieldValue(Element element, String fieldName, String newValue) {
        // If field is not found, skip it (no error thrown)
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                String tagName = childElement.getTagName();

                // Check if this is the field we're looking for
                if (fieldName.equals(tagName)) {
                    // Replace all content (both text nodes and child elements)
                    NodeList contentNodes = childElement.getChildNodes();
                    // Remove all child nodes (both elements and text)
                    for (int j = contentNodes.getLength() - 1; j >= 0; j--) {
                        Node contentNode = contentNodes.item(j);
                        childElement.removeChild(contentNode);
                    }
                    // Add new text content
                    childElement.appendChild(element.getOwnerDocument().createTextNode(newValue));
                } else {
                    // Recursively search in nested elements (field not found yet, continue searching)
                    replaceFieldValue(childElement, fieldName, newValue);
                }
            }
        }
        // If field is never found, it's silently skipped (no exception thrown)
    }

    private void replaceTextContent(Node node, String pattern, String replacement) {
        // Skip empty patterns to avoid unintended replacements
        if (pattern == null || pattern.isEmpty()) {
            return;
        }

        if (node.getNodeType() == Node.TEXT_NODE) {
            String text = node.getTextContent();
            if (text != null && text.contains(pattern)) {
                node.setTextContent(text.replace(pattern, replacement));
            }
        } else if (node.getNodeType() == Node.ELEMENT_NODE) {
            // Recursively process child nodes
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                replaceTextContent(children.item(i), pattern, replacement);
            }
        }
    }
}
