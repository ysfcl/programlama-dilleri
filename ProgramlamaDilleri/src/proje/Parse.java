package proje;

import java.util.List;



public class Parse {
	private List<Token> token;
	private int pozisyon=0;
	
	Parse(){}
	
	Parse(List<Token> token){
		this.token=token;
	}
	
	
     boolean parse() {
    	 
    	 while (pozisyon<token.size()) {
    	        if (!parseStatement()) {
    	            return false;
    	        }
    	    }
    	    return true;
     }
	
     
     
	boolean parseDeclaretion() {
		
		int baslangic_pozisyonu=pozisyon;
	
		if (match("VERI_TIPI") 
				&&match("DEGISKEN") 
				&&match("OPERATOR", "=") 
				&&parseExpression() 
				&&match("SEMBOL", ";")) {
		        
			return true;
		    
		}
	
		pozisyon=baslangic_pozisyonu;
		
		return false;		
	}

	
	
	private boolean parseStatement() {
	    return parseFunctionDefinition() 
	    	||parseDeclaretion()
	        ||parseAssignment()
	        ||parseIfStatement()
	        ||parseLoopStatement()
	        ||parseReturnStatement()
	        ||parseExpression()
	        ||parseFunctionCall();
	    	
	}
	
	
	
	private boolean match(String expectedType) {
	    if (pozisyon<token.size()&&token.get(pozisyon).tip.equals(expectedType)) {
	        pozisyon++;
	        return true;
	    }
	    return false;
	}

	
	
	private boolean match(String expectedType, String expectedValue) {
	    if (pozisyon<token.size()) {
	        
	    	Token t=token.get(pozisyon);
	        
	        if (t.tip.equals(expectedType)&&t.icerik.equals(expectedValue)) {
	        
	        	pozisyon++;
	            
	        	return true;
	        }
	    }
	    return false;
	}
	
	
	
	private boolean parseStatementList() {
		 while (pozisyon<token.size()) {
	         if (token.get(pozisyon).icerik.equals("}")) {
	             return true;
	         }

	         if (!parseStatement()) {
	             return false;
	         }
	     }
	     return true;
	}
	

	
	private boolean parseAssignment() {
		int baslangic_pozisyonu=pozisyon;
		
		if (match("DEGISKEN")&&match("OPERATOR", "=")) {
	        if (parseExpression()&&match("SEMBOL", ";")) {
	            return true;
	        }
	    }
		
		pozisyon=baslangic_pozisyonu;
		return false;
		
	}
	
	
	
	private boolean parseCondition() {
		int baslangic_pozisyonu=pozisyon;

	    if (match("DEGISKEN") 
	    		&&match("OPERATOR") 
	    		&&(match("SAYI") || match("DEGISKEN"))) {
	        
	    	return true;
	    }

	    pozisyon=baslangic_pozisyonu;
	    return false;
	
	}
	
	
	
	private boolean parseIfStatement() {
	    int baslangic_pozisyonu=pozisyon;

	    if (match("ANAHTAR KELIME", "if") 
	    		&&match("SEMBOL", "(") 
	    		&&parseCondition() 
	    		&&match("SEMBOL", ")")
	    		&&match("SEMBOL", "{") 
	    		&&parseStatementList() 
	    		&&match("SEMBOL", "}")) {

	            
	            while (match("ANAHTAR KELIME", "else")) {
	                
	            	if (match("ANAHTAR KELIME", "if")) {
	                    
	            		if (!match("SEMBOL", "(") || !parseCondition() || !match("SEMBOL", ")") 
	            				||!match("SEMBOL", "{") || !parseStatementList() || !match("SEMBOL", "}")) {
	                        
	            				return rollback(baslangic_pozisyonu);
	                    	}
	                } 
	            	
	            	else {
	                    
	            		if (!match("SEMBOL", "{") || !parseStatementList() || !match("SEMBOL", "}")) {
	                        	return rollback(baslangic_pozisyonu);
	                    	}
	                    break; 
	                }
	            }

	            return true;
	        }

	    pozisyon=baslangic_pozisyonu;
	    return false;
	}
	
	
	
	private boolean parseLoopStatement() {
		 int baslangic_pozisyonu=pozisyon;

		    if (match("ANAHTAR KELIME", "while") 
		    		&&match("SEMBOL", "(")
		    		&&parseCondition() 
		    		&&match("SEMBOL", ")")
		    		&&match("SEMBOL", "{")
		    		&&parseStatementList() 
		    		&&match("SEMBOL", "}")) {
		        
		    	return true;
		    }

		    pozisyon=baslangic_pozisyonu;
		    return false;
	}

	
	
