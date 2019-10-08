//****************************************************************************
//		   File : ProjectPanel.java
//		   Group: N_T9
//         Names: Steven Haut
//				  Cole Ruter
//
// Description: This file contains the GUI code. It sets up the User
//				Interface and performs error checking based on input and
//				displays any errors from the other files. If there are no
//				errors, a confirmation message is displayed.
//****************************************************************************
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//--------------------------------------------------------
// ProjectPanel Class
//--------------------------------------------------------
public class ProjectPanel extends JPanel
{
	//--------------------------------------------------------
	// ProjectPanel Variables
	//--------------------------------------------------------
	private JButton about, help, add, process, restart, quit, criticalPath, changeDuration, createFile;
	private JPanel wholePanel, leftPanel, bottomPanel, topPanel, inputPanel, processPanel, msgPanel, postProcessPanel, filePanel;
	private JLabel activityLabel, durationLabel, dependLabel, msgLabel;
	private JTextField activityField, durationField, dependField;
	private JScrollPane scrollPane;
	private JTextArea output;
	private String outputText;

	//Create a new linkedTree
	LinkedTree list = new LinkedTree();

	//--------------------------------------------------------
	// ProjectPanel Constructor
	//--------------------------------------------------------
	public ProjectPanel()
	{
		wholePanel = this;
		//-------------------------
		// JLabels
		//-------------------------
		activityLabel = new JLabel("  Activity Name");
		durationLabel = new JLabel("  Duration");
		dependLabel = new JLabel("  Dependencies");
		msgLabel = new JLabel("");
		msgLabel.setVisible(false);

		//-------------------------
		// JTextFields
		//-------------------------
		activityField = new JTextField();
		durationField = new JTextField();
		dependField = new JTextField();

		//-------------------------
		// JButtons
		//-------------------------
		about = new JButton("About");
		help = new JButton("Help");
		add = new JButton("Add");
		process = new JButton("Process");
		criticalPath = new JButton("Critical Path");
		changeDuration = new JButton("Change Duration");
		createFile = new JButton("Create File");
		restart = new JButton("Restart");
		quit = new JButton("Quit");

		//-------------------------
		// addActionListeners
		//-------------------------
		about.addActionListener(new ButtonListener());
		help.addActionListener(new ButtonListener());
		add.addActionListener(new ButtonListener());
		process.addActionListener(new ButtonListener());
		criticalPath.addActionListener(new ButtonListener());
		changeDuration.addActionListener(new ButtonListener());
		createFile.addActionListener(new ButtonListener());
		restart.addActionListener(new ButtonListener());
		quit.addActionListener(new ButtonListener());

		//-------------------------
		// Setup for Top Panel
		//-------------------------
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());
		topPanel.add(about);
		topPanel.add(help);

		//-------------------------
		// Setup for Message Label
		//-------------------------
		msgPanel = new JPanel(new FlowLayout());
		msgPanel.add(msgLabel);


