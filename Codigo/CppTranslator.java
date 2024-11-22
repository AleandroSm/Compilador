import java.util.ArrayList;
import java.util.List;

public class CppTranslator {
    private StringBuilder mainCode;      
    private StringBuilder functionCode;  
    private int indentLevel;
    private int functionLevel;
    private List<String> parameters;
    private StringBuilder currentFunctionCall;

    public CppTranslator() {
        this.mainCode = new StringBuilder();
        this.functionCode = new StringBuilder();
        this.indentLevel = 0;
        this.parameters = new ArrayList<>();
        this.functionLevel = 0;
        this.currentFunctionCall = new StringBuilder();
    }

    // Añade indentación según el nivel actual
    private void indent(StringBuilder sb) {
        sb.append("    ".repeat(indentLevel));
    }

    // Traduce los tipos de datos
    public String translateType(String type) {
        switch(type.toLowerCase()) {
            case "ent": return "int";
            case "flot": return "float";
            case "cad": return "string";
            case "bool": return "bool";
            default: return type;
        }
    }

    // Traduce operadores
    public String translateOperator(String op) {
        switch(op.toLowerCase()) {
            case "mas": return "+";
            case "menos": return "-";
            case "mult": return "*";
            case "div": return "/";
            case "res": return "%";
            case "and": return "&&";
            case "or": return "||";
            case "not": return "!";
            default: return op;
        }
    }

   
    public void translateVariableDeclaration(String type, String name) {
        String cppType = translateType(type);
        if (isInFunction() == true) {
            indent(functionCode);
            functionCode.append(String.format("%s %s;\n", cppType, name));
        } else {
            indent(mainCode);
            mainCode.append(String.format("%s %s;\n", cppType, name));
        }
    }

    public void translateVariableAssignment(String name, String value) {
        if (isInFunction() == true) {
            indent(functionCode);
            functionCode.append(String.format("%s = %s;\n", name, value));
            
        } else {
            indent(mainCode);
            mainCode.append(String.format("%s = %s;\n", name, value));
        }
    }

   
    public void translateIf(String condition) {
        if (isInFunction()) {
            indent(functionCode);
            functionCode.append(String.format("if (%s) {\n", condition));
        } else {
            indent(mainCode);
            mainCode.append(String.format("if (%s) {\n", condition));
        }
        indentLevel++;
    }

    public void translateElse() {
        if(indentLevel > 0) indentLevel--;
        if (isInFunction()) {
            indent(functionCode);
            functionCode.append("} else {\n");
        } else {
            indent(mainCode);
            mainCode.append("} else {\n");
        }
        indentLevel++;
    }

    private String translateCondition(String condition) {
        return condition
                //.replace("igual", "==")
                //.replace("diferente", "!=")
                //.replace("mayor", ">")
                //.replace("menor", "<")
                //.replace("mayorigual", ">=")
                //.replace("menorigual", "<=")
                .replace("AND", "&&")
                .replace("OR", "||")
                .replace("NOT", "!");
    }


    public void translateWrite(String content) {
        if (isInFunction()) {
            indent(functionCode);
            functionCode.append("cout << ").append(content).append(" << endl;\n");
        } else {
            indent(mainCode);
            mainCode.append("cout << ").append(content).append(" << endl;\n");
        }
    }

    public void translateRead(String variable) {
        if (isInFunction()) {
            indent(functionCode);
            functionCode.append(String.format("cin >> %s;\n", variable));
        } else {
            indent(mainCode);
            mainCode.append(String.format("cin >> %s;\n", variable));
        }
    }

    public void translateAssignment(String variable, String expression) {
        if (isInFunction()) {
            indent(functionCode);
            functionCode.append(variable).append(" = ").append(expression).append(";\n");
        } else {
            indent(mainCode);
            mainCode.append(variable).append(" = ").append(expression).append(";\n");
        }
    }

