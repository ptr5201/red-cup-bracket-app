package com.redcup.app.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.redcup.app.model.Bracket;
import com.redcup.app.model.Participant;
import com.redcup.app.model.SingleEliminationBracketFactory;

public class BracketFactoryTest {
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPreconditions() {
		// this method follows Android conventions by testing
		// that the application under test is initialized correctly
		
		// no preconditions thus far
		assertTrue(true);
	}
	
	@Test
	public void testBracketCreation() {
		List<Participant> participants = new ArrayList<Participant>(4);
		participants.add(new Participant("Jim"));
		participants.add(new Participant("Matt"));
		participants.add(new Participant("Ant"));
		participants.add(new Participant("Phil"));
		participants.add(new Participant("Lamp"));

		Bracket head = SingleEliminationBracketFactory.
				createBracketStructure(participants);
		assertNotNull(head);
	}

}
