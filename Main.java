public class Main {

     public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input; // Return as is for null or empty strings
        }

        // Trim leading spaces but preserve them in the final result
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return input; // String contains only spaces
        }

        // Capitalize first character and append the rest unchanged
        return trimmed.substring(0, 1).toUpperCase() + trimmed.substring(1);
    }

    public static void main (String[] args) {
        String name, type, Name;
		while (true) {
            name = Lire.S();
            type = Lire.S();
            Name = capitalizeFirstLetter(name);
            System.out.println("public void set"+Name+"("+type+" "+name+") {\r\n" + //
                                "   this."+name+" = "+name+";\r\n" + //
                                "}\r\n\n" + //
                                "public "+type+" get"+Name+"() {\r\n" + //
                                "   return "+name+";\r\n" + //
                                "}");
        }
	}

}