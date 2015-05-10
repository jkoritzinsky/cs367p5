import java.io.StringWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;


public class SocialGraph extends UndirectedGraph<String> {
    /**
     * Creates an empty social graph.
     * 
     * DO NOT MODIFY THIS CONSTRUCTOR.
     */
    public SocialGraph() {
        super();
    }

    /**
     * Creates a graph from a preconstructed hashmap.
     * This method will be used to test your submissions. You will not find this
     * in a regular ADT.
     * 
     * DO NOT MODIFY THIS CONSTRUCTOR.
     * DO NOT CALL THIS CONSTRUCTOR ANYWHERE IN YOUR SUBMISSION.
     * 
     * @param hashmap adjacency lists representation of a graph that has no
     * loops and is not a multigraph.
     */
    public SocialGraph(HashMap<String, ArrayList<String>> hashmap) {
        super(hashmap);
    }
    
    public Set<String> friendsOfFriends(String person) {
    	if(person == null) throw new IllegalArgumentException("person");
    	if(!hashmap.containsKey(person)) throw new IllegalArgumentException("person");
        ArrayList<String> friends = hashmap.get(person);
        return friends.stream().flatMap(friend -> hashmap.get(friend).stream())
        						.filter(fof -> !friends.contains(fof) && !fof.equals(person))
        						.collect(Collectors.toCollection(HashSet<String>::new));
    }

    public List<String> getPathBetween(String pFrom, String pTo) {
    	if(pFrom == null) throw new IllegalArgumentException("pFrom");
    	if(pTo == null) throw new IllegalArgumentException("pTo");
    	if(!hashmap.containsKey(pFrom)) throw new IllegalArgumentException("pFrom");
    	if(!hashmap.containsKey(pTo)) throw new IllegalArgumentException("pTo");
    	if(pFrom.equals(pTo)) throw new IllegalArgumentException();
        // Find the shortest path using Djikstra's Algorithm
    	class DijkstraRecord {
    		public boolean visited = false;
    		public int weight = Integer.MAX_VALUE;
    		public String predecessor = null;
    	}
    	// Transform Node -> Node * <Visited * Weight * Predecessor> for node tracking
    	Map<String, DijkstraRecord> dijkstraRecords = hashmap.keySet().stream().collect(Collectors.toMap(node -> node, node -> new DijkstraRecord()));
    	dijkstraRecords.get(pFrom).weight = 0;
    	// Use separate class for the priority queue because we need <Node * Weight>, and the DijkstraRecord class is built to not store the node name
    	class PriorityQueueRecord {
    		public PriorityQueueRecord(String node, int weight) {
				this.node = node;
				this.weight = weight;
			}
			public String node;
    		public int weight;
    	}
    	PriorityQueue<PriorityQueueRecord> queue = new PriorityQueue<>(Comparator.comparingInt(record -> record.weight));
    	queue.add(new PriorityQueueRecord(pFrom, 0));
    	while(!queue.isEmpty()) {
    		PriorityQueueRecord queueRecord = queue.remove();
    		dijkstraRecords.get(queueRecord.node).visited = true;
    		hashmap.get(queueRecord.node).stream().filter(friend -> !dijkstraRecords.get(friend).visited)
    										 .forEach(friend -> {
    											 DijkstraRecord friendRecord = dijkstraRecords.get(friend);
    											 // All edge weights are 1
    											 if(friendRecord.weight > queueRecord.weight + 1){
    												 friendRecord.weight = queueRecord.weight + 1;
    												 friendRecord.predecessor = queueRecord.node;
    												 // Update record in priority queue by removing and re-adding updated version
    												 queue.removeIf(item -> item.node.equals(friend));
    												 queue.add(new PriorityQueueRecord(friend, queueRecord.weight + 1));
    											 }
    										 });
    	}
    	ArrayDeque<String> path = new ArrayDeque<>();
    	DijkstraRecord record = dijkstraRecords.get(pTo);
    	path.addFirst(pTo);
    	while(record.predecessor != null) {
    		path.addFirst(record.predecessor);
    		record = dijkstraRecords.get(record.predecessor);
    	}
    	if(!path.peekFirst().equals(pFrom)) {
    		return null;
    	}
        return new ArrayList<String>(path);
    }

    /**
     * Returns a pretty-print of this graph in adjacency matrix form.
     * People are sorted in alphabetical order, "X" denotes friendship.
     * 
     * This method has been written for your convenience (e.g., for debugging).
     * You are free to modify it or remove the method entirely.
     * THIS METHOD WILL NOT BE PART OF GRADING.
     *
     * NOTE: this method assumes that the internal hashmap is valid (e.g., no
     * loop, graph is not a multigraph). USE IT AT YOUR OWN RISK.
     *
     * @return pretty-print of this graph
     */
    public String pprint() {
        // Get alphabetical list of people, for prettiness
        List<String> people = new ArrayList<String>(this.hashmap.keySet());
        Collections.sort(people);

        // String writer is easier than appending tons of strings
        StringWriter writer = new StringWriter();

        // Print labels for matrix columns
        writer.append("   ");
        for (String person: people)
            writer.append(" " + person);
        writer.append("\n");

        // Print one line of social connections for each person
        for (String source: people) {
            writer.append(source);
            for (String target: people) {
                if (this.getNeighbors(source).contains(target))
                    writer.append("  X ");
                else
                    writer.append("    ");
            }
            writer.append("\n");

        }

        // Remove last newline so that multiple printlns don't have empty
        // lines in between
        String string = writer.toString();
        return string.substring(0, string.length() - 1);
    }

}

