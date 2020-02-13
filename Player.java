package main;

import java.util.ArrayList;
import java.util.List;
import main.Constant;
abstract public class Player implements java.lang.Comparable<Player> {
	private int type;
	protected int bribe;
	protected int money;
	private int pen = 0;
	protected int itemP = 0;
	protected int itemN = 0;
	protected List<Integer> cards = new ArrayList<Integer>();
	private int[] items = new int[13];
	protected int[] itemsnow = new int[13];
	protected int[] sac = new int [13];
	private int finalScore = 0;
	private int comp = 0;
	
	public Player(int type) {
		this.type = type;
		this.money = 50;
		cards = null;
		items = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	}
	
	public String getType() {
		if (type == 0) {return "BASIC";}
		if (type == 1) {return "GREEDY";}
		return "BRIBED";
	}
	
	public List<Integer> getCards() {
		return cards;
	}

	public void setCards(List<Integer> c) {
		cards = new ArrayList<>(c);
	}
	
	abstract public void createItemsnow (int round);
	
	public void addMaxIllegalItem(int x) {
		int i, max = 6, k = 0;
		for (i = 0; i < cards.size(); i++) {
			if (cards.get(i) > max) {
				max = cards.get(i);
				k = i;
			}
		}
		sac[max]++;
		cards.remove(k);
		if (x == 0) {
			itemP = 0;
			itemN++;
		}
	}
	
	public int getMoney() {
		return money;
	}
	
	public void updateMoney(int m) {
		money = money + m;
	}
	
	public int getItemN() {
		return itemN;
	}
	
	public int getItemP() {
		return itemP;
	}
	
	public int[] declareItems(int round) {
		this.createItemsnow(round);
		return sac;
	}

	public void addLegalWay() {
		int i, max = 0,  k = -1, j;	
		for (i = 0; i < 4; i++) {
			if (max < itemsnow[i]) {max = itemsnow[i];}
		}
		if (max == 6) {
			for (i = 0; i < 4; i++) {
				if (max == itemsnow[i]) {
					itemsnow[i]--; k = i;
					for (j = 1; j < 6; j++) {
						cards.remove(i);
					}
				}
			}
		}
		else {
			for (i = 3; i >= 0; i--) {
				if (max == itemsnow[i]) {
					k = i; break;
				}
			}
			if ((itemsnow[3] == max)
					&& (k == 4)) {
				for (i = 0; i < 6; i++) {
					if ((cards.get(i) == 3)
							|| (cards.get(i) == 4)) {
						k = cards.get(i);
						break;
					}
				}
			}
			for (i = 0; i < 13; i++) {
				if (i != k) {
					itemsnow[i] = 0;
				}
			}
			i = 0;
			while (i < cards.size()) {
				if (cards.get(i) == k) {
					cards.remove(i);
				} else {
					i++;
				}
			}
		}
		itemP = k;
		itemN = itemsnow[k];
		sac[itemP] = itemN;
	}
	
	abstract public int  giveBribe();
	abstract public boolean recieveBribe();
	
	public void updateItems() {
		for (int i = 0; i < 13; i++) {
			items[i] = items [i] + sac[i];
			if (i == 10) {
				items[1] = items[1] + 3*sac[i];
			}
			if (i == 11) {
				items[3] = items[3] + 2*sac[i];
			}
			if (i == 12) {
				items[2] = items[2] + 2*sac[i];
			}
		}
	}
	
	public int inspectPerson(int[] itemsR, int n, int d) {
		boolean ok = true;
		pen = 0;
		for (int i = 10; i < 13; i++) {
			if (itemsR[i] != 0) {
				ok = false;
				pen = itemsR[i] * Constant.PENALTY[i] + pen;
				itemsR[i] = 0;
			}
		}
		for (int i = 10; i < 13; i++) {
			sac[i] = 0;
		}
		if (ok) {
			pen = n*Constant.PENALTY[d];
			pen = pen * (-1);
		}
		return pen;
	}
	public void calculatePoints() {
		finalScore = money;
		for (int i = 0; i < 13; i++) {
			finalScore = finalScore + items[i]*Constant.PROFIT[i];
		}
	}
	
	public void updateBonus(int t, int k) {
		finalScore = finalScore + Constant.BONUS[t*4 + k];
	}
	
	public int getItem(int k) {
		return items[k];
	}
	
	public int getFinalScore() {
		return finalScore;
	}
	
	public int getComp() {
		return comp;
	}
	
	public void setComp(int a) {
		comp = a;
	}
	
	public int compareTo(Player otherPlayer) {
        if(this.comp == otherPlayer.getComp()){
            return 0;
        } else if(this.comp < otherPlayer.getComp()){
            return 1;
        } else{
            return -1;
        }
    }
}