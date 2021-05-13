import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PermutationTest {
	PermutationVariation p1;
	PermutationVariation p2;
	public int n1;
	public int n2;
	int cases=0;
	
	void initialize() {
		n1=4;
		n2=6;
		Cases c= new Cases();
		p1= c.switchforTesting(cases, n1);
		p2= c.switchforTesting(cases, n2);
	}


	@Test
	 void testPermutation() {
		initialize();
		assertEquals(n1, p1.original.length);
		assertEquals(n2, p2.original.length);
		assertTrue(checkDuplicateElement(p1.original));
		assertTrue(checkDuplicateElement(p2.original));
		assertNotEquals(null, p1.allDerangements);
		assertNotEquals(null, p2.allDerangements);
	}

	@Test
	void testDerangements() {
		initialize();
		//in case there is something wrong with the constructor
		fixConstructor();
		p1.derangements();
		p2.derangements();
		assertEquals(countDerangements(n1),p1.allDerangements.size());
		assertEquals(countDerangements(n2),p2.allDerangements.size());
		assertTrue(checkFixedPointFreedom(p1));
		assertTrue(checkFixedPointFreedom(p2));
	}
	
	@Test
	void testsameElements() {
		initialize();
		//in case there is something wrong with the constructor
		fixConstructor();
		// TODO
	}
	
	void setCases(int c) {
		this.cases=c;
	}

	public int countDerangements(int length) {
		if (length == 1) { return 0; }
		length = (int) (length * countDerangements(length-1) + Math.pow(-1, length));
		return length;
	}


	/**
	 * Checks the fixed point freedom condition of the permutation arrays in the linked list by comparing the values of both array elements at same position
	 * @param p The permutation variation object
	 * @return false if the condition is failed, true if it passes
	 */
	public boolean checkFixedPointFreedom(PermutationVariation p) {
		for (int[] tempArray : p.allDerangements) {
			for(int i = 0; i < tempArray.length; i++) {
				if (tempArray[i] == p.original[i]) { return false; }
			}
		}
		return true;
	}

	public boolean checkDuplicateElement(int[] tempArray) {
		for (int i = 0; i < tempArray.length-1; i++) {
			for (int j = i+1; j < tempArray.length; j++) {
				if (tempArray[i] == tempArray[j]) { return false; }
			}
		}
		return true;
	}

	public void fixConstructor() {
		//in case there is something wrong with the constructor
		p1.allDerangements=new LinkedList<int[]>();
		for(int i=0;i<n1;i++)
			p1.original[i]=2*i+1;
		
		p2.allDerangements=new LinkedList<int[]>();
		for(int i=0;i<n2;i++)
			p2.original[i]=i+1;
	}
}


