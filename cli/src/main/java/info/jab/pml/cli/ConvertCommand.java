package info.jab.pml.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Command(name = "convert", description = "Converts a PML file to Markdown using XSLT")
public class ConvertCommand implements Callable<Integer> {

    @Option(names = "--file", required = true, description = "Path to the PML file to convert")
    private String filePath;

    @Override
    public Integer call() throws Exception {
        try {
            Path pmlFile = Paths.get(filePath);
            if (!Files.exists(pmlFile)) {
                System.err.println("Error: File not found: " + filePath);
                return 1;
            }

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

            // Transform PML to Markdown
            StreamSource xmlSource = new StreamSource(pmlFile.toFile());
            
            // Output to stdout
            StreamResult result = new StreamResult(System.out);
            transformer.transform(xmlSource, result);
            
            return 0;
        } catch (Exception e) {
            System.err.println("Conversion failed: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
