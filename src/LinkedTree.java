//****************************************************************************
//		   File : LinkedTree.java
//		   Group: N_T9
//         Names: Steven Haut
//				  Cole Ruter
//
//
// Description: This file accepts Node information and stores Nodes in an
//				ArrayList. When the user is ready to process the information,
//				this file will add all the Nodes to a "series" of linked
//				lists and check for any errors. If there are no errors, then
//				this file will continue to determine each path and the
//				entire duration of the path. Then the paths are sorted in
//				decending order.
//****************************************************************************
import java.util.*;

//--------------------------------------------------------
// LinkedTree Class
//--------------------------------------------------------
public class LinkedTree
{
	private Node root;
	private String errorType = "";
	private String criticalPath = "";
	private String pathsList = "";
	private String activitiesList = "";
	ArrayList<Node> activities = null;
	ArrayList<Node> activitiesBackup = null;

	//----------------------------------------------------
	// LinkedTree Constructor
	//----------------------------------------------------
	public LinkedTree() {
		root = null;
	}

	//----------------------------------------------------
	// Getter for Root
	//----------------------------------------------------
	public Node getRoot() {
		return root;
	}

	
	//----------------------------------------------------
	// Getter for errorType
	//----------------------------------------------------
	public String getErrorType() {
		return errorType;
	}
	
	
	//----------------------------------------------------
	// Getter for criticalPath
	//----------------------------------------------------
	public String getCriticalPath() {
		return criticalPath;
	}
	
	
	//----------------------------------------------------
	// Setter for criticalPath
	//----------------------------------------------------
	public void resetCriticalPath() {
		criticalPath = "";
	}
	
	
	//----------------------------------------------------
	// Setter for pathsList
	//----------------------------------------------------
	public void resetPathsList() {
		pathsList = "";
	}
		
		
	//----------------------------------------------------
	// Getter for pathsList
	//----------------------------------------------------
	public String getPathsList() {
		return pathsList;
	}
	
	//----------------------------------------------------
	// Adds Nodes to the "activities" ArrayList
	//----------------------------------------------------
	public boolean add(String name, int length, String[] dependents) {
		Node newNode = new Node(name, length, dependents);
		if(activities == null) {
			this.activities = new ArrayList<Node>();
			this.activities.add(newNode);
			return true;
		}
		else {
			this.activities.add(newNode);
			return true;
		}
	}


	//----------------------------------------------------
	// Adds "activities" ArrayList to the LinkedTree
	//----------------------------------------------------
	public boolean addList() {
		int size = activities.size();
		boolean noError = true;
		ArrayList<Node> temp = new ArrayList<Node>();

		int i = 0;
		while(i < size && noError) {
			Node nextNode = activities.get(i);
			noError = addToTree(nextNode);

			if(!noError) {
				temp.add(nextNode);
				noError = true;
			}
			i++;
		}

		if(temp.isEmpty()) {
			return true;
		}
		else {
			if(temp.size() != activities.size()) {
				activities = temp;
				return addList();
			}
			else {
				return false;
			}
		}
	}


	//----------------------------------------------------
	// Adds Node to LinkedTree
	//----------------------------------------------------
	public boolean addToTree(Node newActivity) {
		Node newNode = newActivity;
		Node iterator = root;

	//--------------------------------------------------------
	// Root Case
	//--------------------------------------------------------
		//----------------------------------------------------
		// Empty Tree
		//----------------------------------------------------
		if(root == null) {
			root = newNode;
			return true;
		}

		//----------------------------------------------------
		// Two Unconnected activities with no Dependencies
		//----------------------------------------------------
		else if(root.dependencies == null && newNode.dependencies == null && !root.activityName.equals(newNode.activityName)) {
			errorType = "connection";
			return false;
		}

		//----------------------------------------------------
		// Fills in Roots Information
		//----------------------------------------------------
		else if(root.activityName.equals(newNode.activityName)) {
			if(newNode.dependencies != null) {
				root.dependencies = newNode.dependencies;
			}
			root.dependencies = newNode.dependencies;
			root.duration = newNode.duration;
			return true;
		}

		//----------------------------------------------------
		// Root has dependencies and is dependent on newNode
		//----------------------------------------------------
		else if(isDependent(root, newNode)){
			newNode.addNext(root);
			root.addLast(newNode);
			root = newNode;
			return true;
		}

	//--------------------------------------------------------
	// Non-Root Cases
	//--------------------------------------------------------
		else {
			return newHasDependent(newNode, iterator);
		}
	}

