package org.zero.kafkastreamsappbuilder.codegen;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.zero.kafkastreamsappbuilder.codegen.models.NodeModel;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaSourceCodeGenerator {


    private static VelocityEngine engine = getVelocityEngine();

    public static String getSourceCodeFromNodeMap(Map<String, NodeModel> nodes){
        List<NodeModel> roots = new ArrayList<>();

        for (Map.Entry<String, NodeModel> entry : nodes.entrySet()) {
            NodeModel value = entry.getValue();
            if (value.getParent() == null) {
                roots.add(value);
            }
        }
        StringBuilder code = new StringBuilder();
        for (NodeModel root : roots) {
            StringBuilder ret = new StringBuilder(generateCodeForNode(root));
            for(NodeModel child: root.getChildren()){
                ret.append(generateCodeForNode(child));
            }
            code.append(ret);
        }
        return code.toString();
    }


    private static String generateCodeForNode(NodeModel node){
        return velocityWithStringTemplateExample(node);
    }

    private static String velocityWithStringTemplateExample(NodeModel node) {

        // Initialize my template repository. You can replace the "Hello $w" with your String.
        StringResourceRepository repo = (StringResourceRepository) engine.getApplicationAttribute(StringResourceLoader.REPOSITORY_NAME_DEFAULT);
        repo.putStringResource("template", node.getOperator().getTemplate());

        // Set parameters for my template.
        VelocityContext context = new VelocityContext();
        context.put("model",node);
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
