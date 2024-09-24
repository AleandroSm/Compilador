import java.util.HashMap;

import java.util.Stack;

public class SymbolTable {
    private Stack<HashMap<String, String>> scopes;

    public SymbolTable() {
        scopes = new Stack<>();
        enterScope(); // Start with a global scope
    }

    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    public void exitScope() {
        if (!scopes.isEmpty()) {
            scopes.pop();
        }
    }

    public void addSymbol(String name, String type) {
        if (!scopes.isEmpty()) {
            scopes.peek().put(name, type);
        }
    }

    public String getType(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name)) {
                return scopes.get(i).get(name);
            }
        }
        return "";
    }

    public boolean contains(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkType(String name, String type) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name)) {
                return scopes.get(i).get(name).equals(type);
            }
        }
        return false;
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