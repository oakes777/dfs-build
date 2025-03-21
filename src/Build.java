import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Build {

  /**
   * Prints words that are reachable from the given vertex and are strictly
   * shorter than k characters.
   * If the vertex is null or no reachable words meet the criteria, prints
   * nothing.
   *
   * @param vertex the starting vertex
   * @param k      the maximum word length (exclusive)
   */
  public static void printShortWords(Vertex<String> vertex, int k) {
    // base case
    if (vertex == null)
      return;
    // instantiate visited Set to avoid cycling
    Set<Vertex<String>> visited = new HashSet<>();
    // call helper method to run algorithm
    printShortWordsHelper(vertex, k, visited);
  }

  private static void printShortWordsHelper(Vertex<String> current, int k, Set<Vertex<String>> visited) {
    // base case -
    if (current == null)
      return;
    // if current already visited immediately return
    if (visited.contains(current))
      return;
    // add current vertex to visited Set tracker
    visited.add(current);
    // print current 'root' if it's shorter than k
    if (current.data != null && current.data.length() < k) {
      System.out.println(current.data);
    }
    if (current.neighbors != null) {
      for (Vertex<String> neighbor : current.neighbors) {
        printShortWordsHelper(neighbor, k, visited);
      }
    }
  }

  /**
   * Returns the longest word reachable from the given vertex, including its own
   * value.
   *
   * @param vertex the starting vertex
   * @return the longest reachable word, or an empty string if the vertex is null
   */
  public static String longestWord(Vertex<String> vertex) {
    if (vertex == null)
      return "";
    Set<Vertex<String>> visited = new HashSet<>();
    return longestWordHelper(vertex, visited);
  }

  public static String longestWordHelper(Vertex<String> current, Set<Vertex<String>> visited) {
    // base case
    if (current == null || visited.contains(current)) return "";
    // ternary op ensures non-null value
    String longest = (current.data != null) ? current.data : ""; 
    //track where we've been to avoid looping
    visited.add(current);

    for (Vertex<String> neighbor : current.neighbors) {
      String candidate = longestWordHelper(neighbor, visited);
      if (candidate.length() > longest.length()) {
        longest = candidate;
      }
    }
    return longest;
  }

  /**
   * Prints the values of all vertices that are reachable from the given vertex
   * and
   * have themself as a neighbor.
   *
   * @param vertex the starting vertex
   * @param <T>    the type of values stored in the vertices
   */
  public static <T> void printSelfLoopers(Vertex<T> vertex) {
    if (vertex == null)
      return;
    Set<Vertex<T>> visited = new HashSet<>();
    printSelfLoopersHelper(vertex, visited);
  }

  public static <T> void printSelfLoopersHelper(Vertex<T> current, Set<Vertex<T>> visited) {
    if (current == null || visited.contains(current))
      return;
    visited.add(current);

    //if one of current's neighbors IS current 
    //that means it self-loops -> print it out!
    if (current.neighbors != null && current.neighbors.contains(current)) {
      System.out.println(current.data);
    }

    //for each iteration and recurse neighbors
    for (Vertex<T> neighbor : current.neighbors) {
      printSelfLoopersHelper(neighbor, visited);
    }
  }

  /**
   * Determines whether it is possible to reach the destination airport through a
   * series of flights
   * starting from the given airport. If the start and destination airports are
   * the same, returns true.
   *
   * @param start       the starting airport
   * @param destination the destination airport
   * @return true if the destination is reachable from the start, false otherwise
   */
  public static boolean canReach(Airport start, Airport destination) {
    if (start == null || destination == null) return false;
    Set<Airport> visited = new HashSet<>();

    return canReachHelper(start, destination, visited);
  }
  private static boolean canReachHelper(Airport current, Airport destination, Set<Airport> visited) {
    //base case if we've reached the destination in question return true
    if (current == destination) return true;
    //
    if (visited.contains(current)) return false;
    visited.add(current);

    for (Airport connection : current.getOutboundFlights()) {
      if (canReachHelper(connection, destination, visited)) return true;
    }
    return false;
  }

  /**
   * Returns the set of all values in the graph that cannot be reached from the
   * given starting value.
   * The graph is represented as a map where each vertex is associated with a list
   * of its neighboring values.
   *
   * @param graph    the graph represented as a map of vertices to neighbors
   * @param starting the starting value
   * @param <T>      the type of values stored in the graph
   * @return a set of values that cannot be reached from the starting value
   */
  public static <T> Set<T> unreachable(Map<T, List<T>> graph, T starting) {
    if (graph == null || starting == null) return new HashSet<>();
    Set<T> visited = new HashSet<>();

    unreachableHelper(graph, starting, visited);

    Set<T> result = new HashSet<>(graph.keySet());
    result.removeAll(visited);
    return result;
  }

  private static <T> void unreachableHelper(Map<T, List<T>> graph, T current, Set<T> visited) {
    //base cases
    if (current == null || visited.contains(current)) return;
    //add current to visited to avoid looping
    visited.add(current);
    //iterate and recurse
    //getOrDefault avoids nullPointerException here
    for (T neighbor : graph.getOrDefault(current, new ArrayList<>())) {
      unreachableHelper(graph, neighbor, visited);
    }
  }
}
