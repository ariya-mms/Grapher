import java.util.ArrayDeque;
import java.util.Random;

class Undirected_Graph extends Graph {
    private ArrayDeque<Vertex>[] adjacency_list;
    private ArrayDeque<ArrayDeque<Vertex>> connected_components;
    private Vertex[] max_independent_set;

    Undirected_Graph(int vertices_quantity) {
        this.vertices_quantity = vertices_quantity;
        System.out.println("Vertices quantity: " + this.vertices_quantity);

        adjacency_list = new ArrayDeque[this.vertices_quantity];
        for (int i = 0; i < this.vertices_quantity; i++) {
            adjacency_list[i] = new ArrayDeque<>();
            adjacency_list[i].add(new Vertex(i));
        }

        generate_random_edges();
    }

    protected void generate_random_edges() {
        Random random = new Random();

        System.out.println("Edges:");
        for (int i = 0; i < adjacency_list.length; i++) {
            for (int j = i + 1; j < adjacency_list.length; j++) {
                if (random.nextFloat() > 0.70) {
                    adjacency_list[i].add(adjacency_list[j].getFirst());
                    adjacency_list[j].add(adjacency_list[i].getFirst());
                    System.out.print(i + "-" + j + "\t");
                }
            }
            System.out.println();
        }
    }

    void print_connected_components() {
        set_connected_components();
        System.out.println("Connected components:");
        for (ArrayDeque<Vertex> connected_component : connected_components) {
            for (Vertex vertex : connected_component) {
                System.out.print(vertex.id + " ");
            }
            System.out.println();
        }
    }

    private void set_connected_components() {
        connected_components = new ArrayDeque<ArrayDeque<Vertex>>();

        for (int i = 0; i < adjacency_list.length; i++) {
            if (!adjacency_list[i].getFirst().visited) {
                connected_components.add(do_BFS(i));
            }
        }
    }

    private ArrayDeque<Vertex> do_BFS(int vertex_id) {
        if (!adjacency_list[vertex_id].getFirst().visited) {
            ArrayDeque<Vertex> result_BFS = new ArrayDeque<>();
            ArrayDeque<Vertex> deque = new ArrayDeque<>();

            deque.addLast(adjacency_list[vertex_id].getFirst());
            adjacency_list[vertex_id].getFirst().visited = true;

            while (!deque.isEmpty()) {
                Vertex vertex = deque.pollFirst();
                result_BFS.add(vertex);

                for (Vertex vertex2 : adjacency_list[vertex.id]) {
                    if (!vertex2.visited) {
                        vertex2.visited = true;
                        deque.addLast(vertex2);
                    }
                }
            }
            return result_BFS;
        }
        return null;
    }

    void print_max_independent_set() {
        set_max_independent_set();
        System.out.println("Maximum independent set:");
        for (Vertex vertex : max_independent_set) {
            System.out.print(vertex.id + " ");
        }
    }

    private void set_max_independent_set() {
        for (int i = adjacency_list.length; i > 0; i--) {
            ArrayDeque<Vertex[]> sets = Combination.printCombination(adjacency_list, vertices_quantity, i);
            // System.out.println(i);
            for (Vertex[] set : sets) {
                // for (Vertex vertex : set) {
                // System.out.print(i + " " + vertex.id + " ");
                // }
                if (is_independent(set)) {
                    max_independent_set = set;
                    return;
                }
            }
            // System.out.println();
        }
        return;
    }

    // // The main function that prints all combinations of size r
    // // in arr[] of size n. This function mainly uses combinationUtil()
    // private ArrayDeque<Vertex[]> combine(int n, int r) {
    // ArrayDeque<Vertex[]> sets = new ArrayDeque<>();

    // // A temporary array to store all combination one by one
    // Vertex[] data = new Vertex[r];

    // // Print all combination using temprary array 'data[]'
    // combinationUtil(n, r, sets, data, 0, 0);
    // return sets;
    // }

    // /*
    // * arr[] ---> Input Array
    // * data[] ---> Temporary array to store current combination
    // * start & end ---> Staring and Ending indexes in arr[]
    // * index ---> Current index in data[]
    // * r ---> Size of a combination to be printed
    // */
    // private void combinationUtil(int n, int r, ArrayDeque<Vertex[]> set, Vertex[]
    // data, int index, int i) {
    // // Current combination is ready to be printed, print it
    // if (index == r) {
    // set.add(data);
    // // for (int j = 0; j < r; j++)
    // // System.out.print(data[j] + " ");

    // // System.out.println("");
    // return;
    // }

    // // When no more elements are there to put in data[]
    // if (i >= n)
    // return;

    // // current is included, put next at next location
    // data[index] = adjacency_list[i].getFirst();
    // combinationUtil(n, r, set, data, index + 1, i + 1);

    // // current is excluded, replace it with next (Note that
    // // i+1 is passed, but index is not changed)
    // combinationUtil(n, r, set, data, index, i + 1);
    // }

    private boolean is_independent(Vertex[] set) {
        boolean indepndency = true;

        for (int i = 0; i < set.length; i++) {
            for (int j = i + 1; j < set.length; j++) {
                if (adjacency_list[set[i].id].contains(adjacency_list[set[j].id].getFirst())) {
                    indepndency = false;
                    return indepndency;
                }
            }
        }
        return indepndency;
    }

    void reset_vertices() {
        for (ArrayDeque<Vertex> vertices : adjacency_list) {
            vertices.getFirst().reset_vertex();
        }
    }
}