    public void translateWhile(String condition) {
        if (isInFunction()) {
            indent(functionCode);
            functionCode.append("while (").append(condition).append(") {\n");
        } else {
            indent(mainCode);
            mainCode.append("while (").append(condition).append(") {\n");
        }
        indentLevel++;
    }


    public void translateFor(String init, String condition, String increment) {
        if (isInFunction()) {
            indent(functionCode);
            functionCode.append(String.format("for (%s; %s; %s) {\n", 
                init, condition, increment));
        } else {
            indent(mainCode);
            mainCode.append(String.format("for (%s; %s %s) {\n", 
                init, condition, increment));
        }
        indentLevel++;
    }


    public void enterFunction() {
        functionLevel++;
    }

    public void exitFunction() {
        if (functionLevel > 0) {
            functionLevel--;
        }
    }

    private boolean isInFunction() {
        return functionLevel > 0;
    }

    public void translateFunctionStart(String returnType, String name) {
        enterFunction();
        functionCode.append("\n");
        indent(functionCode);
        functionCode.append(returnType).append(" ").append(name).append("(");
        parameters.clear();
    }

    public void translateParameter(String type, String name) {
        if (!parameters.isEmpty()) {
            functionCode.append(", ");
        }
        functionCode.append(type).append(" ").append(name);
        parameters.add(name);
    }

    public void translateReturn(String value) {
        indent(functionCode);
        functionCode.append("return ").append(value).append(";\n");
    }

    public void closeFunctionBlock() {
        if(indentLevel > 0) indentLevel--;
        indent(functionCode);
        functionCode.append("}\n\n");
        exitFunction();
        //isInFunction = false;
    }

     // Para iniciar la llamada a función
     public void translateFunctionCallStart(String functionName) {
        currentFunctionCall = new StringBuilder();
        if (isInFunction()) {
            indent(functionCode);
            functionCode.append(functionName).append("(");
        } else {
            indent(mainCode);
            mainCode.append(functionName).append("(");
        }
    }

    // Para añadir argumentos a la llamada
    public void translateFunctionArgument(String arg) {
        if (currentFunctionCall.length() > 0) {
            currentFunctionCall.append(", ");
        }
        currentFunctionCall.append(arg);
    }

    // Para cerrar la llamada a función
    public void translateFunctionCallEnd() {
        if (isInFunction()) {
            functionCode.append(currentFunctionCall.toString()).append(");\n");
        } else {
            mainCode.append(currentFunctionCall.toString()).append(");\n");
        }
        currentFunctionCall = new StringBuilder();
    }

    // Para asignar el resultado de una función
    public void translateFunctionCallWithAssignment(String variable, String functionName) {
        if (isInFunction()) {
            indent(functionCode);
            functionCode.append(variable).append(" = ").append(functionName).append("(");
        } else {
            indent(mainCode);
            mainCode.append(variable).append(" = ").append(functionName).append("(");
        }
    }

    public void translateMainCode(String code) {
        if (!isInFunction()) {
            indent(mainCode);
            mainCode.append(code).append("\n");
        } else {
            indent(functionCode);
            functionCode.append(code).append("\n");
        }
    }

    
    public void generateKeyClose() {
        if (isInFunction()) {
            functionCode.append(") {\n");
            if(indentLevel > 0) indentLevel--;
            indent(functionCode);
            //functionCode.append("}\n");
        } else {
            indentLevel--;
            indent(mainCode);
            mainCode.append("}\n");
        }
    }

    public String getCode() {
        StringBuilder fullCode = new StringBuilder();
        
        // Añadir includes
        fullCode.append("#include <iostream>\n");
        fullCode.append("using namespace std;\n\n");
        
        // Añadir funciones
        fullCode.append(functionCode.toString());
        
        // Añadir main
        fullCode.append("\nint main() {\n");
        indentLevel++;
        fullCode.append(mainCode.toString());
        fullCode.append("    return 0;\n");
        fullCode.append("}\n");
        
        return fullCode.toString();
    }
}