//****************************************************************************
//		   File : Project.java
//		   Group: N_T9
//         Names: Steven Haut
//				  Cole Ruter
//
// Description: Used to Run the Program. Can be ran as a Java Application or
//				as a JApplet.
//****************************************************************************
import java.awt.Font;
import javax.swing.*;

//--------------------------------------------------------
// Project Class
//--------------------------------------------------------
public class Project extends JApplet
{
	private ProjectPanel projectPanel;

	//--------------------------------------------------------
	// To Run program as a Java Application
	//--------------------------------------------------------
	public static void main(String[] args) {
        JFrame window = new JFrame("Project");
        ProjectPanel projPanel = new ProjectPanel();
        window.setContentPane(projPanel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
        window.setSize(700,350);
    }

	//--------------------------------------------------------
	// To Run program as a JApplet
	//--------------------------------------------------------
	public void init()
	{
		projectPanel = new ProjectPanel();
		getContentPane().add(projectPanel);
		setSize(700,350);
	}
}