	//--------------------------------------------------------
	// Determines newNodes Location for Non-Root Cases
	//--------------------------------------------------------
	public boolean newHasDependent(Node newNode, Node iterator) {
		boolean added = isDependent(newNode, iterator);
		
		if(added) {
			Boolean alreadyAdded = false;
			if(iterator.next != null) {
				for(int i=0; i<iterator.next.size(); i++) {
					if(iterator.next.get(i).activityName.equals(newNode.activityName)) {
						alreadyAdded = true;
					}
				}
			}
			if(!alreadyAdded) {
				iterator.addNext(newNode);
			}
			alreadyAdded = false;
			if(newNode.last != null) {
				for(int i=0; i<newNode.last.size(); i++) {
					if(newNode.last.get(i).activityName.equals(iterator.activityName)) {
						alreadyAdded = true;
					}
				}
			}
			if(!alreadyAdded) {
				newNode.addLast(iterator);
			}
			if(newNode.last.size() == newNode.dependencies.length) {
				return true;
			}
			else {
				added = false;
			}
		}
		if (!added) {
			if(iterator.next != null) {
				for(int i=0; i<iterator.next.size(); i++) {
					if(newHasDependent(newNode, iterator.next.get(i))) {
						return true;
					}
				}
			}
		}
		return false;
	}



	//--------------------------------------------------------
	// Determines if firstNode is Dependent on the secondNode
	//--------------------------------------------------------
	public boolean isDependent(Node firstNode, Node secondNode) {
		if(firstNode.dependencies == null) {
			return false;
		}
		for(int i=0; i<firstNode.dependencies.length; i++) {
			if(firstNode.dependencies[i].equals(secondNode.activityName)) {
				return true;
			}
		}
		return false;
	}
	

	//--------------------------------------------------------
	// Checks for Disconnected Merges
	//--------------------------------------------------------
	public boolean mergeCheck(Node iterator) {
		if(iterator.dependencies != null) {
			if(iterator.last == null) {
				iterator.last = new ArrayList<Node>();
			}
			if(iterator.last.size() != iterator.dependencies.length) {
				int i=0;
				boolean notEqual = true;
				boolean missing;

				while(i<iterator.dependencies.length && notEqual) {
					missing = true;
					for(int j=0; j<iterator.last.size(); j++) {
						if(iterator.last.get(j).activityName.equals(iterator.dependencies[i])) {
							missing = false;
						}
					}
					if(missing) {
						Node temp = findNode(iterator.dependencies[i], root);
						iterator.addLast(temp);
						temp.addNext(iterator);

						if(iterator.last.size() != iterator.dependencies.length) {
							notEqual = true;
						}
					}
					i++;
				}
			}
		}

		if(iterator.next == null) {
			return true;
		}
		else {
			for(int i=0; i <iterator.next.size(); i++) {
				try {
					boolean check = mergeCheck(iterator.next.get(i));
					if(!check) {
						return false;
					}
				}
				// Loop Detected in LinkedTree
				catch(StackOverflowError e) {
					errorType = "cycle";
					return false;
				}
			}
			return true;
		}
	}
	
	
	//--------------------------------------------------------
	// Finds an activity and changes it's duration
	//--------------------------------------------------------
	public boolean changeDuration(String name, int duration) {
		Node currentNode = findNode(name, root);
		if(currentNode != null) {
			currentNode.setDuration(duration);
			return true;
		}
		
		return false;
	}


