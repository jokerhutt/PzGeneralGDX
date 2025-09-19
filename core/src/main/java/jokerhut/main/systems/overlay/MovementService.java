package jokerhut.main.systems.overlay;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import jokerhut.main.model.enums.Faction;
import jokerhut.main.model.hex.Axial;
import jokerhut.main.model.hex.Hex;
import jokerhut.main.selection.MovementOverlay;
import jokerhut.main.selection.SupplyRangeOverlay;
import jokerhut.main.systems.battlefield.BattleField;

public class MovementService {

	private record Node(Axial pos, int cost) {
	}

	private static final int[][] hexNeighborDirections = {
			{ +1, 0 }, { +1, -1 }, { 0, -1 },
			{ -1, 0 }, { -1, +1 }, { 0, +1 }
	};

	public static SupplyRangeOverlay computeSupplyRange(Axial startCoordinate, int supplyRange,
			HashMap<Axial, Hex> hexMap, HashMap<Axial, Integer> supplyField) {

		HashMap<Axial, Integer> cost = new HashMap<>();
		HashMap<Axial, Axial> parent = new HashMap<>();

		PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(node -> node.cost));

		cost.put(startCoordinate, 0);
		priorityQueue.add(new Node(startCoordinate, 0));

		while (!priorityQueue.isEmpty()) {

			Node currentNode = priorityQueue.poll();

			if (currentNode.cost() != cost.get(currentNode.pos())) {
				continue;
			}

			for (int[] direction : hexNeighborDirections) {

				Axial neighborDirection = new Axial(currentNode.pos().q() + direction[0],
						currentNode.pos().r() + direction[1]);

				if (!hexMap.containsKey(neighborDirection)) {
					continue;
				}

				int neighborToEnterCost = hexMap.get(neighborDirection).getMoveCost();
				int newCost = currentNode.cost() + neighborToEnterCost;
				if (newCost > supplyRange) {
					continue;
				}

				Integer bestPath = cost.get(neighborDirection);
				if (bestPath == null || newCost < bestPath) {

					cost.put(neighborDirection, newCost);
					parent.put(neighborDirection, currentNode.pos());
					priorityQueue.add(new Node(neighborDirection, newCost));

				}

			}

		}

		int max = cost.values().stream().max(Integer::compareTo).orElse(0);
		HashMap<Axial, Integer> inverted = new HashMap<>();
		for (var entry : cost.entrySet()) {
			inverted.put(entry.getKey(), (max - entry.getValue()) + 1);
			Integer supplyProvided = inverted.get(entry.getKey());

			if (!supplyField.containsKey(entry.getKey()) || supplyProvided > supplyField.get(entry.getKey())) {
				supplyField.put(entry.getKey(), supplyProvided);
			}

		}
		return new SupplyRangeOverlay(startCoordinate, supplyRange, inverted, parent);

	}

	public static MovementOverlay compute(Axial startCoordinate, int movementPointsLeft, float fuelPointsLeft,
			HashMap<Axial, Hex> hexMap,
			BattleField battleField, Faction playerFaction) {

		HashMap<Axial, Integer> cost = new HashMap<>();
		HashMap<Axial, Axial> parent = new HashMap<>();
		HashMap<Axial, Integer> attackable = new HashMap<>();

		int budget = Math.min(movementPointsLeft, (int) Math.floor(fuelPointsLeft));

		PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(node -> node.cost));

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
				if (battleField.getOccupiedHexes().containsKey(neighborDirection)) {

					if (battleField.unitAt(neighborDirection).getFaction() == playerFaction) {
						continue;
					} else {

						int attackCost = currentNode.cost + hexMap.get(neighborDirection).getMoveCost() + 1;
						if (attackCost <= budget) {
							System.out.println("CurrentNode Cost: " + currentNode.cost() + "Next hex cost: "
									+ hexMap.get(neighborDirection).getMoveCost() + " 1");
							attackable.merge(neighborDirection, attackCost, Math::min);
						}
						continue;
					}

				}

				int neighborToEnterCost = hexMap.get(neighborDirection).getMoveCost();
				int newCost = currentNode.cost() + neighborToEnterCost;
				if (newCost > budget)
					continue;

				Integer bestPath = cost.get(neighborDirection);
				if (bestPath == null || newCost < bestPath) {

					cost.put(neighborDirection, newCost);
					parent.put(neighborDirection, currentNode.pos());
					priorityQueue.add(new Node(neighborDirection, newCost));

				}
			}
		}

		return new MovementOverlay(startCoordinate, budget, cost, attackable, parent);
	}

}
