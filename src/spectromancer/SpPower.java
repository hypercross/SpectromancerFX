package spectromancer;

import game.Card;
import game.Power;

public class SpPower implements Power{

	String name;
	int size;
	public SpPower(String name, int size)
	{
		this.name = name;
		this.size = size;
		cards = new Card[size];
	}
	
	protected Card[] cards;
	
	@Override
	public Card getCard(int i) {
		return cards[i];
	}

	@Override
	public String getDisplayName() {
		return name;
	}

	@Override
	public int getSize() {
		return size;
	}

}
