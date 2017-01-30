package com.github.javaparser.generator;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.generator.utils.SourceRoot;
import com.github.javaparser.metamodel.BaseNodeMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;

import java.io.IOException;

import static com.github.javaparser.generator.utils.GeneratorUtils.f;

/**
 * Makes it easier to generate code in the core AST nodes. The generateNode method will get every node type passed to it,
 * ready for modification.
 */
public abstract class NodeGenerator extends Generator {
    protected NodeGenerator(JavaParser javaParser, SourceRoot sourceRoot) {
        super(javaParser, sourceRoot);
    }

    public final void generate() throws IOException {
        for (BaseNodeMetaModel nodeMetaModel : JavaParserMetaModel.getNodeMetaModels()) {
            CompilationUnit nodeCu = sourceRoot.parse(nodeMetaModel.getPackageName(), nodeMetaModel.getTypeName() + ".java", javaParser).orElseThrow(() -> new IOException(f("java file for %s not found", nodeMetaModel.getTypeName())));
            ClassOrInterfaceDeclaration nodeCoid = nodeCu.getClassByName(nodeMetaModel.getTypeName()).orElseThrow(() -> new IOException("Can't find class"));
            generateNode(nodeMetaModel, nodeCu, nodeCoid);
        }
    }

    protected abstract void generateNode(BaseNodeMetaModel nodeMetaModel, CompilationUnit nodeCu, ClassOrInterfaceDeclaration nodeCoid);
}