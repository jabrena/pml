package info.jab.pml.cli;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.jspecify.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "convert", description = "Converts a PML file to Markdown using XSLT and outputs the result to stdout")
public class ConvertCommand implements Callable<Integer> {

    @Option(names = "--file", required = true, description = "Path to the PML file to convert")
    private @Nullable String filePath;

    @Option(names = "--template", arity = "2", description = "Template replacement: FIELD VALUE (e.g., --template goal \"New goal\" --template role \"assistant\")")
    private @Nullable List<String> templatePairs = new ArrayList<>();

    @Override
    public Integer call() throws Exception {
        try {
            if (filePath == null) {
                System.err.println("Error: File path is required");
                return 1;
            }
            Path pmlFile = Paths.get(filePath);
            if (!Files.exists(pmlFile)) {
                System.err.println("Error: File not found: " + filePath);
                return 1;
            }

            // Parse template pairs into a map
            Map<String, String> templateMap = parseTemplatePairs();

            // Read and process PML file
            String pmlContent = Files.readString(pmlFile, StandardCharsets.UTF_8);
            String processedPml = applyTemplates(pmlContent, templateMap);

            // Load XSLT from resources
            InputStream xsltStream = getClass().getClassLoader()
                .getResourceAsStream("xslt/pml-to-md.xsl");

            if (xsltStream == null) {
                System.err.println("Error: Could not find pml-to-md.xsl transformation file");
                return 1;
            }

            // Create transformer
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltStream));

            // Transform PML to Markdown and output to stdout (not a file)
            StreamSource xmlSource = new StreamSource(
                new ByteArrayInputStream(processedPml.getBytes(StandardCharsets.UTF_8)));
            StreamResult result = new StreamResult(System.out);
            transformer.transform(xmlSource, result);

            return 0;
        } catch (Exception e) {
            System.err.println("Conversion failed: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    private Map<String, String> parseTemplatePairs() {
        Map<String, String> templateMap = new HashMap<>();
        if (templatePairs == null || templatePairs.isEmpty()) {
            return templateMap;
        }

        // Parse pairs: [field1, value1, field2, value2, ...]
        for (int i = 0; i < templatePairs.size() - 1; i += 2) {
            String field = templatePairs.get(i);
            String value = templatePairs.get(i + 1);
            templateMap.put(field, value);
        }

        return templateMap;
    }

    private String applyTemplates(String pmlContent, Map<String, String> templateMap) throws Exception {
        if (templateMap.isEmpty()) {
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
            replaceFieldValue(root, fieldName, fieldValue);
        }

        // Convert back to string
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(doc), new StreamResult(outputStream));
        return outputStream.toString(StandardCharsets.UTF_8);
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
                    // Replace the content
                    NodeList contentNodes = childElement.getChildNodes();
                    // Remove existing text content
                    for (int j = contentNodes.getLength() - 1; j >= 0; j--) {
                        Node contentNode = contentNodes.item(j);
                        if (contentNode.getNodeType() == Node.TEXT_NODE) {
                            childElement.removeChild(contentNode);
                        }
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
}
