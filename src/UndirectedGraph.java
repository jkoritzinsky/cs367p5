///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  SocialNetworkingApp
// File:             scr
// Semester:         CS367 Spring 2015
//
// Author:           Jeremy Koritzinsky
// CS Login:         koritzinsky
// Lecturer's Name:  Skrentny
// Lab Section:      002
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
//
// Pair Partner:     Jeffrey Tucker
// Email:            jetucker@wisc.edu	
// CS Login:         jtucker
// Lecturer's Name:  Skrentny
// Lab Section:      002
//
//////////////////// STUDENTS WHO GET HELP FROM OTHER THAN THEIR PARTNER //////
//                   fully acknowledge and credit all sources of help,
//                   other than Instructors and TAs.
//
// Persons:          Identify persons by name, relationship to you, and email.
//                   Describe in detail the the ideas and help they provided.
//
// Online sources:   avoid web searches to solve your problems, but if you do
//                   search, be sure to include Web URLs and description of 
//                   of any information you find.
//////////////////////////// 80 columns wide //////////////////////////////////
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UndirectedGraph<V> implements GraphADT<V>{

	// Stores the vertices of this graph, and their adjacency lists.
	// It's protected rather than private so that subclasses can access it.
    protected HashMap<V, ArrayList<V>> hashmap;

    public UndirectedGraph() {
        this.hashmap = new HashMap<V, ArrayList<V>>();
    }

    public UndirectedGraph(HashMap<V, ArrayList<V>> hashmap) {
        if (hashmap == null) throw new IllegalArgumentException();
        this.hashmap = hashmap;
    }
    /**
     * Adds a vertex
     *
     * <p>Bugs: None known
     *
     * @author Jeremy Kortizinsky
     */
    @Override
    public boolean addVertex(V vertex) {
        if(vertex == null) throw new IllegalArgumentException("vertex");
        return hashmap.putIfAbsent(vertex, new ArrayList<>()) == null;
    }
    /**
     * Adds an edge between two vertices
     *
     * <p>Bugs: None known
     *
     * @author Jeremy Kortizinsky
     */
    @Override
    public boolean addEdge(V v1, V v2) {
        if(v1 == null) throw new IllegalArgumentException("v1");
        if(v2 == null) throw new IllegalArgumentException("v2");
        ArrayList<V> v1Edges = hashmap.get(v1);
        if(v1Edges == null) throw new IllegalArgumentException("v1");
        ArrayList<V> v2Edges = hashmap.get(v2);
        if(v2Edges == null) throw new IllegalArgumentException("v2");
        if(v1.equals(v2)) return false;
        if(v1Edges.contains(v2)) {
        	return false;
        }
        else {
        	v1Edges.add(v2);
        	v2Edges.add(v1);
        	return true;
        }
    }
    /**
     * Returns the vertices next to a specified vertex
     *
     * <p>Bugs: None known
     *
     * @author Jeremy Kortizinsky
     */
    @Override
    public Set<V> getNeighbors(V vertex) {
    	if(vertex == null) throw new IllegalArgumentException("vertex");
        return new HashSet<V>(hashmap.get(vertex));
    }
    /**
     * Removes the edge between two vertices
     *
     * <p>Bugs: None known
     *
     * @author Jeremy Kortizinsky
     */
    @Override
    public void removeEdge(V v1, V v2) {
        if(v1 == null) throw new IllegalArgumentException("v1");
        if(v2 == null) throw new IllegalArgumentException("v2");
        hashmap.get(v1).remove(v2);
        hashmap.get(v2).remove(v1);
    }
    /**
     * Gets all vertices in the map
     *
     * <p>Bugs: None known
     *
     * @author Jeremy Kortizinsky
     */
    @Override
    public Set<V> getAllVertices() {
        return hashmap.keySet();
    }

    /* (non-Javadoc)
     * Returns a print of this graph in adjacency lists form.
     * 
     * This method has been written for your convenience (e.g., for debugging).
     * You are free to modify it or remove the method entirely.
     * THIS METHOD WILL NOT BE PART OF GRADING.
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringWriter writer = new StringWriter();
        for (V vertex: this.hashmap.keySet()) {
            writer.append(vertex + " -> " + hashmap.get(vertex) + "\n");
        }
        return writer.toString();
    }

}

