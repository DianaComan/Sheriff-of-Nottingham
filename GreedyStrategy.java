package main;

public class GreedyStrategy extends Player{
	public GreedyStrategy(int type) {
		super(type);
	}

	public void createItemsnow (int round) {
		boolean ok = false;
		int x = 1;
		int i;
		itemP = 0;
		itemN = 0;
		for (i = 0; i < 13; i++) {
			itemsnow[i] = 0;
			sac[i] = 0;
		}
		for (i = 0; i < 6; i++) {
			if (cards.get(i) < 4) {
				ok = true;
			}
			itemsnow[cards.get(i)]++;
		}
		
		if (ok == true) {
			addLegalWay();
		} else {
			addMaxIllegalItem(0);
			x = 0;
		}
		if (((round % 2) == 0) && (itemN != 5)) {
			addMaxIllegalItem(x);
		}
	}
	
	public int giveBribe() {
		return 0;
	}
	
	public boolean recieveBribe() {
		return true;
	}
}