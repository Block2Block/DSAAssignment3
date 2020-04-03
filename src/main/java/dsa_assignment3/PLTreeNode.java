package dsa_assignment3;

import java.io.NotActiveException;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * A node in a binary tree representing a Propositional Logic expression.
 * <p>
 * Each node has a type (AND node, OR node, NOT node etc.) and can have zero one
 * or two children as required by the node type (AND has two children, NOT has
 * one, TRUE, FALSE and variables have none
 * </p>
 * <p>
 * This class is mutable, and some of the operations are intended to modify the
 * tree internally. There are a few cases when copies need to be made of whole
 * sub-tree nodes in such a way that these copied trees do not share any nodes
 * with their originals. To do this the class implements a deep copying copy
 * constructor <code>PLTreeNode(PLTreeNode node)</code>
 * </p>
 * 
 */
public final class PLTreeNode implements PLTreeNodeInterface
{
	private static final Logger logger = Logger.getLogger(PLTreeNode.class);

	NodeType                    type;
	PLTreeNode                  child1;
	PLTreeNode                  child2;

	/**
	 * For marking purposes
	 * 
	 * @return Your student id
	 */
	public static String getStudentID()
	{
		//change this return value to return your student id number
		return "2025695";
	}

	/**
	 * For marking purposes
	 * 
	 * @return Your name
	 */
	public static String getStudentName()
	{
		//change this return value to return your name
		return "Ethan Paterson-Barker";
	}

	/**
	 * The default constructor should never be called and has been made private
	 */
	private PLTreeNode()
	{
		throw new RuntimeException("Error: default PLTreeNode constuctor called");
	}

	/**
	 * The usual constructor used internally in this class. No checks made on
	 * validity of parameters as this has been made private, so can only be
	 * called within this class.
	 * 
	 * @param type
	 *            The <code>NodeType</code> represented by this node
	 * @param child1
	 *            The first child, if there is one, or null if there isn't
	 * @param child2
	 *            The second child, if there is one, or null if there isn't
	 */
	private PLTreeNode(NodeType type, PLTreeNode child1, PLTreeNode child2)
	{
		// Don't need to do lots of tests because only this class can create nodes directly,
		// any construction required by another class has to go through reversePolishBuilder,
		// which does all the checks
		this.type = type;
		this.child1 = child1;
		this.child2 = child2;
	}

	/**
	 * A copy constructor to take a (recursive) deep copy of the sub-tree based
	 * on this node. Guarantees that no sub-node is shared with the original
	 * other
	 * 
	 * @param node
	 *            The node that should be deep copied
	 */
	private PLTreeNode(PLTreeNode node)
	{
		if (node == null)
			throw new RuntimeException("Error: tried to call the deep copy constructor on a null PLTreeNode");
		type = node.type;
		if (node.child1 == null)
			child1 = null;
		else
			child1 = new PLTreeNode(node.child1);
		if (node.child2 == null)
			child2 = null;
		else
			child2 = new PLTreeNode(node.child2);
	}


	/**
	 * Takes a list of <code>NodeType</code> values describing a valid
	 * propositional logic expression in reverse polish notation and constructs
	 * the corresponding expression tree. c.f. <a href=
	 * "https://en.wikipedia.org/wiki/Reverse_Polish_notation">https://en.wikipedia.org/wiki/Reverse_Polish_notation</a>
	 * <p>
	 * Thus an input containing
	 * </p>
	 * <pre>
	 * {NodeType.P, NodeType.Q, NodeType.NOT, NodeType.AND.
	 * </pre>
	 *
	 * corresponds to
	 *
	 * <pre>
	 * P∧¬Q
	 * </pre>
	 *
	 * Leaving out the <code>NodeType</code> enum class specifier, we get that
	 *
	 * <pre>
	 * { R, P, OR, TRUE, Q, NOT, AND, IMPLIES }
	 * </pre>
	 *
	 * represents
	 *
	 * <pre>
	 * ((R∨P)→(⊤∧¬Q))
	 * </pre>
	 *
	 * @param typeList
	 *            An <code>NodeType</code> array in reverse polish order
	 * @return the <code>PLTreeNode</code> of the root of the tree representing
	 *         the expression constructed for the reverse polish order
	 *         <code>NodeType</code> array
	 */
	public static PLTreeNodeInterface reversePolishBuilder(NodeType[] typeList)
	{
		if (typeList == null || typeList.length == 0)
		{
			logger.error("Trying to create an empty PLTree");
			return null;
		}

		Deque<PLTreeNode> nodeStack = new LinkedList<>();

		for (NodeType type : typeList)
		{
			int arity = type.getArity();

			if (nodeStack.size() < arity)
			{
				logger.error(String.format(
						"Error: Malformed reverse polish type list: \"%s\" has arity %d, but is being applied to only %d arguments", //
						type, arity, nodeStack.size()));
				return null;
			}
			if (arity == 0)
				nodeStack.addFirst(new PLTreeNode(type, null, null));
			else if (arity == 1)
			{
				PLTreeNode node1 = nodeStack.removeFirst();
				nodeStack.addFirst(new PLTreeNode(type, node1, null));
			}
			else
			{
				PLTreeNode node2 = nodeStack.removeFirst();
				PLTreeNode node1 = nodeStack.removeFirst();
				nodeStack.addFirst(new PLTreeNode(type, node1, node2));
			}
		}
		if (nodeStack.size() > 1)
		{
			logger.error("Error: Incomplete term: multiple subterms not combined by top level symbol");
			return null;
		}

		return nodeStack.removeFirst();
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#getReversePolish()
	 */
	@Override
	public NodeType[] getReversePolish()
	{
		Deque<NodeType> nodeQueue = new LinkedList<>();

		getReversePolish(nodeQueue);

		return nodeQueue.toArray(new NodeType[0]);

	}

	/**
	 * A helper method for <code>getReversePolish()</code> used to accumulate
	 * the elements of the reverse polish notation description of the current
	 * tree
	 *
	 * @param nodeQueue
	 *            A queue of <code>NodeType</code> objects used to accumulate
	 *            the values of the reverse polish notation description of the
	 *            current tree
	 */
	private void getReversePolish(Deque<NodeType> nodeQueue)
	{
		if (child1 != null)
			child1.getReversePolish(nodeQueue);
		if (child2 != null)
			child2.getReversePolish(nodeQueue);
		nodeQueue.addLast(type);
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#toString()
	 */
	@Override
	public String toString()
	{
		return toStringPrefix();
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#toStringPrefix()
	 */
	@Override
	public String toStringPrefix()
	{
		String left = null;
		String right = null;

		if (child1 != null) {
			left = child1.toStringPrefix();
		}
		if (child2 != null) {
			right = child2.toStringPrefix();
		}

		if (left == null) {
			return type.getPrefixName();
		}

		if (right == null) {
			return type.getPrefixName() + "(" + left + ")";
		}

		return type.getPrefixName() + "(" + left + "," + right + ")";
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#toStringInfix()
	 */
	@Override
	public String toStringInfix()
	{
		String left = null;
		String right = null;

		if (child1 != null) {
			left = child1.toStringInfix();
		}
		if (child2 != null) {
			right = child2.toStringInfix();
		}

		if (left == null) {
			return type.getInfixName();
		}

		if (right == null) {
			return type.getInfixName() + "" + left;
		}

		return "(" + left + type.getInfixName() + right + ")";
	}
	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#applyVarBindings(java.util.Map)
	 */
	@Override
	public void applyVarBindings(Map<NodeType, Boolean> bindings)
	{
		//Create a copy of the bindings so as not to change the original bindings map.
		Map<NodeType, Boolean> bindingsCopy = new HashMap<>(bindings);

		//Checking to make sure they are only using variable bindings.
		for (NodeType nt : bindings.keySet()) {
			if (!nt.isVar()) {
				bindingsCopy.remove(nt);
			}
		}

		if (child1 != null) {
			child1.applyVarBindings(bindingsCopy);
		}

		if (child2 != null) {
			child2.applyVarBindings(bindingsCopy);
		}

		if (bindingsCopy.containsKey(type)) {
			type = ((bindingsCopy.get(type))?NodeType.TRUE:NodeType.FALSE);
		}
		return;
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#evaluateConstantSubtrees()
	 */
	@Override
	public Boolean evaluateConstantSubtrees()
	{
		if (child1 != null) {
			child1.evaluateConstantSubtrees();
		}
		if (child2 != null) {
			child2.evaluateConstantSubtrees();
		}


		if (type.isVar()) {
			return null;
		}

		switch (type) {
			case OR:
				if (child1.type == NodeType.TRUE || child2.type == NodeType.TRUE) {
					//Either is true meaning the statement as a whole is true.
					type = NodeType.TRUE;
					child1 = null;
					child2 = null;
					return true;
				} else if (child1.type == NodeType.FALSE) {
					//The first condition is false.
					if (child2.type == NodeType.FALSE) {
						//Second condition is also false therefore the statement is false.
						type = NodeType.FALSE;
						child1 = null;
						child2 = null;
						return false;
					} else {
						//There is still a variable in child2 as it is not true or false.
						type = child2.type;
						child1 = child2.child1;
						child2 = child2.child2;
						return null;
					}
				} else if (child2.type == NodeType.FALSE) {
					//There is still a variable in child1 as it it not true or false.
					type = child1.type;
					child2 = child1.child2;
					child1 = child1.child1;
					return null;
				}
				//Both still have variables, nothing we can do.
				return null;
			case AND:
				if (child1.type == NodeType.FALSE || child2.type == NodeType.FALSE) {
					//Either is false meaning the statement as a whole is false.
					type = NodeType.FALSE;
					child1 = null;
					child2 = null;
					return false;
				} else if (child1.type == NodeType.TRUE) {
					//The first condition is true.
					if (child2.type == NodeType.TRUE) {
						//Second condition is also true therefore the statement is true.
						type = NodeType.TRUE;
						child1 = null;
						child2 = null;
						return true;
					} else {
						//There is still a variable in child2 as it is not true or false.
						type = child2.type;
						child1 = child2.child1;
						child2 = child2.child2;
						return null;
					}
				} else if (child2.type == NodeType.TRUE) {
					//There is still a variable in child1 as it it not true or false.
					type = child1.type;
					child2 = child1.child2;
					child1 = child1.child1;
					return null;
				}
				//Both still have variables, nothing we can do.
				return null;
			case NOT:
				if (child1.type == NodeType.TRUE) {
					type = NodeType.FALSE;
					child1 = null;
					child2 = null;
					return false;
				} else if (child1.type == NodeType.FALSE) {
					type = NodeType.TRUE;
					child1 = null;
					child2 = null;
					return true;
				}
				//There is still a variable, nothing to do.
				return null;
			case IMPLIES:
				if (child1.type == NodeType.FALSE || child2.type == NodeType.TRUE) {
					//Either is true meaning the statement as a whole is true.
					type = NodeType.TRUE;
					child1 = null;
					child2 = null;
					return true;
				} else if (child1.type == NodeType.TRUE) {
					//The first condition is false.
					if (child2.type == NodeType.FALSE) {
						//Second condition is also false therefore the statement is false.
						type = NodeType.FALSE;
						child1 = null;
						child2 = null;
						return false;
					} else {
						//There is still a variable in child2 as it is not true or false.
						type = child2.type;
						child1 = child2.child1;
						child2 = child2.child2;
						return null;
					}
				} else if (child2.type == NodeType.FALSE) {
					//There is still a variable in child1 as it it not true or false.
					type = child1.type;
					child2 = child1.child2;
					child1 = child1.child1;
					return null;
				}
				//Both still have variables, nothing we can do.
				return null;
			case TRUE:
				return true;
			case FALSE:
				return false;
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#reduceToCNF()
	 */
	@Override
	public void reduceToCNF()
	{
		replaceImplies();
		pushNotDown();
		pushOrBelowAnd();
		makeAndOrRightDeep();
		return;
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#replaceImplies()
	 */
	@Override
	public void replaceImplies()
	{
		if (type == NodeType.IMPLIES) {
			type = NodeType.OR;
			child1 = new PLTreeNode(NodeType.NOT, child1, null);
		}


		if (child1 != null) {
			child1.replaceImplies();
		}

		if (child2 != null) {
			child2.replaceImplies();
		}
		return;
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#pushNotDown()
	 */
	@Override
	public void pushNotDown()
	{
		if (type == NodeType.NOT) {
			if (child1.type == NodeType.NOT) {
				PLTreeNode old = child1;
				type = old.child1.type;
				child1 = old.child1.child1;
				child2 = old.child1.child2;
			} else if (child1.type == NodeType.OR) {
				type = NodeType.AND;
				PLTreeNode old = child1;
				child1 = new PLTreeNode(NodeType.NOT, old.child1, null);
				child2 = new PLTreeNode(NodeType.NOT, old.child2, null);
			} else if (child1.type == NodeType.AND) {
				type = NodeType.OR;
				PLTreeNode old = child1;
				child1 = new PLTreeNode(NodeType.NOT, old.child1, null);
				child2 = new PLTreeNode(NodeType.NOT, old.child2, null);
			}
		}

		if (child1 != null) {
			child1.pushNotDown();
		}

		if (child2 != null) {
			child2.pushNotDown();
		}
		return;
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#pushOrBelowAnd()
	 */
	@Override
	public void pushOrBelowAnd()
	{
		pushOrBelowAndPre(this);
		pushOrBelowAndPost(this);
		return;
	}

	private void pushOrBelowAndPre(PLTreeNode tree) {
		if (tree == null) {
			return;
		} else if (tree.type == NodeType.OR) {
			if (tree.child1.type == NodeType.AND) {
				tree.type = NodeType.AND;
				PLTreeNode old = tree.child1;
				tree.child1 = new PLTreeNode(NodeType.OR, new PLTreeNode(old.child1), new PLTreeNode(tree.child2));
				tree.child2 = new PLTreeNode(NodeType.OR, new PLTreeNode(old.child2), new PLTreeNode(tree.child2));
				pushOrBelowAndPost(this);
			} else if (tree.child2.type == NodeType.AND) {
				tree.type = NodeType.AND;
				PLTreeNode old = tree.child2;
				tree.child2 = new PLTreeNode(NodeType.OR, new PLTreeNode(tree.child1), new PLTreeNode(old.child2));
				tree.child1 = new PLTreeNode(NodeType.OR, new PLTreeNode(tree.child1), new PLTreeNode(old.child1));
				pushOrBelowAndPost(this);
			}
		}

		pushOrBelowAndPre(tree.child1);
		pushOrBelowAndPre(tree.child2);
	}

	private void pushOrBelowAndPost(PLTreeNode tree) {
		if (tree == null) {
			return;
		}
		pushOrBelowAndPost(tree.child1);
		pushOrBelowAndPost(tree.child2);

		if (tree.type == NodeType.OR) {
			if (tree.child1.type == NodeType.AND) {
				tree.type = NodeType.AND;
				PLTreeNode old = tree.child1;
				tree.child1 = new PLTreeNode(NodeType.OR, new PLTreeNode(old.child1), new PLTreeNode(tree.child2));
				tree.child2 = new PLTreeNode(NodeType.OR, new PLTreeNode(old.child2), new PLTreeNode(tree.child2));
			} else if (tree.child2.type == NodeType.AND) {
				tree.type = NodeType.AND;
				PLTreeNode old = tree.child2;
				tree.child2 = new PLTreeNode(NodeType.OR, new PLTreeNode(tree.child1), new PLTreeNode(old.child2));
				tree.child1 = new PLTreeNode(NodeType.OR, new PLTreeNode(tree.child1), new PLTreeNode(old.child1));
			}
		}
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#makeAndOrRightDeep()
	 */
	@Override
	public void makeAndOrRightDeep()
	{
		if (type == NodeType.OR) {
			while (child1.type == NodeType.OR && child2.type != NodeType.IMPLIES) {
				PLTreeNode node = child1;
				child1 = child1.child1;
				child2 = new PLTreeNode(NodeType.OR, node.child2, child2);
			}
		} else if (type == NodeType.AND) {
			while (child1.type == NodeType.AND && child2.type != NodeType.IMPLIES) {
				PLTreeNode node = child1;
				child1 = child1.child1;
				child2 = new PLTreeNode(NodeType.AND, node.child2, child2);
			}
		}

		if (child1 != null) {
			child1.makeAndOrRightDeep();
		}

		if (child2 != null) {
			child2.makeAndOrRightDeep();
		}
		return;
	}


}
