package cons;

@SuppressWarnings("unchecked")
public class ListGraph<T> extends Conslist<T> {
	T head;
	Conslist<T> tail;

	// Creating a Nil object extending ConsList
	public final static Conslist<Object> Nil = new Conslist<Object>() {

		@Override
		public Object head() {
			// return Nil;
			throw new NullPointerException("Nil has no head");
		}

		@Override
		public Conslist<Object> tail() {
			return Nil; // return Nil
		}

	};

	public ListGraph(T head, Conslist<T> tail) {
		this.head = head;
		this.tail = tail;
	}

	public static <T> Conslist<T> lists(T... lists) {
		// return new Cons(lists[0],make(1, lists));
		return (Conslist<T>) make_tr(Nil, lists.length - 1, lists);
	}

	// A tail recursive function
	private static <T> Conslist<T> make_tr(Conslist<T> a, int i, T... lists) {
		if (i < 0) {
			return a;
		} else {
			return make_tr(new Cons<T>(lists[i], a), --i, lists);
		}
	}

	public T head() {
		return head;
	}

	public Conslist<T> tail() {
		return tail;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
	

	@Override
	public void print() {
		System.out.print(head + " ");
		// Tail is also a conslist so it keeps recursive.
		tail.print();
	}

	public void printnest() {
		// Type-casting 'this' to a nested list
		Conslist<Conslist<T>> nest = (Conslist<Conslist<T>>) this;
		// printing each row
		nest.head().print();
		System.out.println();
		// recursively printing the tail
		if (nest.tail() != (Conslist<Conslist<T>>) (T) Nil)
			nest.tail().printnest();

	}

	// printing specific rows
	public <U> void print_row(int start) {
		// Type casting to nested list containing KVPair
		Conslist<KVPair<T, U>> nest = (Conslist<KVPair<T, U>>) this;
		// printing key and values
		System.out.print(start + " -> " + nest.head().key + "(" + nest.head().value + ")" + "\t");
		if (nest.tail() != (T) Nil)
			nest.tail().print_row(start);
	}

	// A function to print the nested list in graph form
	public <U> void print_listgraph() {
		// Type casting to nested list containing KVPair
		Conslist<Conslist<KVPair<T, U>>> nest = (Conslist<Conslist<KVPair<T, U>>>) this;
		int i = 0;
		// calling the print row function for each row (conslist)
		while (nest != (T) Nil) {
			nest.head().print_row(i);
			nest = nest.tail();
			System.out.println();
			i++;
		}

	}

	@SuppressWarnings("rawtypes")

	// To convert matrix graph to listgraph
	public static <T> Conslist<Conslist<T>> matrixtolist(int[][] matrix) {
		// Type-casting to nested Conslist
		Conslist<Conslist<T>> listgraph = (Conslist<Conslist<T>>) (T) Nil;
		// Inserting the elements in reverse
		for (int i = matrix.length - 1; i >= 0; i--) {
			Conslist<T> row = (Conslist<T>) Nil;
			for (int j = matrix.length - 1; j >= 0; j--) {
				// Key is end vertex value is edge weight
				KVPair<Integer, Integer> b = new KVPair<Integer, Integer>(j, matrix[i][j]);
				row = new ListGraph(b, row);
			}
			// adding rows to the nested list
			listgraph = new ListGraph(row, listgraph);
		}
		return listgraph;
	}

	public int length() {
		return tail.length() + 1;
	}

	// getting the row given row number
	public static <T> Conslist<T> getrow(Conslist<Conslist<T>> nest, int ind) {
		int i = 0;
		Conslist<T> row = nest.head();
		// iterating through the rows
		while (i != ind) {
			nest = nest.tail();
			row = nest.head();
			i++;
		}
		return row;
	}

	// finding the element given row and column
	public static <T, U> int getelement(Conslist<Conslist<KVPair<T, U>>> nest, int m, int n) {
		// getting the row conslist
		Conslist<KVPair<T, U>> row = getrow(nest, m);

		// accessing the key for column and value for weight
		int col = (int) row.head().key;
		int val = (int) row.head().value;
		// iterating through columns
		while (col != n) {
			row = row.tail();
			col = (int) row.head().key;
			val = (int) row.head().value;
		}
		return val;
	}

	// Returns the index with minimum distance
	public static int minimize(int[] cost, boolean[] taken, int V) {
		int minIndex = -1, minValue = Integer.MAX_VALUE;
		for (int i = 0; i < V; i++) {
			if (!taken[i] && cost[i] < minValue) {
				minValue = cost[i];
				minIndex = i;
			}
		}
		return minIndex;
	}

	public int[] dijkstras(int src) {
		// Type-casting to nested conslist of KVPair objects
		Conslist<Conslist<KVPair<Integer, Integer>>> graph = 
				(Conslist<Conslist<KVPair<Integer, Integer>>>) this;
		// Number of vertices
		int V = graph.length();
		int[] dist = new int[V];
		boolean[] taken = new boolean[V];
		// all distances are initially infinite
		for (int i = 0; i < V; i++) {
			dist[i] = Integer.MAX_VALUE;
			taken[i] = false;
		}
		// distance of the source vertex = 0
		dist[src] = 0;
		// V-1 vertices gave to be processed
		for (int i = 0; i < V - 1; i++) {
			// finding node with minimum distance
			int u = minimize(dist, taken, V);
			taken[u] = true;
			// finding adjacent vertices of u and updating distances
			for (int v = 0; v < V; v++) {
				// if v is adjacent to u and not taken yet
				if (getelement(graph, u, v) != 0 && !taken[v]) {
					// update if cost to v from u is < existing cost
					if (dist[v] > getelement(graph, u, v) + dist[u]) {
						dist[v] = getelement(graph, u, v) + dist[u];
					}
				}
			}
		}
		return dist;

	}

	public static void main(String[] args) {
		int[][] adMatrix = { 
				{ 0, 4, 9, 0, 7, 0 }, 
				{ 4, 0, 8, 0, 5, 0 }, 
				{ 9, 8, 0, 7, 0, 4 }, 
				{ 0, 0, 7, 0, 9, 14 },
				{ 7, 5, 0, 9, 0, 10 }, 
				{ 0, 0, 4, 14, 10, 0 } 
				};
		/*KVPair b = new KVPair(1,2);
		KVPair b1 = new KVPair(1,3);
		KVPair b2 = new KVPair(1,4);
		Conslist<Conslist<KVPair<Integer, Integer>>> listgraph1 = Cons.lists(Cons.lists(b,b1,b2),Cons.lists(b,b1,b2));
		listgraph1.print_listgraph();*/
		Conslist<Conslist<KVPair<Integer, Integer>>> listgraph = matrixtolist(adMatrix);
		System.out.println("The Matrix Graph converted into List Graph");
		System.out.println();
		listgraph.print_listgraph();
		System.out.println();
		int src = 0;
		System.out.println("The shortest distance of nodes from the node " + src + " are");
		System.out.println();
		int[] mst = listgraph.dijkstras(src);
		System.out.println("Vertex" + "\t\t" + "Distance from source");
		for (int i = 0; i < mst.length; i++) {
			System.out.println(i + "\t\t" + mst[i]);
		}
	}
}
