package main;

public class BribeStrategy extends Player {
	public BribeStrategy(int type) {
		super(type);
	}
	
	public void createItemsnow (int round) {
		int ok = 0;
		int i;
		itemP = 0;
		itemN = 0;
		bribe = 0;
		for (i = 0; i < 13; i++) {
			itemsnow[i] = 0;
			sac[i] = 0;
		}
		for (i = 0; i < 6; i++) {
			if (cards.get(i)> 4) {
				ok++;
			}
			itemsnow[cards.get(i)]++;
		}
		if (ok == 0) {
			addLegalWay();
		} else {
			if (money > 10) {
				for (i = 0; i < ok; i++) {
					addMaxIllegalItem(0);
				}
				if (ok > 2) {bribe = 10;}
				else {bribe = 5;}
			}
			else {
				if (money > 5) {
					for (i = 0; i < ok; i++) {
						addMaxIllegalItem(0);
					}
					bribe = 5;
				}
				else {
					if (ok == 6) {addMaxIllegalItem(0);}
					else {addLegalWay();}
				}
			}
		}
	}
	
	public int giveBribe() {
		return bribe;
	}
	
	//la trei jucatori va primi mereu Bride
	public boolean recieveBribe() {
		return true;
	}
}