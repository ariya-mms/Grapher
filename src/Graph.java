abstract class Graph {
    protected int vertices_quantity;

    protected class Vertex {
        int id;
        boolean visited;
        int low;

        protected Vertex(int id) {
            this.id = id;
            visited = false;
        }

        protected void reset_vertex() {
            visited = false;
        }
    }

    protected abstract void generate_random_edges();
}