	private boolean parseExpression() {
		int baslangic_pozisyonu=pozisyon;
		
		if (!parseTerm()) return rollback(baslangic_pozisyonu);

	    while (match("OPERATOR", "+")||match("OPERATOR", "-")) {
	       
	    	if (!parseTerm()) 
	        	return rollback(baslangic_pozisyonu);
	    }
	    return true;
	}
	
	
	
	private boolean parseTerm() {
	    int baslangic = pozisyon;
	    if (!parseFactor()) return rollback(baslangic);

	    while (match("OPERATOR", "*")||match("OPERATOR", "/")) {
	        
	    	if (!parseFactor()) 
	        	return rollback(baslangic);
	    }
	    return true;
	}
	
	
	
	private boolean parseFactor() {
	    int baslangic = pozisyon;

	    if (match("SAYI") || match("DEGISKEN")) 
	    	return true;

	    if (match("SEMBOL", "(")) {
	        
	    	if (!parseExpression()){
	        	return rollback(baslangic);
	        }
	        
	        if (!match("SEMBOL", ")")) { 
	        	return rollback(baslangic);
	        }
	        
	        return true;
	    }

	    return rollback(baslangic);
	}
	
	
	
	private boolean parseFunctionCall() {
		
		int baslangic_pozisyonu=pozisyon;

	    if (!match("ANAHTAR KELIME") && !match("DEGISKEN")) {
	        pozisyon=baslangic_pozisyonu;
	        return false;
	    }

	    if (!match("SEMBOL", "(")) {
	        pozisyon=baslangic_pozisyonu;
	        return false;
	    }

	    if (!parseArgumentList()) {
	        pozisyon=baslangic_pozisyonu;
	        return false;
	    }

	    if (!match("SEMBOL", ")")) {
	        pozisyon=baslangic_pozisyonu;
	        return false;
	    }

	    if (!match("SEMBOL", ";")) {
	        pozisyon=baslangic_pozisyonu;
	        return false;
	    }

	    return true;
	}
	
	
	
	private boolean parseFunctionDefinition() {
		int baslangic_pozisyonu=pozisyon;

		if (!match("VERI_TIPI")) return rollback(baslangic_pozisyonu);
	    if (!match("DEGISKEN")) return rollback(baslangic_pozisyonu);
	    if (!match("SEMBOL", "(")) return rollback(baslangic_pozisyonu);

	    
	    if (peek("VERI_TIPI")) {
	        if (!parseParameterList()) return rollback(baslangic_pozisyonu);
	    }

	    if (!match("SEMBOL", ")")) return rollback(baslangic_pozisyonu);
	    if (!match("SEMBOL", "{")) return rollback(baslangic_pozisyonu);
	    if (!parseStatementList()) return rollback(baslangic_pozisyonu);
	    if (!match("SEMBOL", "}")) return rollback(baslangic_pozisyonu);

	    return true;
	}
	
	
	
	private boolean rollback(int baslangic) {
	    pozisyon = baslangic;
	    return false;
	}
	
	
	private boolean parseParameterList() {
	    int baslangic_pozisyonu=pozisyon;

	    if (!match("VERI_TIPI") || !match("DEGISKEN")) {
	        pozisyon = baslangic_pozisyonu;
	        return false;
	    }

	    while (match("SEMBOL", ",")) {
	        if (!match("VERI_TIPI") || !match("DEGISKEN")) {
	            pozisyon = baslangic_pozisyonu;
	            return false;
	        }
	    }

	    return true;
	}
	
	
	
	private boolean peek(String expectedType) {
	    return pozisyon < token.size() && token.get(pozisyon).tip.equals(expectedType);
	}
	
	
	
	private boolean parseArgumentList() {
		
		int baslangic_pozisyonu = pozisyon;

	    if (match("SEMBOL", ")")) {
	        pozisyon--; // Dışarıda tekrar kontrol edilecek
	        return true;
	    }

	    if (!parseArgument()) {
	        pozisyon=baslangic_pozisyonu;
	        return false;
	    }

	    while (match("SEMBOL", ",")) {
	        if (!parseArgument()) {
	            pozisyon=baslangic_pozisyonu;
	            return false;
	        }
	    }

	    return true;
	}
	
	
	private boolean parseArgument() {
		return match("STRING") 
		        || match("SAYI") 
		        || match("DEGISKEN") 
		        || (match("OPERATOR", "&") && match("DEGISKEN"));
	}

	
	
	private boolean parseReturnStatement() {
	    int baslangic_pozisyonu = pozisyon;

	    if (match("ANAHTAR KELIME", "return")) {
	       
	        parseExpression(); 
	        
	        if (match("SEMBOL", ";")) {
	            return true;
	        }
	    }

	    pozisyon = baslangic_pozisyonu;
	    return false;
	}
	
}
