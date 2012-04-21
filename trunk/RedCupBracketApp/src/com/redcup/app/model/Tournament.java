package com.redcup.app.model;

import java.util.List;

public class Tournament {
	private BracketStrategy strategy;
	private String name;
	private List<Participant> participants;
	
	public BracketStrategy getStrategy() {
		return strategy;
	}
	public void setStrategy(BracketStrategy strategy) {
		this.strategy = strategy;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Participant> getParticipants() {
		return participants;
	}
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

}
