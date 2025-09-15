package jokerhut.main.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.Hex;
import jokerhut.main.screen.BattleField;
import jokerhut.main.selection.MovementOverlay;

public class MovementService {

    private record Node(Axial pos, int cost) {
    }

    private static final int[][] hexNeighborDirections = {
            { +1, 0 }, { +1, -1 }, { 0, -1 },
            { -1, 0 }, { -1, +1 }, { 0, +1 }
    };

    public MovementOverlay compute(Axial startCoordinate, int movementPointsLeft, HashMap<Axial, Hex> hexMap,
            BattleField battleField) {

        HashMap<Axial, Integer> cost = new HashMap<>();
        HashMap<Axial, Axial> parent = new HashMap<>();

        // Auto order queue by smallest node cost first
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(node -> node.cost));

        // Place Currently Selected Hex into queue
        cost.put(startCoordinate, 0);
        priorityQueue.add(new Node(startCoordinate, 0));

        while (!priorityQueue.isEmpty()) {

            Node currentNode = priorityQueue.poll();
            if (currentNode.cost() != cost.get(currentNode.pos()))
                continue;

            for (int[] direction : hexNeighborDirections) {

                Axial neighborDirection = new Axial(currentNode.pos().q() + direction[0],
                        currentNode.pos().r() + direction[1]);

                if (!hexMap.containsKey(neighborDirection))
                    continue;
                if (battleField.getOccupiedHexes().containsKey(neighborDirection))
                    continue;

                int neighborToEnterCost = hexMap.get(neighborDirection).getMoveCost();
                int newCost = currentNode.cost() + neighborToEnterCost;
                if (newCost > movementPointsLeft)
                    continue;

                Integer bestPath = cost.get(neighborDirection);
                if (bestPath == null || newCost < bestPath) {

                    cost.put(neighborDirection, newCost);
                    parent.put(neighborDirection, currentNode.pos());
                    priorityQueue.add(new Node(neighborDirection, newCost));

                }
            }
        }

        return new MovementOverlay(startCoordinate, movementPointsLeft, cost, parent);
    }

}
