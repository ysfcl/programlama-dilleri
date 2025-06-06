package proje;

import java.util.*;

 class SimpleTokenizer {
	static final Set<String> ANAHTAR_KELIMELER = Set.of("if", "else if", "else", "while", "for", "printf", "scanf", "return");
    static final Set<String> OPERATORLER = Set.of("+", "-", "*", "/", "=", "<", ">", "==", "!=", "<=", ">=", "&");
    static final Set<String> SEMBOLLER = Set.of("(", ")", "{", "}", "[", "]", ",", ";");
    static final Set<String> VERI_TIPI = Set.of("int", "float", "double", "char", "boolean", "void", "String");
    static final Set<String> YORUM_SATIRI = Set.of("/*", "*/", "//");

    public static List<Token> tokenize(String kod) {
        List<Token> tokenList = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(kod, " +-*/=<>{}[](),;\\\"&\n\t", true);

        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();

            if (token.equals("\"")) {
                StringBuilder str = new StringBuilder("\"");
                while (st.hasMoreTokens()) {

                	String next = st.nextToken();
                    str.append(next);
                    if (next.equals("\"")) 
                    break;
                }
                token = str.toString();
            }

            if (token.isEmpty()) continue;
            tokenList.add(new Token(token, detectTokenType(token)));
        }

        return tokenList;
    }

    static String detectTokenType(String token) {
    	
    	if (token.matches("\".*\"")) {
		    return "STRING";
		}
			
		else if(VERI_TIPI.contains(token)) {
			return "VERI_TIPI";
		}	
		
		else if(ANAHTAR_KELIMELER.contains(token)) {
			return "ANAHTAR KELIME";
		}
		
		else if(token.matches("[a-zA-Z_][a-zA-Z_0-9]*")) {
			return "DEGISKEN";
		}
		
		else if(OPERATORLER.contains(token)) {
			return "OPERATOR";
		}
		
		else if(token.matches("[0-9]+")) {
			return "SAYI";
		}
		
		else if(SEMBOLLER.contains(token)) {
			return "SEMBOL";
		}
		
		else if(YORUM_SATIRI.contains(token)) {
			return "YORUM_SATIRI";
		}
		
		else if(token.matches("\".*\"")) {
		    return "STRING";
		}
		
		else {
			return "BILINMIYOR";
		}
    }
}

