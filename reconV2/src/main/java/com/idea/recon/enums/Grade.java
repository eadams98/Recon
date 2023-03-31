package com.idea.recon.enums;

public enum Grade {

	A1 ("A+"),
	A2 ("A"),
	A3 ("A-"),
	B1 ("B+"),
	B2 ("B"),
	B3 ("B-"),
	C1 ("C+"),
	C2 ("C"),
	C3 ("C-"),
	D1 ("D+"),
	D2 ("D"),
	D3 ("D-"),
	F1 ("F+"),
	F2 ("F"),
	F3 ("F-");
	
	private final String name;
	
	private Grade(String s) {
		name = s;
	}
	
	public boolean equalsName(String otherName) {
		return name.equals(otherName);
	}
	
	public String toString() {
		return name;
	}
	
}
