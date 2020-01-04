import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        String repition;
        do {
            System.out.println("What is your command?\n" 
                    + "Find connected components of an undirected graph. [1]\n"
                    + "Find maximum independent set of an undirected graph. [2]\n"
                    + "Find SCCs of an undireted graph by Tarjan and BfS and compare their performance. [3]");

            Scanner scanner = new Scanner(System.in);
            Random random = new Random();
            int command = scanner.nextInt();
            System.out.println();
            // scanner.close();

            Graph graph;
            final int random_range = 15;
            final int vertices_quantity_lower_bound = 2;
            final int vertices_quantity_upper_bound = 100;

            switch (command) {
            case 1:
                graph = new Undirected_Graph(random.nextInt(random_range) + vertices_quantity_lower_bound);
                ((Undirected_Graph) graph).print_connected_components();
                break;

            case 2:
                graph = new Undirected_Graph(random.nextInt(random_range) + vertices_quantity_lower_bound);
                ((Undirected_Graph) graph).print_max_independent_set();
                break;

            case 3:
                int[][] chart_data = new int[vertices_quantity_upper_bound][4];
                FileWriter file_writer = new FileWriter("Chart data.txt");
                for (int v = vertices_quantity_lower_bound; v < vertices_quantity_upper_bound; v++) {
                    graph = new Directed_Graph(v);
                    chart_data[v][0] = ((Directed_Graph) graph).vertices_quantity;
                    chart_data[v][1] = ((Directed_Graph) graph).edges_quantity;
                    chart_data[v][2] = ((Directed_Graph) graph).set_SCCs_by_tarjan();
                    ((Directed_Graph) graph).reset_vertices();
                    chart_data[v][3] = ((Directed_Graph) graph).set_SCCs_by_BFS();

                    // ((Directed_Graph)graph).print_SCCs_tarjan();
                    // ((Directed_Graph)graph).reset_vertices();
                    // ((Directed_Graph)graph).print_SCCs_BFS();

                    file_writer.write(chart_data[v][0] + " ");
                    file_writer.write(chart_data[v][1] + " ");
                    file_writer.write(chart_data[v][2] + " ");
                    file_writer.write(chart_data[v][3] + "\n");
                }
                file_writer.close();
                System.out.println("Data saved.");
                break;

            default:
                System.out.println("Do not act like an asshole! enter a valid command.");
                break;
            }
            System.out.println("\nDo you hava any other command? [y]");
            repition = scanner.next();
        } while (repition.equals("y"));
    }
}
