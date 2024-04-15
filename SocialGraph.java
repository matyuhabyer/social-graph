import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The SocialGraph class represents a social network and provides methods to load, display, and analyze its structure.
 */
public class SocialGraph {
    private HashMap<Integer, Set<Integer>> graph; // social graph using person IDs and their friend sets

    /**
     * Constructs an empty SocialGraph object.
     */
    public SocialGraph() {
        graph = new HashMap<>();
    }

    /**
     * Loads the social graph from a file.
     *
     * @param filePath The path to the file containing social graph data.
     * @return True if the graph is successfully loaded, false otherwise.
     */
    private boolean loadGraph(String filePath) {
        try (Scanner scanner = new Scanner(new FileReader(filePath))) {
            int e = scanner.nextInt(); // Number of friendships

            graph = new HashMap<>();

            for (int i = 0; i < e; i++) {
                int a = scanner.nextInt();
                int b = scanner.nextInt();

                // Bidirectional Friendships
                graph.computeIfAbsent(a, k -> new HashSet<>()).add(b);
                graph.computeIfAbsent(b, k -> new HashSet<>()).add(a);
            }

            return true;
        } catch (IOException | NoSuchElementException | IllegalStateException | NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Displays the friend list of a person.
     *
     * @param personID The ID of the person.
     */
    public void displayFriendList(int personID) {
        if (graph.containsKey(personID)) {
            ArrayList<Integer> friendList = new ArrayList<>(graph.get(personID));
            Collections.sort(friendList);

            System.out.println("Person " + personID + " has " + friendList.size() + " friends!");
            System.out.print("List of friends: ");
            for (int i = 0; i < friendList.size(); i++) {
                System.out.print(friendList.get(i));
                if (i < friendList.size() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        } else {
            System.out.println("Person " + personID + " does not exist.");
        }
    }

    /**
     * Displays whether a connection exists between two persons.
     *
     * @param firstPerson  The ID of the first person.
     * @param secondPerson The ID of the second person.
     */
    public void displayConnection(int firstPerson, int secondPerson){
        if (!graph.containsKey(firstPerson) || !graph.containsKey(secondPerson)) {
            System.out.println("One or both persons do not exist in the dataset.");
            return;
        }

        boolean foundConnection = findConnection(firstPerson, secondPerson, new HashSet<>());
        if (foundConnection) {
            System.out.println("There is a connection from " + firstPerson + " to " + secondPerson + "!");
        } else {
            System.out.println("Cannot find a connection between " + firstPerson + " and " + secondPerson);
        }
    }

    /**
     * Recursively finds a connection between two persons in the social graph.
     *
     * @param current  The ID of the current person during the search.
     * @param target   The ID of the target person to find a connection to.
     * @param visited  A set to track visited persons during the search.
     * @return True if a connection is found, false otherwise.
     */
    private boolean findConnection(int current, int target, HashSet<Integer> visited) {
        if (current == target) {
            return true;
        }

        visited.add(current);

        if (graph.containsKey(current)) {
            for (int friend : graph.get(current)) {
                if (!visited.contains(friend)) {
                    if (findConnection(friend, target, visited)) {
                        System.out.println(current + " is friends with " + friend);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * The main method for user interaction and program execution.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        SocialGraph socialGraph = new SocialGraph();

        System.out.print("Input file path: ");
        String filePath = scanner.nextLine();

        socialGraph.loadGraph(filePath);
        System.out.println("Graph file loaded!");

        int choice;
        do{
            System.out.println("MAIN MENU");
            System.out.println("[1] Get Friend List");
            System.out.println("[2] Get Connection");
            System.out.println("[3] Exit\n");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter ID of person: ");
                    int personID = scanner.nextInt();
                    scanner.nextLine();
                    socialGraph.displayFriendList(personID);
                }
                case 2 -> {
                    System.out.print("Enter ID of first person: ");
                    int firstPersonID = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter ID of second person: ");
                    int secondPersonID = scanner.nextInt();
                    scanner.nextLine();
                    socialGraph.displayConnection(firstPersonID, secondPersonID);
                }
                case 3 -> {
                    System.out.println("Exiting the program. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please enter valid option.");
            }
        } while(true);
    }
}