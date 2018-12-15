package org.zero.kafkastreamsappbuilder.codegen;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.springframework.core.io.ClassPathResource;
import org.zero.kafkastreamsappbuilder.codegen.models.NodeModel;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaSourceCodeGenerator {


    private static final String STR_NL = "\n";
    private static final String STR_TAB = "\t";
    private static VelocityEngine engine = getVelocityEngine();

    public static String getSourceCodeFromNodeMap(Map<String, NodeModel> nodes) throws IOException {
        List<NodeModel> roots = new ArrayList<>();

        for (Map.Entry<String, NodeModel> entry : nodes.entrySet()) {
            NodeModel value = entry.getValue();
            if (value.getParent() == null) {
                roots.add(value);
            }
        }
        String generatedCode = visitNodes(roots);
        return generateCodeFromTemplate(
                new String(
                        Files.readAllBytes(
                                new ClassPathResource("velocity/main.vm")
                                        .getFile()
                                        .toPath()
                        )
                ),
                new HashMap<String, Object>() {{
                    put("generatedCode", generatedCode);
                }}
        );
    }

    private static String visitNodes(List<NodeModel> roots) {
        StringBuilder code = new StringBuilder();
        for (NodeModel root : roots) {
            StringBuilder ret = new StringBuilder(generateCodeForNode(root));
            for (NodeModel child : root.getChildren()) {
                ret.append(generateCodeForNode(child));
                ret.append(visitNodes(child.getChildren()));
            }
            code.append(STR_NL).append(ret);
        }
        return code.toString();
    }

    private static String generateCodeForNode(NodeModel node) {
        return STR_TAB + STR_TAB + generateCodeFromTemplate(
                node.getOperator().getTemplate(),
                new HashMap<String, Object>() {{
                    put("model", node);
                }}
        ) + STR_NL;
    }

    private static String generateCodeFromTemplate(String templateStr, Map<String, Object> contextObjects) {

        StringResourceRepository repo = (StringResourceRepository) engine.getApplicationAttribute(StringResourceLoader.REPOSITORY_NAME_DEFAULT);
        repo.putStringResource("template", templateStr);

        // Set parameters for my template.
        VelocityContext context = new VelocityContext();
        contextObjects.forEach(context::put);
        // Get and merge the template with my parameters.
        Template template = engine.getTemplate("template");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        return writer.toString();
    }

    private static VelocityEngine getVelocityEngine() {
        VelocityEngine engine = new VelocityEngine();
        engine.setProperty(Velocity.RESOURCE_LOADER, "string");
        engine.addProperty("string.resource.loader.class", StringResourceLoader.class.getName());
        engine.addProperty("string.resource.loader.repository.static", "false");
        engine.init();
        return engine;
    }

}
