import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, Symbol> table;

    public SymbolTable() {
        table = new HashMap<>();
    }

    public void addSymbol(String name, Symbol symbol) {
        table.put(name, symbol);
    }

    public String getType(String name) {
        return table.get(name).getType();
    }

    public boolean contains(String name) {
        return table.containsKey(name);
    }

    public boolean checkType(String name, String type) {
        return table.get(name).getType().equals(type);
    }

}

class Symbol {
    private String name, type;

    public Symbol(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }


}