package com.example.mobilesafe.bean;

public class BlackNumberInfo {
	private String number;
	private String mode;
	
	public BlackNumberInfo(){
		super();
	}
	
	public BlackNumberInfo(String number, String mode) {
		super();
		this.number = number;
		this.mode = mode;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	
	
}
