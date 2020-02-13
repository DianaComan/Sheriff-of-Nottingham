package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import main.Player;

public final class Main {
	private static final class GameInputLoader {
		private final String mInputPath;

		private GameInputLoader(final String path) {
			mInputPath = path;
		}

		public GameInput load() {
			List<Integer> assetsIds = new ArrayList<>();
			List<String> playerOrder = new ArrayList<>();

			try {
				BufferedReader inStream = new BufferedReader(new FileReader(mInputPath));
				String assetIdsLine = inStream.readLine().replaceAll("[\\[\\] ']", "");
				String playerOrderLine = inStream.readLine().replaceAll("[\\[\\] ']", "");

				for (String strAssetId : assetIdsLine.split(",")) {
					assetsIds.add(Integer.parseInt(strAssetId));
				}

				for (String strPlayer : playerOrderLine.split(",")) {
					playerOrder.add(strPlayer);
				}
				inStream.close();


			} catch (IOException e) {
				e.printStackTrace();
			}
			return new GameInput(assetsIds, playerOrder);
		}
	}

	public static void main(final String[] args) {
		GameInputLoader gameInputLoader = new GameInputLoader(args[0]);
		int i = 0, j = 0, k = 0, size = 0, k_cards = 0;
		int index = 0, number = 0, nItem = 0, pozItem = 0;
		int bribe = 0, pen = 0, round = 0, comp = 0;
		int[] sac = new int[13];
		List<String> play;
		List<Integer> asset = new ArrayList<Integer>();
		List<Integer> cardNow = new ArrayList<Integer>();
		GameInput gameInput = gameInputLoader.load();
		//int[] a1 = {0, 2, 0, 2, 10, 2, 3, 10, 0, 3, 2, 0, 0, 1, 3, 2, 0, 0, 10, 12, 0, 0, 10, 2, 3, 1, 0, 0, 2, 1, 1, 0, 11, 0, 3, 3, 12, 2, 0, 2, 1, 12, 10, 2, 2, 10, 2, 0, 11, 12, 2, 11, 2, 10, 3, 3, 10, 3, 10, 0, 1, 12, 0, 1, 12, 11, 0, 3, 11, 0, 0, 12, 0, 0, 1, 0, 12, 0, 3, 10, 1, 10, 1, 1, 0, 1, 11, 2, 10, 2, 1, 1, 10, 3, 2, 0, 10, 1, 10, 2, 10, 1, 3, 0, 12, 1, 0, 12, 3, 3, 3, 3, 0, 11, 0, 0, 0, 2, 2, 10, 3, 12, 10, 11, 11, 3, 2, 1, 12, 3, 12, 2, 1, 11, 1, 1, 11, 1, 2, 0, 1, 2, 1, 11, 1, 0, 12, 11, 0, 0, 11, 0, 2, 12, 0, 0, 0, 12, 0, 0, 1, 2, 2, 2, 1, 0, 1, 10, 2, 11, 12, 1, 0, 11, 1, 1, 3, 2, 12, 0, 2, 12, 12, 0, 11, 2, 2, 2, 1, 2, 3, 3, 11, 2, 1, 10, 2, 11, 1, 3, 3, 2, 1, 0, 0, 0, 11, 1, 2, 1, 1, 0, 1, 2, 3, 1};
	//	String[] b1 = {"basic", "bribed", "greedy"};
		
		/*for (i = 0; i < 216; i++)
		{
		    asset.add(a1[i]);
		}
		for (i = 0; i < 3; i++)
		{
		    play.add(b1[i]);
		}*/
		play = new ArrayList<String>(gameInput.getPlayerNames());
		asset = new ArrayList<Integer>(gameInput.getAssetIds());
		number = play.size();
		cardNow = new ArrayList<Integer>();
		List <Player> players = new ArrayList <Player>();
		for (i = 0; i < number; i++) {
			if (play.get(i) == "basic") {
				players.add(new BaseStrategy(0));
			}
			if (play.get(i) == "greedy") {
				players.add(new GreedyStrategy(1));
			}
			if (play.get(i) == "bribed") {
				players.add(new BribeStrategy(2));
			}
		}
		for (i = 0; i < number; i++) {
			int l = cardNow.size();
			for (j = 0; j < l; j++) {
				cardNow.remove(0);
			}
			for (j = 0; j < 6; j++) {
				cardNow.add(asset.get(k_cards));
				k_cards++;
			}
			players.get(i).setCards(cardNow);
		}
		for (j = 1; j <= 2; j++) {
			for (i = 0; i < number; i++) {
				for (k = 0; k < number; k++) {
					if (k != i) {
						if (players.get(k).getType() == "GREEDY") {
							round++;
						}
						//creazaSac
						sac = players.get(k).declareItems(round);
						nItem = players.get(k).getItemN();
						pozItem = players.get(k).getItemP();
						//daca inspectorul primeste mita
						//daca comerciant da mita
						// ia mita, update Money
						//altfel
						//inspecteaaza sac
						bribe = players.get(k).giveBribe();
						if ((players.get(i).recieveBribe())
								&& (bribe > 0)) {
							players.get(i).updateMoney(bribe);
							players.get(k).updateMoney(-bribe);
						} else {
							pen = players.get(k).inspectPerson(sac, nItem, pozItem);
							if (pen > 0) {
								players.get(k).updateMoney(-pen);
								players.get(i).updateMoney(pen);
								for (int i1 = 10; i1 < 13; i1++) {
									sac[i1] = 0;
								}
							} else {
								players.get(k).updateMoney(-pen);
								players.get(i).updateMoney(pen);
							}
						}
						//recompletare carti
						cardNow = players.get(k).getCards();
						size = cardNow.size();
						for (index = size; index < 6; index++) {
							cardNow.add(asset.get(k_cards));
							k_cards++;
						}
						players.get(k).setCards(cardNow);
						//update items taraba - sac
						// verificare penalty!!!
						players.get(k).updateItems();
					}
				}
			}
		}
		// calculate points + refacere functie din Player
		for (k = 0; k < number; k++) {
			players.get(k).calculatePoints();
		}
		// ordonare
		i = 0;
		for (j = 0; j < 4; j++) {
			for (k = 0; k < number; k++) {
				comp = players.get(k).getItem(j);
				players.get(k).setComp(comp);
			}
			Collections.sort(players);
			for (k = 0; k < number; k++) {
				comp = players.get(k).getItem(j);
			}
			if (number == 2) {
				if (players.get(0).getItem(j) == players.get(1).getItem(j)) {
					players.get(0).updateBonus(0, j);
					players.get(1).updateBonus(0, j);
				} else {
					players.get(0).updateBonus(0, j);
					players.get(1).updateBonus(1, j);
				}
			}
			if (number == 3) {
				if (players.get(0).getItem(j) == players.get(2).getItem(j)) {
					players.get(0).updateBonus(0, j);
					players.get(1).updateBonus(0, j);
					players.get(2).updateBonus(0, j);
				} else {
					if (players.get(0).getItem(j) == players.get(1).getItem(j)) {
						players.get(0).updateBonus(0, j);
						players.get(1).updateBonus(0, j);
					} else {
						if (players.get(1).getItem(j) == players.get(2).getItem(j)) {
							players.get(0).updateBonus(0, j);
							players.get(1).updateBonus(1, j);
							players.get(2).updateBonus(1, j);
						} else {
							players.get(0).updateBonus(0, j);
							players.get(1).updateBonus(1, j);
						}
					}
				}
			}
		}
			//calculate Bonus - citit in enunt, refacere functie
			//afisare
		for (i = 0; i < number; i++) {
			comp = players.get(i).getFinalScore();
			players.get(i).setComp(comp);
		}
		Collections.sort(players);
		for (i = 0; i < number; i++) {
			String a = players.get(i).getType();
			int b = players.get(i).getFinalScore();
			System.out.println(a + ": " + b);
		}
	}
}