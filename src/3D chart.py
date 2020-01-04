import matplotlib.pyplot

file_data = list()
with open("Chart data.txt", "r") as file:
    for line in file.readlines():
        file_data.append(list(str(line).split()))

figure = matplotlib.pyplot.figure()
tarjan_line = figure.gca(projection='3d')
bfs_line = figure.gca(projection='3d')
bfs_line = figure.gca(projection='3d')
x_vertex = list()
y_edge = list()
z_tarjan = list()
z_bfs = list()

for line in file_data:
    x_vertex.append(int(line[0]))
    y_edge.append(int(line[1]))
    z_tarjan.append(int(line[2]))
    z_bfs.append(int(line[3]))

tarjan_line.plot(x_vertex, y_edge, z_tarjan, label='Tarjan Time Cost')
tarjan_line.legend()
bfs_line.plot(x_vertex, y_edge, z_bfs, label='BFS Time Cost')
bfs_line.legend()

matplotlib.pyplot.show()