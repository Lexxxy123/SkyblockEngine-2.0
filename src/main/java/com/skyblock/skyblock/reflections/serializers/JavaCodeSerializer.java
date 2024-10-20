/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.skyblock.skyblock.reflections.serializers;

import com.skyblock.skyblock.reflections.Reflections;
import com.skyblock.skyblock.reflections.scanners.TypeElementsScanner;
import com.skyblock.skyblock.reflections.serializers.Serializer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JavaCodeSerializer
implements Serializer {
    private static final String pathSeparator = "_";
    private static final String doubleSeparator = "__";
    private static final String dotSeparator = ".";
    private static final String arrayDescriptor = "$$";
    private static final String tokenSeparator = "_";
    private StringBuilder sb;
    private List<String> prevPaths;
    private int indent;

    @Override
    public Reflections read(InputStream inputStream) {
        throw new UnsupportedOperationException("read is not implemented on JavaCodeSerializer");
    }

    @Override
    public File save(Reflections reflections, String name) {
        String className;
        String packageName;
        if (name.endsWith("/")) {
            name = name.substring(0, name.length() - 1);
        }
        String filename = name.replace('.', '/').concat(".java");
        File file = Serializer.prepareFile(filename);
        int lastDot = name.lastIndexOf(46);
        if (lastDot == -1) {
            packageName = "";
            className = name.substring(name.lastIndexOf(47) + 1);
        } else {
            packageName = name.substring(name.lastIndexOf(47) + 1, lastDot);
            className = name.substring(lastDot + 1);
        }
        try {
            this.sb = new StringBuilder();
            this.sb.append("//generated using Reflections JavaCodeSerializer").append(" [").append(new Date()).append("]").append("\n");
            if (packageName.length() != 0) {
                this.sb.append("package ").append(packageName).append(";\n");
                this.sb.append("\n");
            }
            this.sb.append("public interface ").append(className).append(" {\n\n");
            this.toString(reflections);
            this.sb.append("}\n");
            Files.write(new File(filename).toPath(), this.sb.toString().getBytes(Charset.defaultCharset()), new OpenOption[0]);
        } catch (IOException e2) {
            throw new RuntimeException();
        }
        return file;
    }

    private void toString(Reflections reflections) {
        Map map = (Map)reflections.getStore().get(TypeElementsScanner.class.getSimpleName());
        this.prevPaths = new ArrayList<String>();
        this.indent = 1;
        map.keySet().stream().sorted().forEach(fqn -> {
            List<String> typePaths = Arrays.asList(fqn.split("\\."));
            String className = fqn.substring(fqn.lastIndexOf(46) + 1);
            ArrayList<String> fields = new ArrayList<String>();
            ArrayList<String> methods = new ArrayList<String>();
            ArrayList<String> annotations = new ArrayList<String>();
            ((Set)map.get(fqn)).stream().sorted().forEach(element -> {
                if (element.startsWith("@")) {
                    annotations.add(element.substring(1));
                } else if (element.contains("(")) {
                    if (!element.startsWith("<")) {
                        int i2 = element.indexOf(40);
                        String name = element.substring(0, i2);
                        String params = element.substring(i2 + 1, element.indexOf(")"));
                        String paramsDescriptor = params.length() != 0 ? "_" + params.replace(dotSeparator, "_").replace(", ", doubleSeparator).replace("[]", arrayDescriptor) : "";
                        methods.add(!methods.contains(name) ? name : name + paramsDescriptor);
                    }
                } else if (!element.isEmpty()) {
                    fields.add((String)element);
                }
            });
            int i2 = this.indentOpen(typePaths, this.prevPaths);
            this.addPackages(typePaths, i2);
            this.addClass(typePaths, className);
            this.addFields(typePaths, fields);
            this.addMethods(typePaths, fields, methods);
            this.addAnnotations(typePaths, annotations);
            this.prevPaths = typePaths;
        });
        this.indentClose(this.prevPaths);
    }

    protected int indentOpen(List<String> typePaths, List<String> prevPaths) {
        int i2;
        for (i2 = 0; i2 < Math.min(typePaths.size(), prevPaths.size()) && typePaths.get(i2).equals(prevPaths.get(i2)); ++i2) {
        }
        for (int j2 = prevPaths.size(); j2 > i2; --j2) {
            this.sb.append(this.indent(--this.indent)).append("}\n");
        }
        return i2;
    }

    protected void indentClose(List<String> prevPaths) {
        for (int j2 = prevPaths.size(); j2 >= 1; --j2) {
            this.sb.append(this.indent(j2)).append("}\n");
        }
    }

    protected void addPackages(List<String> typePaths, int i2) {
        for (int j2 = i2; j2 < typePaths.size() - 1; ++j2) {
            this.sb.append(this.indent(this.indent++)).append("interface ").append(this.uniqueName(typePaths.get(j2), typePaths, j2)).append(" {\n");
        }
    }

    protected void addClass(List<String> typePaths, String className) {
        this.sb.append(this.indent(this.indent++)).append("interface ").append(this.uniqueName(className, typePaths, typePaths.size() - 1)).append(" {\n");
    }

    protected void addFields(List<String> typePaths, List<String> fields) {
        if (!fields.isEmpty()) {
            this.sb.append(this.indent(this.indent++)).append("interface fields {\n");
            for (String field : fields) {
                this.sb.append(this.indent(this.indent)).append("interface ").append(this.uniqueName(field, typePaths)).append(" {}\n");
            }
            this.sb.append(this.indent(--this.indent)).append("}\n");
        }
    }

    protected void addMethods(List<String> typePaths, List<String> fields, List<String> methods) {
        if (!methods.isEmpty()) {
            this.sb.append(this.indent(this.indent++)).append("interface methods {\n");
            for (String method : methods) {
                String methodName = this.uniqueName(method, fields);
                this.sb.append(this.indent(this.indent)).append("interface ").append(this.uniqueName(methodName, typePaths)).append(" {}\n");
            }
            this.sb.append(this.indent(--this.indent)).append("}\n");
        }
    }

    protected void addAnnotations(List<String> typePaths, List<String> annotations) {
        if (!annotations.isEmpty()) {
            this.sb.append(this.indent(this.indent++)).append("interface annotations {\n");
            for (String annotation : annotations) {
                this.sb.append(this.indent(this.indent)).append("interface ").append(this.uniqueName(annotation, typePaths)).append(" {}\n");
            }
            this.sb.append(this.indent(--this.indent)).append("}\n");
        }
    }

    private String uniqueName(String candidate, List<String> prev, int offset) {
        String normalized = this.normalize(candidate);
        for (int i2 = 0; i2 < offset; ++i2) {
            if (!normalized.equals(prev.get(i2))) continue;
            return this.uniqueName(normalized + "_", prev, offset);
        }
        return normalized;
    }

    private String normalize(String candidate) {
        return candidate.replace(dotSeparator, "_");
    }

    private String uniqueName(String candidate, List<String> prev) {
        return this.uniqueName(candidate, prev, prev.size());
    }

    private String indent(int times) {
        return IntStream.range(0, times).mapToObj(i2 -> "  ").collect(Collectors.joining());
    }
}

