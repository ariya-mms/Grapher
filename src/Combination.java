import java.util.ArrayDeque;

final class Combination {
	static ArrayDeque<Graph.Vertex[]> sets;

	public static void combinationUtil(ArrayDeque<Graph.Vertex>[] adjacency_list, Graph.Vertex[] data, int start, int end,
			int index, int r) {
		if (index == r) {
			sets.add(data.clone());
			return;
		}

		for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
			data[index] = adjacency_list[i].getFirst();
			combinationUtil(adjacency_list, data, i + 1, end, index + 1, r);
		}
	}

	public static ArrayDeque<Graph.Vertex[]> printCombination(ArrayDeque<Graph.Vertex>[] adjacency_list, int n, int r) {
		Graph.Vertex[] data = new Graph.Vertex[r];
		sets = new ArrayDeque<>();
		combinationUtil(adjacency_list, data, 0, n - 1, 0, r);
		return sets;
	}
}