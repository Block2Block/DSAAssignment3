package dsa_assignment3;

import static dsa_assignment3.NodeType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class PLTreeNodeTest
{
	private static final Logger logger        = Logger.getLogger(PLTreeNodeTest.class);

	@Rule
	public Timeout              globalTimeout = new Timeout(2000, TimeUnit.MILLISECONDS);

	@Test
	public void testCheckStudentIdentification()
	{
		assertNotEquals("Please update the studentID field in PLTreeNode.java with your student id number", //
				"MY STUDENT ID", PLTreeNode.getStudentID());
		assertNotEquals("Please update the studentName field in PLTreeNode.java with your name", //
				"MY NAME", PLTreeNode.getStudentName());
	}


	/**
	 * This is not a true JUnit Test method: it merely gives examples of calling
	 * the PLTree methods and obtaining output.
	 * 
	 * As part of this exercise, you should devise your OWN tests to check that
	 * your code is performing as the JavaDoc and the assignment hand-out says it
	 * should. You are strongly advised to use the "Coverage" tool to check for
	 * parts of your code that are not executed as part of your tests. The coverage
	 * tool can be invoked from the "Run" menu item in Eclipse or from the button
	 * beside the "Run" and "Debug" buttons in Eclipse
	 */
	@Test
	public void testPLTree()
	{
		NodeType[] typeList = { NodeType.R, P, NodeType.OR, NodeType.TRUE, NodeType.Q, NodeType.NOT, NodeType.AND,
				NodeType.IMPLIES };
		logger.debug("typeList: " + Arrays.toString(typeList));

		PLTreeNodeInterface pltree = PLTreeNode.reversePolishBuilder(typeList);

		assertNotNull("PLTree construction failed when using: " + typeList, pltree);
		logger.debug("Constructed: " + pltree.toStringPrefix());
		logger.debug("Constructed: " + pltree.toStringInfix());
		NodeType[] typeListReturned = pltree.getReversePolish();
		logger.debug("typeListReturned: " + Arrays.toString(typeListReturned));

		Map<NodeType, Boolean> bindings = new HashMap<>();
		bindings.put(P, true);
		bindings.put(NodeType.R, false);

		pltree.applyVarBindings(bindings);
		logger.debug(String.format("Applied bindings : %s to get: %s", bindings, pltree.toStringPrefix()));
		logger.debug(String.format("Applied bindings : %s to get: %s", bindings, pltree.toStringInfix()));
		typeListReturned = pltree.getReversePolish();
		logger.debug("typeListReturned: " + Arrays.toString(typeListReturned));

		//		Boolean b = pltree.evaluateConstantSubtrees();
		//		logger.debug(String.format("Evaluate constant subtrees to get: %s", pltree.toStringPrefix()));
		//		logger.debug(String.format("Evaluate constant subtrees to get: %s", pltree.toStringInfix()));

		pltree.replaceImplies();
		logger.debug(String.format("Replace Implies: %s", pltree.toStringPrefix()));
		logger.debug(String.format("Replace Implies: %s", pltree.toStringInfix()));
		typeListReturned = pltree.getReversePolish();
		logger.debug("typeListReturned: " + Arrays.toString(typeListReturned));

		pltree.pushNotDown();
		logger.debug(String.format("pushNotDown: %s", pltree.toStringPrefix()));
		logger.debug(String.format("pushNotDown: %s", pltree.toStringInfix()));
		typeListReturned = pltree.getReversePolish();
		logger.debug("typeListReturned: " + Arrays.toString(typeListReturned));

		pltree.pushOrBelowAnd();
		logger.debug(String.format("pushOrBelowAnd: %s", pltree.toStringPrefix()));
		logger.debug(String.format("pushOrBelowAnd: %s", pltree.toStringInfix()));
		typeListReturned = pltree.getReversePolish();
		logger.debug("typeListReturned: " + Arrays.toString(typeListReturned));

		pltree.makeAndOrRightDeep();
		logger.debug(String.format("makeAndOrRightDeep: %s", pltree.toStringPrefix()));
		logger.debug(String.format("makeAndOrRightDeep: %s", pltree.toStringInfix()));
		typeListReturned = pltree.getReversePolish();
		logger.debug("typeListReturned: " + Arrays.toString(typeListReturned));

		pltree.evaluateConstantSubtrees();
		logger.debug(String.format("Evaluate constant subtrees to get: %s", pltree.toStringPrefix()));
		logger.debug(String.format("Evaluate constant subtrees to get: %s", pltree.toStringInfix()));
		typeListReturned = pltree.getReversePolish();
		logger.debug("typeListReturned: " + Arrays.toString(typeListReturned));

		//==========================

		typeList = new NodeType[] { NodeType.R, P, NodeType.IMPLIES, NodeType.S, NodeType.IMPLIES, NodeType.NOT, NodeType.Q,
				NodeType.IMPLIES };
		logger.debug("typeList: " + Arrays.toString(typeList));

		pltree = PLTreeNode.reversePolishBuilder(typeList);
		logger.debug("Constructed: " + pltree.toStringPrefix());
		logger.debug("Constructed: " + pltree.toStringInfix());
		typeListReturned = pltree.getReversePolish();
		logger.debug("typeListReturned: " + Arrays.toString(typeListReturned));

		pltree.reduceToCNF();
		logger.debug(String.format("ReduceToCNF to get: %s", pltree.toStringPrefix()));
		logger.debug(String.format("ReduceToCNF to get: %s", pltree.toStringInfix()));
		typeListReturned = pltree.getReversePolish();
		logger.debug("typeListReturned: " + Arrays.toString(typeListReturned));

		pltree.evaluateConstantSubtrees();
		logger.debug(String.format("Evaluate constant subtrees to get: %s", pltree.toStringPrefix()));
		logger.debug(String.format("Evaluate constant subtrees to get: %s", pltree.toStringInfix()));
		typeListReturned = pltree.getReversePolish();
		logger.debug("typeListReturned: " + Arrays.toString(typeListReturned));

		//==========================

		typeList = new NodeType[] { NodeType.A, NodeType.B, NodeType.AND, NodeType.C, NodeType.OR, NodeType.D, NodeType.OR, NodeType.E,
				NodeType.OR, NodeType.F, NodeType.OR, NodeType.G, NodeType.OR, NodeType.H, NodeType.OR };
		logger.debug("typeList: " + Arrays.toString(typeList));
		pltree = PLTreeNode.reversePolishBuilder(typeList);
		logger.debug("Constructed: " + pltree.toStringPrefix());
		logger.debug("Constructed: " + pltree.toStringInfix());
		typeListReturned = pltree.getReversePolish();
		logger.debug("typeListReturned: " + Arrays.toString(typeListReturned));

		pltree.pushOrBelowAnd();
		logger.debug(String.format("pushOrBelowAnd: %s", pltree.toStringPrefix()));
		logger.debug(String.format("pushOrBelowAnd: %s", pltree.toStringInfix()));
		typeListReturned = pltree.getReversePolish();
		logger.debug("typeListReturned: " + Arrays.toString(typeListReturned));
		
		
		//===========================

		logger.debug("Extra tests of pushOrBelowAnd()");
		typeList = new NodeType[] { NodeType.A, NodeType.B, NodeType.AND, NodeType.C, NodeType.AND, NodeType.D, NodeType.OR, NodeType.E, NodeType.OR};
		logger.debug("typeList: " + Arrays.toString(typeList));
		pltree = PLTreeNode.reversePolishBuilder(typeList);
		logger.debug("Constructed: " + pltree.toStringPrefix());
		logger.debug("Constructed: " + pltree.toStringInfix());
		typeListReturned = pltree.getReversePolish();
		logger.debug("typeListReturned: " + Arrays.toString(typeListReturned));

		pltree.pushOrBelowAnd();
		logger.debug(String.format("pushOrBelowAnd: %s", pltree.toStringPrefix()));
		logger.debug(String.format("pushOrBelowAnd: %s", pltree.toStringInfix()));
		typeListReturned = pltree.getReversePolish();
		logger.debug("typeListReturned: " + Arrays.toString(typeListReturned));

	}
	/*
	 To assist you in writing your tests, the log output from the solution to
	 the exercise for the above code is included below. You may find it useful
	 to copy and paste some of the strings below into some of your tests.

typeList: [R, P, OR, TRUE, Q, NOT, AND, IMPLIES]
Constructed: implies(or(R,P),and(true,not(Q)))
Constructed: ((R∨P)→(⊤∧¬Q))
typeListReturned: [R, P, OR, TRUE, Q, NOT, AND, IMPLIES]
Applied bindings : {P=true, R=false} to get: implies(or(false,true),and(true,not(Q)))
Applied bindings : {P=true, R=false} to get: ((⊥∨⊤)→(⊤∧¬Q))
typeListReturned: [FALSE, TRUE, OR, TRUE, Q, NOT, AND, IMPLIES]
Replace Implies: or(not(or(false,true)),and(true,not(Q)))
Replace Implies: (¬(⊥∨⊤)∨(⊤∧¬Q))
typeListReturned: [FALSE, TRUE, OR, NOT, TRUE, Q, NOT, AND, OR]
pushNotDown: or(and(not(false),not(true)),and(true,not(Q)))
pushNotDown: ((¬⊥∧¬⊤)∨(⊤∧¬Q))
typeListReturned: [FALSE, NOT, TRUE, NOT, AND, TRUE, Q, NOT, AND, OR]
pushOrBelowAnd: and(and(or(not(false),true),or(not(false),not(Q))),and(or(not(true),true),or(not(true),not(Q))))
pushOrBelowAnd: (((¬⊥∨⊤)∧(¬⊥∨¬Q))∧((¬⊤∨⊤)∧(¬⊤∨¬Q)))
typeListReturned: [FALSE, NOT, TRUE, OR, FALSE, NOT, Q, NOT, OR, AND, TRUE, NOT, TRUE, OR, TRUE, NOT, Q, NOT, OR, AND, AND]
makeAndOrRightDeep: and(or(not(false),true),and(or(not(false),not(Q)),and(or(not(true),true),or(not(true),not(Q)))))
makeAndOrRightDeep: ((¬⊥∨⊤)∧((¬⊥∨¬Q)∧((¬⊤∨⊤)∧(¬⊤∨¬Q))))
typeListReturned: [FALSE, NOT, TRUE, OR, FALSE, NOT, Q, NOT, OR, TRUE, NOT, TRUE, OR, TRUE, NOT, Q, NOT, OR, AND, AND, AND]
Evaluate constant subtrees to get: not(Q)
Evaluate constant subtrees to get: ¬Q
typeListReturned: [Q, NOT]
typeList: [R, P, IMPLIES, S, IMPLIES, NOT, Q, IMPLIES]
Constructed: implies(not(implies(implies(R,P),S)),Q)
Constructed: (¬((R→P)→S)→Q)
typeListReturned: [R, P, IMPLIES, S, IMPLIES, NOT, Q, IMPLIES]
ReduceToCNF to get: and(or(R,or(S,Q)),or(not(P),or(S,Q)))
ReduceToCNF to get: ((R∨(S∨Q))∧(¬P∨(S∨Q)))
typeListReturned: [R, S, Q, OR, OR, P, NOT, S, Q, OR, OR, AND]
Evaluate constant subtrees to get: and(or(R,or(S,Q)),or(not(P),or(S,Q)))
Evaluate constant subtrees to get: ((R∨(S∨Q))∧(¬P∨(S∨Q)))
typeListReturned: [R, S, Q, OR, OR, P, NOT, S, Q, OR, OR, AND]
typeList: [A, B, AND, C, OR, D, OR, E, OR, F, OR, G, OR, H, OR]
Constructed: or(or(or(or(or(or(and(A,B),C),D),E),F),G),H)
Constructed: (((((((A∧B)∨C)∨D)∨E)∨F)∨G)∨H)
typeListReturned: [A, B, AND, C, OR, D, OR, E, OR, F, OR, G, OR, H, OR]
pushOrBelowAnd: and(or(or(or(or(or(or(A,C),D),E),F),G),H),or(or(or(or(or(or(B,C),D),E),F),G),H))
pushOrBelowAnd: (((((((A∨C)∨D)∨E)∨F)∨G)∨H)∧((((((B∨C)∨D)∨E)∨F)∨G)∨H))
typeListReturned: [A, C, OR, D, OR, E, OR, F, OR, G, OR, H, OR, B, C, OR, D, OR, E, OR, F, OR, G, OR, H, OR, AND]
Extra tests of pushOrBelowAnd()
typeList: [A, B, AND, C, AND, D, OR, E, OR]
Constructed: or(or(and(and(A,B),C),D),E)
Constructed: ((((A∧B)∧C)∨D)∨E)
typeListReturned: [A, B, AND, C, AND, D, OR, E, OR]
pushOrBelowAnd: and(and(or(or(A,D),E),or(or(B,D),E)),or(or(C,D),E))
pushOrBelowAnd: ((((A∨D)∨E)∧((B∨D)∨E))∧((C∨D)∨E))
typeListReturned: [A, D, OR, E, OR, B, D, OR, E, OR, AND, C, D, OR, E, OR, AND]


	 */
	
	// The following are simple examples using the first results of the sample output above.

	@Test
	public void testPLTreeConstruction()
	{
		NodeType[] typeList = { NodeType.R, P, NodeType.OR, NodeType.TRUE, NodeType.Q, NodeType.NOT, NodeType.AND,	NodeType.IMPLIES };

		PLTreeNodeInterface pltree = PLTreeNode.reversePolishBuilder(typeList);

		assertNotNull("PLTree construction failed when using: " + typeList, pltree);
		
		NodeType[] newTypeList = pltree.getReversePolish();
		
		// The hamcrest matcher that follows generates better error messages automatically when comparing arrays than assertArrayEquals().
		// Try uncommenting the following line to see what the error message looks like if the arrays don't match
		//		newTypeList[1] = NodeType.A;
		assertThat(newTypeList, equalTo(typeList));
	}	

	@Test
	public void testPLTreeToPrefixString()
	{
		NodeType[] typeList = { NodeType.R, P, NodeType.OR, NodeType.TRUE, NodeType.Q, NodeType.NOT, NodeType.AND,	NodeType.IMPLIES };

		PLTreeNodeInterface pltree = PLTreeNode.reversePolishBuilder(typeList);

		// Always check that the tree generated is not null before any other tests:
		// if it is, it means that you have an error in your input typeList
		
		assertNotNull("PLTree construction failed when using: " + typeList, pltree);
		
		// Again, the Hamcrest matcher generates suitable error messages automatically:
		assertThat(pltree.toStringPrefix(), equalTo("implies(or(R,P),and(true,not(Q)))"));		
	}

	@Test
	public void testPLTreeToInfixString() {
		NodeType[] typeList = { NodeType.R, P, NodeType.OR, NodeType.TRUE, NodeType.Q, NodeType.NOT, NodeType.AND,	NodeType.IMPLIES };

		PLTreeNodeInterface pltree = PLTreeNode.reversePolishBuilder(typeList);

		// Always check that the tree generated is not null before any other tests:
		// if it is, it means that you have an error in your input typeList

		assertNotNull("PLTree construction failed when using: " + typeList, pltree);

		// Again, the Hamcrest matcher generates suitable error messages automatically:
		assertThat(pltree.toStringInfix(), equalTo("((R∨P)→(⊤∧¬Q))"));
	}

	@Test
	public void testPLTreeApplyBindings() {
		NodeType[] typeList = { NodeType.R, P, NodeType.OR, NodeType.TRUE, NodeType.Q, NodeType.NOT, NodeType.AND,	NodeType.IMPLIES };

		PLTreeNodeInterface pltree = PLTreeNode.reversePolishBuilder(typeList);

		// Always check that the tree generated is not null before any other tests:
		// if it is, it means that you have an error in your input typeList

		assertNotNull("PLTree construction failed when using: " + typeList, pltree);

		//Testing applying bindings
		Map<NodeType, Boolean> bindings = new HashMap<>();
		bindings.put(P, true);
		bindings.put(NodeType.R, false);

		pltree.applyVarBindings(bindings);

		NodeType[] expected = { NodeType.FALSE, NodeType.TRUE, NodeType.OR, NodeType.TRUE, NodeType.Q, NodeType.NOT, NodeType.AND,	NodeType.IMPLIES };
		assertThat(pltree.getReversePolish(), equalTo(expected));


		//Testing that bindings that are not variables are ignored.
		pltree = PLTreeNode.reversePolishBuilder(typeList);
		assertNotNull("PLTree construction failed when using: " + typeList, pltree);

		bindings = new HashMap<>();
		bindings.put(P, true);
		bindings.put(NodeType.R, false);
		bindings.put(NodeType.IMPLIES, false);

		pltree.applyVarBindings(bindings);
		assertThat(pltree.getReversePolish(), equalTo(expected));
	}

	@Test
	public void testPLTreeReplaceImplies() {
		NodeType[] typeList = { NodeType.R, P, NodeType.OR, NodeType.TRUE, NodeType.Q, NodeType.NOT, NodeType.AND,	NodeType.IMPLIES };

		PLTreeNodeInterface pltree = PLTreeNode.reversePolishBuilder(typeList);

		// Always check that the tree generated is not null before any other tests:
		// if it is, it means that you have an error in your input typeList

		assertNotNull("PLTree construction failed when using: " + typeList, pltree);
		pltree.replaceImplies();

		NodeType[] expected = { NodeType.R, P, NodeType.OR, NodeType.NOT, NodeType.TRUE, NodeType.Q, NodeType.NOT, NodeType.AND, NodeType.OR };

		assertThat(pltree.getReversePolish(), equalTo(expected));

		//Making sure that doing it again doesn't effect anything, as there should be no more implies.
		pltree.replaceImplies();
		assertThat(pltree.getReversePolish(), equalTo(expected));
	}

	@Test
	public void testPLTreePushNotDownSingle() {
		NodeType[] typeListNotNot = {NodeType.R, NodeType.NOT, NodeType.NOT};
		NodeType[] typeListNotOr = {NodeType.R, P, NodeType.OR, NodeType.NOT};
		NodeType[] typeListNotAnd = {NodeType.R, P, NodeType.AND, NodeType.NOT};

		NodeType[] expectedNotNot = {NodeType.R};
		NodeType[] expectedNotOr = {NodeType.R, NodeType.NOT, P, NodeType.NOT, NodeType.AND};
		NodeType[] expectedNotAnd = {NodeType.R, NodeType.NOT, P, NodeType.NOT, NodeType.OR};

		PLTreeNodeInterface pltreeNotNot = PLTreeNode.reversePolishBuilder(typeListNotNot);

		// Always check that the tree generated is not null before any other tests:
		// if it is, it means that you have an error in your input typeList

		assertNotNull("PLTree construction failed when using: " + typeListNotNot, pltreeNotNot);

		PLTreeNodeInterface pltreeNotOr = PLTreeNode.reversePolishBuilder(typeListNotOr);

		// Always check that the tree generated is not null before any other tests:
		// if it is, it means that you have an error in your input typeList

		assertNotNull("PLTree construction failed when using: " + typeListNotOr, pltreeNotOr);

		PLTreeNodeInterface pltreeNotAnd = PLTreeNode.reversePolishBuilder(typeListNotAnd);

		// Always check that the tree generated is not null before any other tests:
		// if it is, it means that you have an error in your input typeList

		assertNotNull("PLTree construction failed when using: " + typeListNotAnd, pltreeNotAnd);

		pltreeNotNot.pushNotDown();
		pltreeNotOr.pushNotDown();
		pltreeNotAnd.pushNotDown();


		assertThat(pltreeNotNot.getReversePolish(), equalTo(expectedNotNot));
		assertThat(pltreeNotOr.getReversePolish(), equalTo(expectedNotOr));
		assertThat(pltreeNotAnd.getReversePolish(), equalTo(expectedNotAnd));

		//Checking that it does effect anything when nots cannot be pushed down

		pltreeNotNot.pushNotDown();
		pltreeNotOr.pushNotDown();
		pltreeNotAnd.pushNotDown();


		assertThat(pltreeNotNot.getReversePolish(), equalTo(expectedNotNot));
		assertThat(pltreeNotOr.getReversePolish(), equalTo(expectedNotOr));
		assertThat(pltreeNotAnd.getReversePolish(), equalTo(expectedNotAnd));
	}

	@Test
	public void testPLTreePushNotDownMultiple() {
		NodeType[] input = {P, NOT, NOT, Q, NOT, AND, R, NOT, OR, NOT};

		PLTreeNodeInterface pltree = PLTreeNode.reversePolishBuilder(input);

		// Always check that the tree generated is not null before any other tests:
		// if it is, it means that you have an error in your input typeList

		assertNotNull("PLTree construction failed when using: " + input, pltree);
		NodeType[] expected = {P, NOT, Q, OR, R, AND};
		pltree.pushNotDown();

		assertThat(pltree.getReversePolish(), equalTo(expected));
	}

	@Test
	public void testPLTreePushNotDownIssues() {
		NodeType[] tripleNot = {Q, NOT, NOT, NOT};
		PLTreeNodeInterface pltree = PLTreeNode.reversePolishBuilder(tripleNot);

		// Always check that the tree generated is not null before any other tests:
		// if it is, it means that you have an error in your input typeList

		assertNotNull("PLTree construction failed when using: " + tripleNot, pltree);

		pltree.pushNotDown();
		assertThat(pltree.getReversePolish(), equalTo(new NodeType[]{Q, NOT}));
	}

	@Test
	public void testPLTreePushOrBelowAnd() {
		NodeType[] input = {P, Q, AND, R, OR};
		NodeType[] input1 = {P, Q, R, AND, OR};
		PLTreeNodeInterface pltree = PLTreeNode.reversePolishBuilder(input);
		PLTreeNodeInterface pltree1 = PLTreeNode.reversePolishBuilder(input1);
		// Always check that the tree generated is not null before any other tests:
		// if it is, it means that you have an error in your input typeList

		assertNotNull("PLTree construction failed when using: " + input, pltree);
		assertNotNull("PLTree construction failed when using: " + input1, pltree1);

		pltree.pushOrBelowAnd();
		pltree1.pushOrBelowAnd();

		NodeType[] expected = {P, R, OR, Q, R, OR, AND};
		NodeType[] expected1 = {P, Q, OR, P, R, OR, AND};
		assertThat(pltree.getReversePolish(), equalTo(expected));
		assertThat(pltree1.getReversePolish(), equalTo(expected1));
	}

	@Test
	public void testPLTreePushOrBelowAndPreAndPost() {
		NodeType[] input = {W, X, AND, Y, OR, Z, OR};
		NodeType[] input1 = {W, X, AND, Y, AND, Z, OR};
		PLTreeNodeInterface pltree = PLTreeNode.reversePolishBuilder(input);
		PLTreeNodeInterface pltree1 = PLTreeNode.reversePolishBuilder(input1);
		// Always check that the tree generated is not null before any other tests:
		// if it is, it means that you have an error in your input typeList

		assertNotNull("PLTree construction failed when using: " + input, pltree);
		assertNotNull("PLTree construction failed when using: " + input1, pltree1);

		pltree.pushOrBelowAnd();
		pltree1.pushOrBelowAnd();

		NodeType[] expected = {W, Y, OR, Z, OR, X, Y, OR, Z, OR, AND};
		NodeType[] expected1 = {W, Z, OR, X, Z, OR, AND, Y, Z, OR, AND};
		assertThat(pltree.getReversePolish(), equalTo(expected));
		assertThat(pltree1.getReversePolish(), equalTo(expected1));
	}

	@Test
	public void testPLTreeMakeAndOrRightDeep() {
		NodeType[] input = {X, Y, OR, Z, OR};
		NodeType[] input1 = {X, Y, AND, Z, AND};
		NodeType[] input2 = {W, X, OR, Y, Z, OR, OR};
		NodeType[] input3 = {W, X, OR, Y, OR, Z, OR};

		PLTreeNodeInterface pltree = PLTreeNode.reversePolishBuilder(input);
		PLTreeNodeInterface pltree1 = PLTreeNode.reversePolishBuilder(input1);
		PLTreeNodeInterface pltree2 = PLTreeNode.reversePolishBuilder(input2);
		PLTreeNodeInterface pltree3 = PLTreeNode.reversePolishBuilder(input3);
		// Always check that the tree generated is not null before any other tests:
		// if it is, it means that you have an error in your input typeList

		assertNotNull("PLTree construction failed when using: " + input, pltree);
		assertNotNull("PLTree construction failed when using: " + input1, pltree1);
		assertNotNull("PLTree construction failed when using: " + input2, pltree2);
		assertNotNull("PLTree construction failed when using: " + input3, pltree3);

		pltree.makeAndOrRightDeep();
		pltree1.makeAndOrRightDeep();
		pltree2.makeAndOrRightDeep();
		pltree3.makeAndOrRightDeep();

		NodeType[] expected = {X, Y, Z, OR, OR};
		NodeType[] expected1 = {X, Y, Z, AND, AND};
		NodeType[] expected2 = {W, X, Y, Z, OR, OR, OR};
		assertThat(pltree.getReversePolish(), equalTo(expected));
		assertThat(pltree1.getReversePolish(), equalTo(expected1));
		assertThat(pltree2.getReversePolish(), equalTo(expected2));
		assertThat(pltree3.getReversePolish(), equalTo(expected2));
	}

	@Test
	public void testPLTreeEvaluateConstantSubtrees() {
		NodeType[] input = {P, Q, AND, R, OR};

		PLTreeNodeInterface pltree = PLTreeNode.reversePolishBuilder(input);
		// Always check that the tree generated is not null before any other tests:
		// if it is, it means that you have an error in your input typeList

		assertNotNull("PLTree construction failed when using: " + input, pltree);

		assertThat(pltree.evaluateConstantSubtrees(), equalTo(null));

		Map<NodeType, Boolean> bindings = new HashMap<>();
		bindings.put(P, true);
		bindings.put(NodeType.R, false);

		pltree.applyVarBindings(bindings);

		assertThat(pltree.evaluateConstantSubtrees(), equalTo(null));

		bindings = new HashMap<>();
		bindings.put(Q, true);
		pltree.applyVarBindings(bindings);

		assertThat(pltree.evaluateConstantSubtrees(), equalTo(true));

		pltree = PLTreeNode.reversePolishBuilder(input);
		// Always check that the tree generated is not null before any other tests:
		// if it is, it means that you have an error in your input typeList

		assertNotNull("PLTree construction failed when using: " + input, pltree);

		bindings = new HashMap<>();
		bindings.put(P, false);
		bindings.put(NodeType.R, false);
		bindings.put(Q, true);
		pltree.applyVarBindings(bindings);

		assertThat(pltree.evaluateConstantSubtrees(), equalTo(false));

	}

}
