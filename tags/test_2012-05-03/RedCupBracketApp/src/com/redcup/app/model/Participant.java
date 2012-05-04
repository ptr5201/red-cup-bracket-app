package com.redcup.app.model;

import java.io.Serializable;

public class Participant implements Serializable{	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int id;
	
	public Participant(String name){
		this.name = name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String toString() {
		return name;
	}
}