	//--------------------------------------------------------
	// Looks for a Node Based on it's activityName
	//--------------------------------------------------------
	public Node findNode(String name, Node iterator) {
		if(iterator.activityName.equals(name)) {
			return iterator;
		}
		if(iterator.next != null) {
			for(int i=0; i<iterator.next.size(); i++) {
				Node temp = findNode(name, iterator.next.get(i));
				if(temp != null) {
					return temp;
				}
			}
		}
		return null;
	}
	
	//--------------------------------------------------------
	// Returns a String of all Activities Alphabetized
	//--------------------------------------------------------
	public String allActivites() {
		String[] alphaArray = new String[activitiesBackup.size()];
		for(int i=0; i<activitiesBackup.size(); i++) {
			alphaArray[i] = activitiesBackup.get(i).printNode();
		}
		Arrays.sort(alphaArray);
		
		for(int i=0; i<alphaArray.length; i++) {
			Node temp = findNode(alphaArray[i], root);
			
			activitiesList += "Activity Name:	" + temp.activityName + "	Activity Duration:	" + temp.duration;
			
			if(i != alphaArray.length-1) {
				activitiesList += "\n";	// Displays Each Activity on a New Line
			}
		}
		
		return activitiesList;
	}


	//--------------------------------------------------------
	// Returns a String of Paths and Durations
	//--------------------------------------------------------
	public String printTree() {
		if(activitiesBackup == null) {
			activitiesBackup = activities;
		}
		
		if(root != null) {
			String line = root.activityName;
			int count = root.duration;
			String listNames = "";
			ArrayList<String> pathStrings = new ArrayList<String>();
			ArrayList<Integer> pathCount = new ArrayList<Integer>();
			Node iterator = root;
			if(root.next != null) {
				createBranchStrings(iterator, line, count, pathStrings, pathCount);
				int arrayLength = pathCount.size();
				int[] newArray = new int[arrayLength];
				int[] sortedArray = new int[arrayLength];
				for(int i=0; i<pathCount.size(); i++) {
					newArray[i] = pathCount.get(i);
				}
				Arrays.sort(newArray);

				for(int i=0; i<arrayLength; i++) {
					sortedArray[i] = newArray[arrayLength-1-i];
				}

				for(int i=0; i<sortedArray.length; i++) {
					for(int j=0; j<pathCount.size(); j++) {
						if(sortedArray[0] == pathCount.get(j)) {
							criticalPath += "Path Duration: " + pathCount.get(j) + "	Path: " + pathStrings.get(j) + "\n";
						}
						if(sortedArray[i] == pathCount.get(j)) {
							listNames += "Path Duration: " + pathCount.get(j) + "	Path: " + pathStrings.get(j) + "\n";
							pathStrings.remove(j);
							pathCount.remove(j);
						}
					}
				}
				pathsList = listNames;
				return listNames;
			}
			else {
				criticalPath += "Path Duration: " + count + "	Path: " + root.activityName + "\n";
				pathsList += "Path Duration: " + count + "	Path: " + root.activityName + "\n";;
				return "Path Duration:  "+ count + "	Path: " + root.activityName + "\n";
			}
		}
		pathsList = "";
		return "";
	}
	//--------------------------------------------------------
	// Returns Strings of Branches for "printTree()"
	//--------------------------------------------------------
	public void createBranchStrings(Node iterator, String line, int count, ArrayList<String> pathStrings, ArrayList<Integer> pathCount) {
		if(iterator != root) {
			line += " -> " + iterator.activityName;
			count += iterator.duration;
		}

		if(iterator.next == null) {
			pathStrings.add(line);
			pathCount.add(count);
		}

		else {
			for(int i=0; i <iterator.next.size(); i++) {

				createBranchStrings(iterator.next.get(i), line, count, pathStrings, pathCount);
			}
		}
	}
}