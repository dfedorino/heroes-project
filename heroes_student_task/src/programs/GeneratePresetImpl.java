package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class GeneratePresetImpl implements GeneratePreset {

    private static final int MAX_UNITS_PER_TYPE = 11;
    private static final int ROWS = 21;
    private static final int COLS = 3;

    private Queue<int[]> freeCells;

    // Time Complexity: O(n log n + n * m)
    // Space Complexity: O(n * m)
    public Army generateForCommonCase(List<Unit> unitList, int maxPoints) {
        initFreeCells(); // O(1)
        unitList.sort((a, b) -> {
            long aAttackEfficiency = (long) a.getBaseAttack() * b.getCost();
            long bAttackEfficiency = (long) b.getBaseAttack() * a.getCost();

            if (aAttackEfficiency == bAttackEfficiency) {
                long aHealthEfficiency = (long) a.getHealth() * b.getCost();
                long bHealthEfficiency = (long) b.getHealth() * a.getCost();
                return Long.compare(bHealthEfficiency, aHealthEfficiency);
            }

            return Long.compare(bAttackEfficiency, aAttackEfficiency);
        }); // O(n log n)

        int remainingPoints = maxPoints;
        List<Unit> units = new ArrayList<>();
        for (int i = 0; i < unitList.size() && remainingPoints > 0; i++) {
            Unit unitTemplate = unitList.get(i);
            int availableForPurchase = remainingPoints / unitTemplate.getCost();
            int maximumPurchasableUnits = Math.min(availableForPurchase, MAX_UNITS_PER_TYPE);
            for (int j = 0; j < maximumPurchasableUnits; j++) {
                Unit unit = createUnit(unitTemplate, j);
                units.add(unit);
            } // O(m)
            System.out.printf(">> Added %s units of type %s\n", maximumPurchasableUnits,
                              unitTemplate.getUnitType());
            remainingPoints -= maximumPurchasableUnits * unitTemplate.getCost();
        } // O(n * m)
        Army army = new Army(units);
        army.setPoints(maxPoints - remainingPoints);
        System.out.println(">> Army points: " + army.getPoints());
        return army;
    }

    private void initFreeCells() {
        List<int[]> cells = new ArrayList<>();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                cells.add(new int[]{row, col});
            }
        }
        Collections.shuffle(cells);
        freeCells = new ArrayDeque<>(cells);
    }

    private Unit createUnit(Unit unitTemplate, int j) {
        int[] cell = getRandomAvailableCell();
        return new Unit(unitTemplate.getName() + " " + (j + 1),
                        unitTemplate.getUnitType(),
                        unitTemplate.getHealth(),
                        unitTemplate.getBaseAttack(),
                        unitTemplate.getCost(),
                        unitTemplate.getAttackType(),
                        unitTemplate.getAttackBonuses(),
                        unitTemplate.getDefenceBonuses(),
                        cell[1],
                        cell[0]);
    }

    private int[] getRandomAvailableCell() {
        if (freeCells.isEmpty()) {
            throw new IllegalStateException("No available grid cells left");
        }
        return freeCells.poll();
    }

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        initFreeCells(); // O(1)

        // We can rely on unit template order
        Army army = new Army(List.of(
                // Knight
                createUnit(unitList.get(0), 0),
                createUnit(unitList.get(0), 1),
                createUnit(unitList.get(0), 2),
                createUnit(unitList.get(0), 3),
                createUnit(unitList.get(0), 4),
                createUnit(unitList.get(0), 5),
                createUnit(unitList.get(0), 6),
                createUnit(unitList.get(0), 7),
                createUnit(unitList.get(0), 8),
                createUnit(unitList.get(0), 9),
                createUnit(unitList.get(0), 10),

                // Swordsman
                createUnit(unitList.get(1), 0),
                createUnit(unitList.get(1), 1),
                createUnit(unitList.get(1), 2),
                createUnit(unitList.get(1), 3),
                createUnit(unitList.get(1), 4),
                createUnit(unitList.get(1), 5),
                createUnit(unitList.get(1), 6),
                createUnit(unitList.get(1), 7),
                createUnit(unitList.get(1), 8),
                createUnit(unitList.get(1), 9),
                createUnit(unitList.get(1), 10),

                // Pikeman
                createUnit(unitList.get(2), 0),
                createUnit(unitList.get(2), 1),
                createUnit(unitList.get(2), 2),
                createUnit(unitList.get(2), 3),
                createUnit(unitList.get(2), 4),
                createUnit(unitList.get(2), 5),
                createUnit(unitList.get(2), 6),
                createUnit(unitList.get(2), 7),
                createUnit(unitList.get(2), 8),
                createUnit(unitList.get(2), 9),
                createUnit(unitList.get(2), 10),

                // Archer
                createUnit(unitList.get(3), 0),
                createUnit(unitList.get(3), 1),
                createUnit(unitList.get(3), 2),
                createUnit(unitList.get(3), 3),
                createUnit(unitList.get(3), 4),
                createUnit(unitList.get(3), 5),
                createUnit(unitList.get(3), 6),
                createUnit(unitList.get(3), 7),
                createUnit(unitList.get(3), 8)
        )); // O(1)
        army.setPoints(maxPoints);
        return army;
    }
}