import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Random;

class Directed_Graph extends Graph {
    private ArrayDeque<Vertex>[] external_adjacency_list;
    private ArrayDeque<Vertex>[] internal_adjacency_list;
    private ArrayDeque<ArrayDeque<Vertex>> graph_SCCs;
    private ArrayDeque<HashSet<Vertex>> hashed_SCCs;
    int edges_quantity;

    Directed_Graph(int vertices_quantity) {
        this.vertices_quantity = vertices_quantity;
        System.out.println("Vertices quantity: " + this.vertices_quantity);

        external_adjacency_list = new ArrayDeque[this.vertices_quantity];
        internal_adjacency_list = new ArrayDeque[this.vertices_quantity];
        for (int i = 0; i < this.vertices_quantity; i++) {
            external_adjacency_list[i] = new ArrayDeque<>();
            internal_adjacency_list[i] = new ArrayDeque<>();

            Vertex vertex = new Vertex(i);
            external_adjacency_list[i].add(vertex);
            internal_adjacency_list[i].add(vertex);
        }

        edges_quantity = 0;
        generate_random_edges();
    }

    protected void generate_random_edges() { // Cannot reduce invisibility!
        Random random = new Random();

        System.out.println("Edges:");
        for (int i = 0; i < external_adjacency_list.length; i++) {
            for (int j = 0; j < external_adjacency_list.length; j++) {
                if (i != j) {
                    if (random.nextFloat() > 0.5) {
                        external_adjacency_list[i].add(external_adjacency_list[j].getFirst());
                        internal_adjacency_list[j].add(external_adjacency_list[i].getFirst());
                        edges_quantity++;
                        System.out.print(i + "-->" + j + "\t");
                    }
                }
            }
            System.out.println();
        }
    }

    ArrayDeque<ArrayDeque<Vertex>> print_SCCs_tarjan() {
        set_SCCs_by_tarjan();
        System.out.println("Strongly connected components (Tarjan):");
        for (ArrayDeque<Vertex> strongly_connected_component : graph_SCCs) {
            for (Vertex vertex : strongly_connected_component) {
                System.out.print(vertex.id + " ");
            }
            System.out.println();
        }
        return graph_SCCs;
    }

    int set_SCCs_by_tarjan() {
        graph_SCCs = new ArrayDeque<ArrayDeque<Vertex>>();
        ArrayDeque<Vertex> stack = new ArrayDeque<Vertex>();
        for (ArrayDeque<Vertex> graph_SCC : graph_SCCs) {
            graph_SCC = new ArrayDeque<>();
        }

        long start_time = System.nanoTime();

        int preorder_counter = 0;
        for (ArrayDeque<Vertex> deque : external_adjacency_list) {
            if (!deque.getFirst().visited) {
                do_modified_DFS(deque.getFirst().id, stack, preorder_counter);
            }
        }
        return (int) ((System.nanoTime() - start_time) / 1000);
    }

    private void do_modified_DFS(int vertex_id, ArrayDeque<Vertex> stack, int preorder_counter) {

        external_adjacency_list[vertex_id].getFirst().low = preorder_counter++;
        external_adjacency_list[vertex_id].getFirst().visited = true;
        stack.push(external_adjacency_list[vertex_id].getFirst());
        int min = external_adjacency_list[vertex_id].getFirst().low;

        for (Vertex neighbor_vertex : external_adjacency_list[vertex_id]) {
            if (neighbor_vertex != external_adjacency_list[vertex_id].getFirst()) { // When ArrayDeque sucks and you can
                                                                                    // do nothing...!
                if (!neighbor_vertex.visited) {
                    do_modified_DFS(neighbor_vertex.id, stack, preorder_counter);
                }
                if (external_adjacency_list[neighbor_vertex.id].getFirst().low < min) {
                    min = external_adjacency_list[neighbor_vertex.id].getFirst().low;
                }
            }
        }

        if (min < external_adjacency_list[vertex_id].getFirst().low) {
            external_adjacency_list[vertex_id].getFirst().low = min;
            return;
        }

        ArrayDeque<Vertex> component = new ArrayDeque<Vertex>();
        Vertex vertex;

        do {
            vertex = stack.pop();
            component.add(vertex);
            vertex.low = external_adjacency_list.length;
        } while (vertex.id != vertex_id);

        graph_SCCs.add(component);
    }

    ArrayDeque<HashSet<Vertex>> print_SCCs_BFS() {
        set_SCCs_by_BFS();
        System.out.println("Strongly connected components (BFS):");
        for (HashSet<Vertex> strongly_connected_component : hashed_SCCs) {
            for (Vertex vertex : strongly_connected_component) {
                System.out.print(vertex.id + " ");
            }
            System.out.println();
        }
        return hashed_SCCs;
    }

    int set_SCCs_by_BFS() {
        hashed_SCCs = new ArrayDeque<HashSet<Vertex>>();

        long start_time = System.nanoTime();

        for (ArrayDeque<Vertex> vertices : external_adjacency_list) {
            boolean visited = false;
            for (HashSet<Vertex> an_SCC : hashed_SCCs) {
                if (an_SCC.contains(vertices.getFirst())) {
                    visited = true;
                    break;
                }
            }
            if (!visited) {
                hashed_SCCs.add(get_common_vertices(do_extended_BFS(vertices.getFirst().id, false),
                        do_extended_BFS(vertices.getFirst().id, true)));
            }
        }
        return (int) ((System.nanoTime() - start_time) / 1000);
    }

    private HashSet<Vertex> do_extended_BFS(int vertex_id, boolean reverse_traverse) {
        ArrayDeque<Vertex>[] target_AL;
        if (!reverse_traverse) {
            target_AL = external_adjacency_list;
        } else {
            target_AL = internal_adjacency_list;
        }

        HashSet<Vertex> result_BFS = new HashSet<Vertex>();
        ArrayDeque<Vertex> deque = new ArrayDeque<Vertex>();

        deque.addLast(target_AL[vertex_id].getFirst());
        target_AL[vertex_id].getFirst().visited = true;

        while (!deque.isEmpty()) {
            Vertex vertex = deque.pollFirst();
            result_BFS.add(vertex);

            for (Vertex v : target_AL[vertex.id]) {
                if (!v.visited) {
                    v.visited = true;
                    deque.addLast(v);
                }
            }
        }
        reset_vertices();
        return result_BFS;
    }

    private HashSet<Vertex> get_common_vertices(HashSet<Vertex> neighbors, HashSet<Vertex> reversed_neighbors) {
        HashSet<Vertex> common_vertices = new HashSet<>();
        for (Vertex vertex : neighbors) {
            if (reversed_neighbors.contains(vertex)) {
                common_vertices.add(vertex);
            }
        }
        return common_vertices;
    }

    void reset_vertices() {
        for (ArrayDeque<Vertex> vertices : external_adjacency_list) {
            vertices.getFirst().reset_vertex();
        }
    }
}
