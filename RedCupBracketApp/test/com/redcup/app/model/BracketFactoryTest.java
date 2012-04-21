package com.redcup.app.model;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

public class BracketFactoryTest extends TestCase {
	
	@Test
	public void testBracketCreation() {
		List<Participant> participants = new ArrayList<Participant>(4);
		participants.add(new Participant("Jim"));
		participants.add(new Participant("Matt"));
		participants.add(new Participant("Ant"));
		participants.add(new Participant("Phil"));
		participants.add(new Participant("Lamp"));

//		Bracket head = BracketFactory.createBracketStructure(participants);
//		assertNotNull(head);
		System.out.println(participants);
	}

}
