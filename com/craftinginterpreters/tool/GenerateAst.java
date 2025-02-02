package com.craftinginterpreters.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * Instead of tediously handwriting each class definition, 
 * field declaration, constructor, and initializer, 
 * we’ll hack together a script that does it for us. 
 * It has a description of each tree type—its name and fields—and it 
 * prints out the Java code needed to define a class with that name and state.
 */
public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if(args.length != 1) {
            System.err.println("Usage: generate_ast <output_directory>");
            System.exit(64);
        }
        String outputDir = args[0];

        defineAst(outputDir, "Expr", Arrays.asList(
            "Binary   : Expr left, Token operator, Expr right",
            "Grouping : Expr expression",
            "Literal  : Object value",
            "Unary    : Token operator, Expr right"
        ));
    };

    /**
     * 
     * @param outputDir
     * @param baseName
     * @param types
     * @throws IOException
     */
    private static void defineAst(
        String outputDir, String baseName, List<String> types
    ) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package com.craftinginterpreters.lox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        // defining the base class inside each subclass
        for(String type: types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();

            defineType(writer, baseName, className, fields);
        }


        writer.println("}");
        writer.close();
    }

    /**
     * It declares each field in the class body. 
     * It defines a constructor for the class with parameters for each field and initializes them in the body
     * @param writer
     * @param baseName
     * @param className
     * @param fieldList
     */
    private static void defineType(
        PrintWriter writer, String baseName,
        String className, String fieldList
    ) {
        writer.println(" static class " + className + " extends " + baseName + " {");

      
        // Constructor
        writer.println("    " + className + "(" + fieldList + ") {");

        // Store parameters in fields.
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
        String name = field.split(" ")[1];
        writer.println("      this." + name + " = " + name + ";");
        }

        writer.println("    }");

        // Fields.
        writer.println();
        for (String field : fields) {
        writer.println("    final " + field + ";");
        }

        writer.println("  }");
    }
    
}
