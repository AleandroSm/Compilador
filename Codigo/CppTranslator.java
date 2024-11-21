
public class CppTranslator {
    private StringBuilder code;
    private int indentLevel;

    public CppTranslator() {
        this.code = new StringBuilder();
        this.indentLevel = 0;
    }

    // Añade indentación según el nivel actual
    private void indent() {
        code.append("    ".repeat(indentLevel));
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
        indent();
        String cppType = translateType(type);
        code.append(String.format("%s %s;\n", cppType, name));
    }

    public void translateVariableAssignment(String name, String valor){
        indent();
        code.append(String.format("%s = %s;\n", name,valor));
    }

    private void translateExpression(String line) {
        indent();
        // Reemplaza operadores de tu lenguaje por operadores C++
        String cppLine = line
                .replace("mas", "+")
                .replace("menos", "-")
                .replace("mult", "*")
                .replace("div", "/")
                .replace("res", "%");
        
        code.append(cppLine + ";\n");
    }

   
    public void translateIf(String condition) {
        indent();
        // Traduce los operadores en la condición
        //String cppCondition = translateCondition(condition);
        code.append(String.format("if (%s) {\n", condition));
        indentLevel++;
    }

    public void translateElse(){
        code.append("else {\n");
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
        indent();
        code.append(String.format("cout << %s << endl;\n", content));
    }

    public void translateRead(String variable) {
        indent();
        code.append(String.format("cin >> %s;\n", variable));
    }

    public void translateWhile(String condition) {
        indent();
        code.append(String.format("while (%s) {\n", condition));
        indentLevel++;
    }

    public void translateFor(String init, String condition, String increment) {
        indent();
       // String cppInit = translateType(init)
        //String passed = increment.replace("inc ", "i++");
        code.append(String.format("for (%s %s %s) {\n",
                init, condition, increment));
        indentLevel++;
    }

    // Genera el encabezado del archivo C++
    public void generateHeader() {
        code.append("#include <iostream>\n");
        code.append("using namespace std;\n\n");
        code.append("int main() {\n");
    }

    public void generateKeyClose() {
        code.append("}\n");
    }

    // Obtiene el código C++ generado
    public String getCode() {
        code.append("return 0;\n");
        code.append("}\n");
        return code.toString();
    }
}