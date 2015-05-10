///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Social Networking App
// Files:            (list of source files)
// Semester:         CS367 Spring 2015
//
// Author:           Jeremy Koritzinsky
// Email:            jeremy.koritzinsky@wisc.edu
// CS Login:         kortizinsky
// Lecturer's Name:  Skrentny
// Lab Section:      002
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
//
//                   CHECK ASSIGNMENT PAGE TO see IF PAIR-PROGRAMMING IS ALLOWED
//                   If pair programming is allowed:
//                   1. Read PAIR-PROGRAMMING policy (in cs302 policy) 
//                   2. choose a partner wisely
//                   3. REGISTER THE TEAM BEFORE YOU WORK TOGETHER 
//                      a. one partner creates the team
//                      b. the other partner must join the team
//                   4. complete this section for each program file.
//
// Pair Partner:     Jeffrey Tucker
// Email:            jetucker@wisc.edu	
// CS Login:         jtucker
// Lecturer's Name:  Skrentny
// Lab Section:      002
//
//////////////////// STUDENTS WHO GET HELP FROM OTHER THAN THEIR PARTNER //////
//                   must fully acknowledge and credit those sources of help.
//                   Instructors and TAs do not have to be credited here,
//                   but tutors, roommates, relatives, strangers, etc do.
//
// Persons:          Identify persons by name, relationship to you, and email.
//                   Describe in detail the the ideas and help they provided.
//
// Online sources:   avoid web searches to solve your problems, but if you do
//                   search, be sure to include Web URLs and description of 
//                   of any information you find.
//////////////////////////// 80 columns wide //////////////////////////////////
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;



public class SocialNetworkingApp {

	/**
	 * Returns a social network as defined in the file 'filename'.
	 * See assignment handout on the expected file format.
	 * @param filename filename of file containing social connection data
	 * @return
	 * @throws FileNotFoundException if file does not exist
	 */
	public static SocialGraph loadConnections(String filename) throws FileNotFoundException {
		try(Scanner fileScnr = new Scanner(new File(filename))) {
			String[] ln = null;
			SocialGraph newGraph = new SocialGraph();
			while(fileScnr.hasNextLine()) { // for each line in the file
				ln = fileScnr.nextLine().split(" "); // separates the names
				newGraph.addVertex(ln[0]);
				if(ln.length > 1) {
					for(int i = 1; i < ln.length; i++) {
						newGraph.addVertex(ln[i]);
						newGraph.addEdge(ln[0], ln[i]);
					}
				}
			}
			return newGraph;
		}
	}

	static Scanner stdin = new Scanner(System.in);
	static SocialGraph graph;
	static String prompt = ">> ";  // Command prompt

	/**
	 * Access main menu options to login or exit the application.
	 * 
	 * THIS METHOD HAS BEEN IMPLEMENTED FOR YOU.
	 */
	public static void enterMainMenu() {
		boolean exit = false;
		while (!exit) {
			System.out.print(prompt);
			String[] tokens = stdin.nextLine().split(" ");
			String cmd = tokens[0];
			String person = (tokens.length > 1 ? tokens[1] : null);

			switch(cmd) {
			case "login":
				System.out.println("Logged in as " + person);
				enterSubMenu(person);
				System.out.println("Logged out");
				break;
			case "exit":
				exit = true;
				break;
			default:
				System.out.println("Invalid command");
			}
		}
	}

	/**
	 * Access submenu options to view the social network from the perspective
	 * of currUser. Assumes currUser exists in the network.
	 * @param currUser
	 */
	public static void enterSubMenu(String currUser) {

		// Define the set of commands that have no arguments or one argument
		String commands =
				"friends fof logout print\n" +
						"connection friend unfriend";
		Set<String> noArgCmds = new HashSet<String>
		(Arrays.asList(commands.split("\n")[0].split(" ")));
		Set<String> oneArgCmds = new HashSet<String>
		(Arrays.asList(commands.split("\n")[1].split(" ")));

		boolean logout = false;
		while (!logout) {
			System.out.print(prompt);

			// Read user commands
			String[] tokens = stdin.nextLine().split(" ");
			String cmd = tokens[0];
			String otherPerson = (tokens.length > 1 ? tokens[1] : null);

			// Reject erroneous commands
			// You are free to do additional error checking of user input, but
			// this isn't necessary to receive a full grade.
			if (tokens.length == 0) continue;
			if (!noArgCmds.contains(cmd) && !oneArgCmds.contains(cmd)) {
				System.out.println("Invalid command");
				continue;
			}
			if (oneArgCmds.contains(cmd) && otherPerson == null) {
				System.out.println("Did not specify person");
				continue;
			}

			switch(cmd) {
			//Displays the shortest path between the current user and the specified person
			case "connection": {     
				List<String> L1 = graph.getPathBetween(currUser, otherPerson);
				if(L1 != null) {
					System.out.println(L1);
				}
				else {
					System.out.println("You are not connected to " + otherPerson);
				}
				break;
			}
			//Displays all of the degree one neighbors of the current user
			case "friends": {
				if(!graph.getNeighbors(currUser).isEmpty()) { 
					//gets all of the friends of the current user
					ArrayList<String> friends = new ArrayList<>(graph.getNeighbors(currUser));
					Collections.sort(friends); //sorts the friends into alphabetical order
					System.out.println(friends);
				}
				else {
					System.out.println("You do not have any friends"); 
				}
				break;
			}
			//Prints all of the degree 2 friends of the current user
			case "fof": { 
				Set<String> friendsOfFriends = graph.friendsOfFriends(currUser);
				 //an array to store the friends of friends
				ArrayList<String> fofArray = new ArrayList<String>(); 
				//We need this in an array instead of a set so we can sort
				fofArray.addAll(friendsOfFriends);
				Collections.sort(fofArray); //sorts the array in alphabetical order
				if(!fofArray.isEmpty()) {
					System.out.println(fofArray);
				}
				else {
					System.out.println("You do not have any friends of friends");
				}
				break;
			}
			//Adds the specified person as a friend to the current user
			case "friend": {
				if(graph.addEdge(currUser, otherPerson)) {
					System.out.println("You are now friends with " + otherPerson);
				} 
				else {
					System.out.println("You are already friends with " + otherPerson);
				}
				break;
			}
			//Removes the specified person from the current user's friends
			case "unfriend": {
				if(graph.getNeighbors(currUser).contains(otherPerson)) { //checks if they are friends
					graph.removeEdge(currUser, otherPerson);	
					System.out.println("You are no longer friends with " + otherPerson);
				}
				else {
				System.out.println("You are already not friends with " + otherPerson);
				}
				break;
			}

			case "print": {
				// This command is left here for your debugging needs.
				// You may want to call graph.toString() or graph.pprint() here
				// You are free to modify this or remove this command entirely.
				//
				// YOU DO NOT NEED TO COMPLETE THIS COMMAND
				// THIS COMMAND WILL NOT BE PART OF GRADING
				break;
			}

			case "logout":
				logout = true;
				break;
			}  // End switch
		}
	}

	/**
	 * Commandline interface for a social networking application.
	 *
	 * THIS METHOD HAS BEEN IMPLEMENTED FOR YOU.
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Usage: java SocialNetworkingApp <filename>");
			return;
		}
		try {
			graph = loadConnections(args[0]);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}

		enterMainMenu();

	}

}

