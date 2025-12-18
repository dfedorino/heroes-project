package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {

    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита

    /**
     * Этот метод осуществляет симуляцию боя между армией игрока и армией компьютера.
     * Цель метода — провести бой, следуя установленным правилам.
     * <p>
     * Симуляция происходит следующим образом:
     * <p>
     * 1. На каждом раунде юниты сортируются по убыванию значения атаки, чтобы первыми
     * ходили самые сильные.
     * 2. Пока в обеих армиях есть живые юниты, они атакуют друг друга по очереди.
     * 3. Если у одной из армий заканчиваются юниты, она ожидает завершения ходов
     * оставшихся юнитов противника.
     * 4. Когда все юниты походили, раунд завершается, и начинается следующий.
     * <p>
     * Юниты, которые погибли (значение поля isAlive — false) и ещё не походили,
     * исключаются из очередей в момент их смерти, и очереди хода пересчитываются.
     * <p>
     * Если количество юнитов в армиях становится разным из-за потерь,
     * очерёдность ходов может измениться.
     * <p>
     * Юниты атакуют друг друга с помощью метода unit.getProgram().attack(),
     * который возвращает цель атаки (юнит противника) или null, если цель не найдена.
     * <p>
     * После каждой атаки необходимо вывести лог с помощью метода
     * printBattleLog.printBattleLog(unit, target), где unit — атакующий юнит,
     * а target — цель атаки.
     * 5. Симуляция завершается, когда у одной из армий не остаётся живых юнитов, способных сделать ход.
     *
     * Алгоритмическая сложность данного алгоритма должна быть O(n^2 * log n) или лучше,
     * если принять, что метод атаки юнита работает за O(1),
     * где n — общее количество юнитов в армии.
     * @param playerArmy - объект армии игрока, содержащий список её юнитов
     * @param computerArmy - объект армии компьютера, содержащий список её юнитов.
     */
    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        while (true) {
            List<Unit> aliveUnitsForRound = new ArrayList<>();
            int addedPlayerUnits = addAliveUnits(playerArmy, aliveUnitsForRound); // O(n)
            if (addedPlayerUnits == 0) {
                break;
            }
            int addedComputerUnits = addAliveUnits(computerArmy, aliveUnitsForRound); // O(n)
            if (addedComputerUnits == 0) {
                break;
            }

            aliveUnitsForRound.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed()); // O(n log n)
            for (Unit unit : aliveUnitsForRound) {
                if (unit.isAlive()) {
                    Unit attacked = unit.getProgram().attack(); // O(1)
                    printBattleLog.printBattleLog(unit, attacked);
                }
            } // O(n)
        }
    }

    private static int addAliveUnits(Army army, Collection<Unit> units) {
        int addedUnits = 0;
        for (Unit unit : army.getUnits()) {
            if (unit.isAlive()) {
                addedUnits++;
                units.add(unit);
            }
        } // O(n)
        return addedUnits;
    }
}