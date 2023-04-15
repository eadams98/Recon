package com.idea.recon.enums;

import java.util.HashMap;
import java.util.Map;

public enum Grade {
	A_PLUS("A+"), A("A"), A_MINUS("A-"),
	B_PLUS("B+"), B("B"), B_MINUS("B-"),
	C_PLUS("C+"), C("C"), C_MINUS("C-"),
	D_PLUS("D+"), D("D"), D_MINUS("D-"),
	F_PLUS("F+"), F("F"), F_MINUS("F-");
	
	public final String label;
	private static Map<String, Grade> gradeMapping = new HashMap<>();
	private static Map<Grade, String> gradeToString = new HashMap<>();
	
	static {
		gradeMapping.put("A+", Grade.A_PLUS);
		gradeMapping.put("A", Grade.A);
		gradeMapping.put("A-", Grade.A_MINUS);
		
		gradeMapping.put("B+", Grade.B_PLUS);
		gradeMapping.put("B", Grade.B);
		gradeMapping.put("B-", Grade.B_MINUS);
		
		gradeMapping.put("C+", Grade.C_PLUS);
		gradeMapping.put("C", Grade.C);
		gradeMapping.put("C-", Grade.C_MINUS);
		
		gradeMapping.put("D+", Grade.D_PLUS);
		gradeMapping.put("D", Grade.D);
		gradeMapping.put("D-", Grade.D_MINUS);
		
		gradeMapping.put("F+", Grade.F_PLUS);
		gradeMapping.put("F", Grade.F);
		gradeMapping.put("F-", Grade.F_MINUS);
		////////////////////
		gradeToString.put(Grade.A_PLUS, "A+");
		gradeToString.put(Grade.A, "A");
		gradeToString.put(Grade.A_MINUS, "A-");
		
		gradeToString.put(Grade.B_PLUS, "B+");
		gradeToString.put(Grade.B, "B");
		gradeToString.put(Grade.B_MINUS, "B-");
		
		gradeToString.put(Grade.C_PLUS, "C+");
		gradeToString.put(Grade.C, "C");
		gradeToString.put(Grade.C_MINUS, "C-");
		
		gradeToString.put(Grade.D_PLUS, "D+");
		gradeToString.put(Grade.D, "D");
		gradeToString.put(Grade.D_MINUS, "D-");
		
		gradeToString.put(Grade.F_PLUS, "F+");
		gradeToString.put(Grade.F, "F");
		gradeToString.put(Grade.F_MINUS, "F-");
		
    }

    private Grade(String label) {
        this.label = label;
    }
    
    public static Grade fromString(String str) {
        if (gradeMapping.containsKey(str))
        	return gradeMapping.get(str);
        throw new IllegalArgumentException("Invalid grade string: " + str);
    }
    
    public static String toString(Grade gradeEnum) {
    	if (gradeToString.containsKey(gradeEnum))
    		return gradeToString.get(gradeEnum);
    	throw new IllegalArgumentException("invalide grade enum: " + gradeEnum);
    	
    }
}
