package com.redcup.app;

import java.io.Serializable;

public class Participant implements Serializable{	
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	public Participant(String _name){
		name = _name;
	}
	
	public void setName(String _name){
		name = _name;
	}
	
	public String getName(){
		return name;
	}
}