		//-------------------------
		// Setup for Input Panel
		//-------------------------
		inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(3,2));
		inputPanel.add(activityLabel);
		inputPanel.add(activityField);
		inputPanel.add(durationLabel);
		inputPanel.add(durationField);
		inputPanel.add(dependLabel);
		inputPanel.add(dependField);

		//------------------------------
		// Setup for Post Process Panel
		//------------------------------
		postProcessPanel = new JPanel();
		postProcessPanel.add(criticalPath);
		postProcessPanel.add(changeDuration);


		//------------------------------
		// Setup for File Panel
		//------------------------------
		filePanel = new JPanel();
		filePanel.add(createFile);

		//-------------------------
		// Setup for Process Panel
		//-------------------------
		processPanel = new JPanel();
		processPanel.setLayout(new FlowLayout());
		processPanel.add(add);
		processPanel.add(process);

		//-------------------------
		// Setup for Bottom Panel
		//-------------------------
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.add(restart);
		bottomPanel.add(quit);

		//-----------------------------
		// Sets the Entire Left Side
		//-----------------------------
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(topPanel);
		leftPanel.add(msgPanel);
		leftPanel.add(inputPanel);
		leftPanel.add(processPanel);
		leftPanel.add(postProcessPanel);
		leftPanel.add(filePanel);
		leftPanel.add(bottomPanel);

		//-----------------------------
		// Sets the Entire Right Side
		//-----------------------------
		output = new JTextArea();
		output.setText("");
		output.setLineWrap(true);
		output.setWrapStyleWord(true);
		scrollPane = new JScrollPane(output);

		//-----------------------------
		// Sets for Entire Layout
		//-----------------------------
		setLayout(new GridLayout(1,2));
		add(leftPanel);
		add(scrollPane);

	}//End ProjectPanel Constructor


	//--------------------------------------------------------
	// ButtonListener Class
	//--------------------------------------------------------
	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{

			String line, activityName = "";
		    int duration = 0;
		    String[] dependencies = null;
			msgLabel.setVisible(false);
			//-----------------------------------------
			// Add Activity to LinkedTree
			//-----------------------------------------
			if(e.getSource() == add)
			{
				if(list.getRoot() == null) { // Stops user from adding activities after the list has been processed once
					if(!activityField.getText().equals("") && !durationField.getText().equals(""))
					{
						activityName = activityField.getText();
						try {
							line = durationField.getText();
							duration = Integer.parseInt(line);

							line = dependField.getText();
							if(line.contains(",")) {
								msgLabel.setText("Seperate Dependencies by a space, NOT a comma.");
								msgLabel.setForeground(Color.red);
								msgLabel.setVisible(true);
								output.setText("");
							}
							else if(line.contains("  ")) {
								msgLabel.setText("Dependencies contains too many spaces.");
								msgLabel.setForeground(Color.red);
								msgLabel.setVisible(true);
								output.setText("");
							}
							else {
								if(!line.equals("")) {
									dependencies = line.split(" ");
								}
								boolean added = list.add(activityName, duration, dependencies);

								activityField.setText("");
								durationField.setText("");
								dependField.setText("");

								if(added) {
									msgLabel.setText("Activity Added");
									msgLabel.setForeground(Color.blue);
									msgLabel.setVisible(true);
								}
								else {
									//-------------------------------------------
									//	ERROR Handling for Incompatible Addition
									//-------------------------------------------
									msgLabel.setText("Activity NOT Added.");
									msgLabel.setForeground(Color.red);
									msgLabel.setVisible(true);
									list = new LinkedTree();
									output.setText("");
								}
							}
						}
						//-------------------------------------------
						//	ERROR Handling for non-integer Duration
						//-------------------------------------------
						catch(NumberFormatException exception) {
							msgLabel.setText("Enter an integer for duration");
							msgLabel.setForeground(Color.red);
							msgLabel.setVisible(true);
						}
					}
					else
					{
						//-----------------------------------------
						// ERROR Handling for Empty Input
						//-----------------------------------------
						if(durationField.getText().equals("") && activityField.getText().equals(""))
						{
							msgLabel.setText("Activity Name & Duration are Empty.");
						}
						else if(activityField.getText().equals(""))
						{
							msgLabel.setText("Activity Name Field is Empty.");
						}
						else {
							msgLabel.setText("Duration Field is Empty.");
						}

						msgLabel.setForeground(Color.red);
		 				msgLabel.setVisible(true);
					}
				}
				else {
					//--------------------------------------------
					//	ERROR Handling for Adding after Processed
					//--------------------------------------------
					msgLabel.setText("Must Restart before Adding.");
					msgLabel.setForeground(Color.red);
					msgLabel.setVisible(true);
				}

			}


			//-----------------------------------------
			// Processes the Entire LinkedTree
			//-----------------------------------------
			if(e.getSource() == process)
			{
				if(list.getRoot() == null) { //Stops user from Processing if it's already been processed
					if(list.activities != null) {
						boolean noError = list.addList();
						if(noError) {
							noError = list.mergeCheck(list.getRoot());
							if(noError) {
								outputText = list.printTree();
								output.setText(outputText);

								msgLabel.setText("Activites Processed.");
								msgLabel.setForeground(Color.blue);
								msgLabel.setVisible(true);
							}
						}
						if(!noError){
							//-------------------------------------------
							//	ERROR Handling for Incompatible Addition
							//-------------------------------------------
							String errorType = list.getErrorType();

							switch(errorType) {
								case "connection":
									msgLabel.setText("Activities NOT Connected. Start over.");
									break;
								case "cycle":
									msgLabel.setText("Cycle in the Input. Start over.");
									break;
								default:
									msgLabel.setText("Activity conflict. Start over.");
							}
							msgLabel.setForeground(Color.red);
							msgLabel.setVisible(true);
							list = new LinkedTree();
							output.setText("");
						}
					}
					else {
						msgLabel.setText("You must Add an Activity.");
						msgLabel.setForeground(Color.red);
						msgLabel.setVisible(true);
						output.setText("");
					}
				}
				else {
					//-------------------------------------------
					//	If list has already been processed
					//-------------------------------------------
					outputText = list.getPathsList();
					output.setText(outputText);

					msgLabel.setText("Activites Processed.");
					msgLabel.setForeground(Color.blue);
					msgLabel.setVisible(true);
				}
			}


			//-----------------------------------------
			// Display the Critical Path
			//-----------------------------------------
			if(e.getSource() == criticalPath)
			{
				String path = list.getCriticalPath();
				if(path.equals("")) {
					//-------------------------------------------
					//	ERROR Handling for Unprocessed List
					//-------------------------------------------
					msgLabel.setText("You must process before displaying critical path.");
					msgLabel.setForeground(Color.red);
					msgLabel.setVisible(true);
				}
				else {
					output.setText("	Critical Path(s): \n" + "----------------------------------------------------------------------\n"
				+ path);

					msgLabel.setText("Critical Paths Determined.");
					msgLabel.setForeground(Color.blue);
					msgLabel.setVisible(true);
				}
			}

			//-----------------------------------------
			// Display the Critical Path
			//-----------------------------------------
			if(e.getSource() == changeDuration) {
				if(list.getRoot() != null)
				{
					JTextField activity = new JTextField();
					JTextField newDuration = new JTextField();
					Object[] message = {
						"Activity:", activity,
						"Duration:", newDuration
					};

					int option = JOptionPane.showConfirmDialog(wholePanel, message, "Change Duration for Activity", JOptionPane.OK_CANCEL_OPTION);
					if (option == JOptionPane.OK_OPTION) {

						if(!activity.getText().equals("") && !newDuration.getText().equals(""))
						{
							if(list.findNode(activity.getText(), list.getRoot()) != null)
							{
								Node changeNode = list.findNode(activity.getText(), list.getRoot());
								try
								{
									duration = Integer.parseInt(newDuration.getText());
									changeNode.setDuration(duration);
									list.resetCriticalPath();
									list.resetPathsList();
									String consume = list.printTree();

									msgLabel.setText("Activity duration changed.");
									msgLabel.setForeground(Color.blue);
									msgLabel.setVisible(true);
								} catch (NumberFormatException ex) {
									msgLabel.setText("Activity duration must be an integer");
									msgLabel.setForeground(Color.red);
		 							msgLabel.setVisible(true);
								}
							}
							else
							{
								msgLabel.setText("Activity entered was invalid");
								msgLabel.setForeground(Color.red);
		 						msgLabel.setVisible(true);
							}
						} else {
							msgLabel.setText("Activity or Duration was empty");
							msgLabel.setForeground(Color.red);
		 					msgLabel.setVisible(true);
						}
					}
				}
				else
				{
					msgLabel.setText("You must process list before changing duration");
					msgLabel.setForeground(Color.red);
					msgLabel.setVisible(true);
				}
				/*///////////////////////////////////////////////////////// <<<---- POPUP FOR aName and aDuration
				String aName = activityField.getText();
				line = durationField.getText();
				int aDuration = Integer.parseInt(line);


				Node changeNode = list.findNode(aName, list.getRoot());
				changeNode.setDuration(aDuration);
				list.resetCriticalPath();
				String consume = list.printTree();*/
			}


			//-----------------------------------------
			// Create File
			//-----------------------------------------
			if(e.getSource() == createFile) {
				if(list.getRoot() != null)
				{
					///////////////////////////////////////////////////////// <<<---- POPUP FOR filename

					//-------------------------------------
					// Adds Title to Data String
					//-------------------------------------
					String fileString = JOptionPane.showInputDialog(wholePanel, "Enter file name:", "Create a file", JOptionPane.WARNING_MESSAGE);
					if(!fileString.equals(""))
					{
						String data = "";
						String filename = fileString;	// <<<---name will go here;
						filename += ".txt";
						data += "Team Project: Group N_T9" + "\n";

						//-------------------------------------
						// Adds Date-Time to Data String
						//-------------------------------------
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
						LocalDateTime now = LocalDateTime.now();
						String currentDateTime = dtf.format(now);
						data += "Created On: " + currentDateTime + "\n";

						//-------------------------------------
						//	Adds List of Activities in
						//	Alphanumeric Order to Data String
						//-------------------------------------
						data += "\n------------------------------------------------------------------------\n"
								+ "ACTIVITIES:" + "\n------------------------------------------------------------------------\n";
						data += list.allActivites() + "\n";

						//-------------------------------------
						//	Adds List Paths to Data String
						//-------------------------------------
						data += "\n------------------------------------------------------------------------\n"
								+ "PATHS:" + "\n------------------------------------------------------------------------\n";
						data += list.getPathsList();

						//-------------------------------------
						//	Creates File on the Desktop
						//-------------------------------------
						String userHomeFolder = System.getProperty("user.home");
						String Desktop = userHomeFolder + "/desktop";
						File textFile = new File(Desktop, filename);

						BufferedWriter out;

						try {
							out = new BufferedWriter(new FileWriter(textFile));

							String[] lines = data.split("\n");
							for (int i=0; i<lines.length; i++) {
								out.write(lines[i]);
								out.newLine();
							}
							out.close();
							msgLabel.setText("File created.");
							msgLabel.setForeground(Color.blue);
							msgLabel.setVisible(true);
						}
						catch(IOException error) {
							msgLabel.setText("ERROR: File Data Not Added.");
							msgLabel.setForeground(Color.red);
							msgLabel.setVisible(true);
						}
					}
					else
					{
						msgLabel.setText("File name input was empty");
						msgLabel.setForeground(Color.red);
						msgLabel.setVisible(true);
					}
				}
				else
				{
					msgLabel.setText("You must process list before creating a file");
					msgLabel.setForeground(Color.red);
					msgLabel.setVisible(true);
				}
			}


			//----------------------------------------
			// Restarts the LinkedTree
			//-----------------------------------------
			if(e.getSource() == restart)
			{
				list = new LinkedTree();
				activityField.setText("");
				durationField.setText("");
				dependField.setText("");
				output.setText("");
				msgLabel.setText("Input Cleared. Start with New Input.");
				msgLabel.setForeground(Color.blue);
				msgLabel.setVisible(true);
			}

			//-----------------------------------------
			// Closes the Application
			//-----------------------------------------
			if(e.getSource() == quit)
			{
				System.exit(0);
			}

			//-----------------------------------------
			// Displays the "About" Information
			//-----------------------------------------
			if(e.getSource() == about)
			{
				String aboutInfo = "This application was created by Steven Haut and Cole Ruter for CSE 360.\n\n"
						+ "This program accepts an unlimited amount of activites with durations and connected based on dependencies. "
						+ "Then the program computes every path and shows the duration of the entire path in decending order.\n\n"
						+ "From there it can calculate the critical path based on the durations, change the duration for an activity, or create a file"
						+ " that lists all the activities, each activity's duration, and all the paths with its total duration.";

				if(output.getText().equals(aboutInfo)) {
					output.setText("");
				}
				else {
					output.setText(aboutInfo);
				}
				msgLabel.setVisible(false);
			}

			//-----------------------------------------
			// Displays the "Help" Information
			//-----------------------------------------
			if(e.getSource() == help)
			{
				String helpInfo = "About: Displays information about the program.\n\n"
						+ "Help: Displays information to help the user.\n\n"
						+ "Add: Stores the activity information in the text fields to the activity network.\n\n"
						+ "Process: Determines every path and path duration in the activty network and displays it.\n\n"
						+ "Critical Path: Displays all the paths with the longest duration.\n\n"
						+ "Change Duration: Changes the duration for a given activity.\n\n"
						+ "Create File: Creates a file that lists all the activities, each activity's duration, and all the paths with its total duration.\n\n"
						+ "Restart: Clears the entire activity network.\n\n"
						+ "Quit: End the Program.\n\n"
						+ "Getting Started: Fill out all the activity information in the text fields and click the \"add\" button";

				if(output.getText().equals(helpInfo)) {
					output.setText("");
				}
				else {
					output.setText(helpInfo);
				}
				msgLabel.setVisible(false);
			}
		}
	}
}
