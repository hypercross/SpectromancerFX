package entity;

import game.Card;
import game.Triggerable;

public class EntityCard extends Entity implements TriggerCarrier
{
	public int cost;
	public boolean usable;
	public EntityGame game;
	public EntityPlayer player;
	
	public Card card;
	
	public EntityCard(Card card, EntityPlayer player)
	{
		this.card = card;
		this.cost = card.getCost();
		this.game = player.game;
		this.player = player;
		usable = true;
	}

	@Override
	public Triggerable getTriggerable() {
		return card;
	}
	
	public String toString()
	{
		return card.toString();
	}
	
	public boolean sufficientPower()
	{
		return player.getPowerValue(card.getPower()) >= cost;
	}
}
