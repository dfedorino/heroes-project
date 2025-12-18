package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    private static final int[][] DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };

    /**
     * Метод определяет кратчайший маршрут между атакующим и атакуемым юнитом и возвращает его в
     * виде списка объектов, содержащих координаты каждой точки данного кратчайшего пути. Цель
     * данного метода — найти кратчайший путь между атакующим и атакуемым юнитами. То есть для
     * атакующего юнита с координатами x = 1 и y = 2 и атакуемого юнита x = 0 и y = 0 результатом
     * станет список [Edge(1, 2), Edge (1, 1), Edge (1,0)].
     * <p>
     * Для определения кратчайшего пути рекомендуем использовать один из алгоритмов теории графов.
     * Метод имеет следующую сигнатуру: List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit,
     * List<Unit> existingUnitList) Алгоритмическая сложность данного метода составляет O((WIDTH *
     * HEIGHT) log WIDTH * HEIGHT) или лучше, где: WIDTH — ширина игрового поля (27); HEIGHT —
     * высота игрового поля (21). Алгоритм, представленный в коде, использует один из алгоритмов
     * теории графов для нахождения кратчайшего пути на поле с препятствиями, где некоторые клетки
     * заняты юнитами. Основные шаги включают инициализацию структуры данных для хранения
     * расстояний, обработку соседних клеток и определение пути. Допускается движение по диагонали.
     *
     * @param attackUnit       - юнит, который атакует
     * @param targetUnit       - юнит, который подвергается атаке
     * @param existingUnitList - список всех существующих юнитов на данный момент
     * @return список объектов Edge, то есть координат клеток пути от атакующего юнита до атакуемого
     * юнита включительно. Если маршрут не найден — возвращает пустой список.
     */
    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        boolean[][] blockedCells = new boolean[HEIGHT][WIDTH]; // Space: O(W*H)
        for (Unit unit : existingUnitList) {
            if (unit.isAlive()) {
                int x = unit.getxCoordinate();
                int y = unit.getyCoordinate();
                blockedCells[y][x] = true;
            }
        } // O(n), n = existingUnitList.size()
        Node startNode = new Node(attackUnit.getxCoordinate(), attackUnit.getyCoordinate());

        Queue<Node> nodesToVisit = new PriorityQueue<>(
                Comparator.comparingInt(node -> node.distanceFromStart));
        Map<String, Node> createdNodes = new HashMap<>();

        nodesToVisit.offer(startNode); // O(log 1) ~ O(1)
        createdNodes.put(startNode.getKey(), startNode); // O(1)

        while (!nodesToVisit.isEmpty()) {
            Node current = nodesToVisit.poll(); // O(log k), k — текущее количество узлов в очереди

            for (int[] direction : DIRECTIONS) { // O(1), DIRECTIONS.length = 8
                int neighborX = current.x + direction[0];
                int neighborY = current.y + direction[1];

                if (neighborX == targetUnit.getxCoordinate()
                        && neighborY == targetUnit.getyCoordinate()) {
                    return reconstructedPath(new Node(neighborX,
                                                      neighborY,
                                                      current.distanceFromStart + 1,
                                                      current)); // O(pathLength) <= O(W*H)
                }

                if (neighborX >= 0 && neighborX < WIDTH && neighborY >= 0 && neighborY < HEIGHT) {
                    if (!blockedCells[neighborY][neighborX]) {
                        String key = neighborX + "," + neighborY;
                        Node neighbor = createdNodes.get(key); // O(1)
                        if (neighbor == null) {
                            neighbor = new Node(neighborX,
                                                neighborY,
                                                current.distanceFromStart + 1,
                                                current);
                            createdNodes.put(key, neighbor); // O(1)
                            nodesToVisit.offer(neighbor); // O(log k)
                        } else if (current.distanceFromStart + 1 < neighbor.distanceFromStart) {
                            neighbor.distanceFromStart = current.distanceFromStart + 1;
                            neighbor.parent = current;
                            nodesToVisit.offer(neighbor); // O(log k)
                        }
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    private List<Edge> reconstructedPath(Node target) {
        List<Edge> path = new ArrayList<>();
        for (Node node = target; node != null; node = node.parent) {
            path.add(new Edge(node.x, node.y));
        } // O(pathLength) ≤ O(W*H)
        Collections.reverse(path); // O(pathLength) ≤ O(W*H)
        return path;
    }

    private static class Node {

        int x;
        int y;
        int distanceFromStart;
        Node parent;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.distanceFromStart = 0;
            this.parent = null;
        }

        public Node(int x, int y, int distanceFromStart, Node parent) {
            this.x = x;
            this.y = y;
            this.distanceFromStart = distanceFromStart;
            this.parent = parent;
        }

        public String getKey() {
            return x + "," + y;
        }
    }
}
