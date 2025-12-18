package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    /**
     * Метод определяет список юнитов, подходящих для атаки, для атакующего юнита одной из армий.
     * Цель метода — исключить ненужные попытки найти кратчайший путь между юнитами, которые не
     * могут атаковать друг друга.
     * <p>
     * Подходящий юнит для атаки для атакующей армии компьютера — это юнит армии игрока, который не
     * закрыт справа (по координате y) другим юнитом армии игрока.
     * <p>
     * Подходящий юнит для атаки для атакующей армии игрока — это юнит армии компьютера, который не
     * закрыт слева (по координате y) другим юнитом армии компьютера. То есть слева от юнита в
     * соседней клетке находится другой юнит.
     * <p>
     * Алгоритмическая сложность данного метода должна быть O(n * m) или лучше, что означает
     * линейную сложность на двумерной плоскости, где n — количество юнитов в ряду, а m — количество
     * рядов. Так как количество рядов фиксировано и равно трём, алгоритм фактически должен иметь
     * линейную сложность O(n) или лучше.
     *
     * @param unitsByRow       - трёхслойный массив юнитов противника. Для юнита из атакующей армии
     *                         компьютера эти юниты находятся на координатах 24..26 по оси x. Для
     *                         армии игрока они располагаются на координатах 0..2 по оси x
     *                         (фактически, это юниты армии компьютера).
     * @param isLeftArmyTarget - параметр, который указывает, юниты какой армии подвергаются атаке.
     *                         Если значение true, то атаке подвергаются юниты армии компьютера
     *                         (левая армия); если false — юниты армии игрока (правая армия).
     * @return список юнитов, подходящих для атаки, для юнита атакующей армии
     */
    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        return isLeftArmyTarget ? getComputerSuitableUnits(unitsByRow) : getPlayerSuitableUnits(unitsByRow);
    }

    private List<Unit> getComputerSuitableUnits(List<List<Unit>> unitsByRow) {
        List<Unit> suitableUnits = new ArrayList<>();
        Set<Integer> previousRowY = new HashSet<>();
        Set<Integer> currentRowY = new HashSet<>();

        for (int i = unitsByRow.size() - 1; i >= 0; i--) {
            for (Unit unit : unitsByRow.get(i)) {
                if (unit == null || !unit.isAlive()) {
                    continue;
                }
                currentRowY.add(unit.getyCoordinate()); // O(1)
                if (previousRowY.contains(unit.getyCoordinate())) {
                    continue;
                } // O(1)
                suitableUnits.add(unit); // O(1)
            } // O(m)
            previousRowY = currentRowY;
            currentRowY = new HashSet<>();
        } // O(n * m)
        return suitableUnits;
    }

    private List<Unit> getPlayerSuitableUnits(List<List<Unit>> unitsByRow) {
        List<Unit> suitableUnits = new ArrayList<>();
        Set<Integer> previousRowY = new HashSet<>();
        Set<Integer> currentRowY = new HashSet<>();

        for (List<Unit> units : unitsByRow) {
            for (Unit unit : units) {
                if (unit == null || !unit.isAlive()) {
                    continue;
                }
                currentRowY.add(unit.getyCoordinate()); // O(1)
                if (previousRowY.contains(unit.getyCoordinate())) {
                    continue;
                } // O(1)
                suitableUnits.add(unit); // O(1)
            } // O(m)
            previousRowY = currentRowY;
            currentRowY = new HashSet<>();
        } // O(n * m)
        return suitableUnits;
    }
}
