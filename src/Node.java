//****************************************************************************
//		   File : Node.java
//		   Group: N_T9
//         Names: Steven Haut
//				  Cole Ruter
//
// Description: This file contains information about the Node, which will
//				be an Activity with general information about it and an
//				ArrayList of pointers to it's next and last point in the
//				LinkedTree.
//****************************************************************************
import java.util.*;

//--------------------------------------------------------
// Activity Nodes for the Linked Tree
//--------------------------------------------------------
public class Node
{
	//--------------------------------------------------------
	// Node Variables
	//--------------------------------------------------------
	public String activityName;
	public int duration;
	public String[] dependencies = null;
	public ArrayList<Node> next = null;
	public ArrayList<Node> last = null;

	//--------------------------------------------------------
	// Main Constructor
	//--------------------------------------------------------
	public Node(String name, int length, String[] dependents) {
		this.activityName = name;
		this.duration = length;
		this.dependencies = dependents;
	}

	//--------------------------------------------------------
	// Overloaded Constructor
	//--------------------------------------------------------
	public Node(String name) {
		activityName = name;
	}


	//--------------------------------------------------------
	// Adds newNode to Node's "next" ArrayList
	//--------------------------------------------------------
	public void addNext(Node newNode) {
		if(next == null) {
			this.next = new ArrayList<Node>();
			this.next.add(newNode);
		}
		else {
			this.next.add(newNode);
		}
	}


	//--------------------------------------------------------
	// Adds newNode to Node's "last" Variable
	//--------------------------------------------------------
	public void addLast(Node newNode) {
		if(last == null) {
			this.last = new ArrayList<Node>();
			this.last.add(newNode);
		}
		else {
			this.last.add(newNode);
		}
	}
	
	
	//--------------------------------------------------------
	// Setter for Node's Durations
	//--------------------------------------------------------
	public void setDuration(int newDuration) {
		this.duration = newDuration;
	}


	//--------------------------------------------------------
	// Getter for Node's Activity Name
	//--------------------------------------------------------
	public String printNode() {
		return activityName;
	}

}