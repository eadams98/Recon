package com.idea.enums;

public enum Grade {
	A_PLUS("A+"), A("A"), A_MINUS("A-"),
	B_PLUS("B+"), B("B"), B_MINUS("B-"),
	C_PLUS("C+"), C("C"), C_MINUS("C-"),
	D_PLUS("D+"), D("D"), D_MINUS("D-"),
	F_PLUS("F+"), F("F"), F_MINUS("F-");
	
	public final String label;

    private Grade(String label) {
        this.label = label;
    }
}
