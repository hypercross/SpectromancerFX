package entity;

import game.DamageType;
import game.Event;
import game.PlayerClass;
import game.Power;
import game.Triggerable;
import provider.EventFactory;

public class EntityPlayer extends Entity implements TriggerCarrier, Damageable{

	int hp;
	Power[] powers;
	int[] power_values;
	EntityCard[][] cards;
	EntityGame game;
	PlayerClass type;

	boolean side;

	public String toString()
	{
		return side ? "Guest Player" : "Host Player";
	}

	public EntityPlayer(boolean side, EntityGame game, PlayerClass type)
	{
		this(side,game, type.getPowers());
		this.type = type;
	}

	public EntityPlayer(boolean side, EntityGame game, Power... powers)
	{
		this.side = side;
		this.game = game;
		this.powers = powers.clone();
		this.power_values = new int[powers.length];
		this.hp = 60;

		cards = new EntityCard[powers.length][];
		for(int i = 0 ; i < powers.length;i++)cards[i] = new EntityCard[4];
	}

	private boolean compare(EntityCard a, EntityCard b)
	{
		if(a == null)return false;
		if(b == null)return true;
		return a.cost <= b.cost;
	}

	private void sortCardByCost(EntityCard[] cards)
	{
		for(int i = 0;i<4;i++)
			for(int j = 0 ; j<3;j++)
			{
				if(!compare(cards[j],cards[j+1]))
				{
					EntityCard c = cards[j];
					cards[j] = cards[j+1];
					cards[j+1] = c;
				}
			}
	}

	public EntityCard[][] getCards()
	{
		return cards;
	}

	public boolean giveCard(EntityCard card)
	{
		int i = powerIndex(card.card.getPower());
		if(i>=0)for(int j = 0 ; j < 4; j ++)if(cards[i][j] == null)
		{
			cards[i][j] = card;
			sortCardByCost(cards[i]);
			card.join();
			return true;
		}

		return false;
	}

	public boolean takeCard(EntityCard c)
	{
		for(int i = 0 ; i < powers.length; i ++)
			for(int j = 0;j < 4;j++)
				if(cards[i][j] == c)
				{
					cards[i][j] = null;
					sortCardByCost(cards[i]);
					c.remove();
					return true;
				}
		return false;
	}

	public EntityPlayer getOpponent()
	{
		if(this == game.self)return game.opponent;
		return game.self;
	}

	public EntityGame game()
	{
		return game;
	}

	public boolean damage(int x, DamageType type)
	{
		Event.Damage damage = new Event.Damage(x, type, this);
		EventFactory.post(damage);

		hp -= damage.result();
		if(hp<=0)
		{
			game.terminate();
			return true;
		}
		return false;
	}

	public void heal(int x)
	{
		hp += x;
	}

	public Power[] getPowers()
	{
		return powers;
	}

	public int getPowerValue(Power pwr)
	{
		for(int i = 0 ;i < powers.length;i++)
			if(powers[i] == pwr)
				return power_values[i];
		return 0;
	}

	public int getPowerValue(int i)
	{
		return power_values[i];
	}

	public void regeneratePower(Power pwr, int x)
	{
		for(int i = 0 ;i < powers.length;i++)
			if(powers[i] == pwr)
				regeneratePower(i,x);
	}

	public void regeneratePower(int i,int x)
	{
		power_values[i] += x;
		if(power_values[i] < 0 ) power_values[i] = 0;
	}

	public void savagePower(Power pwr, int x)
	{
		regeneratePower(pwr, -x);
	}

	private int powerIndex(Power pwr)
	{
		for(int i = 0 ;i < powers.length;i++)
			if(powers[i] == pwr)return i;
		return -1;
	}

	public int getHp() {
		return hp;
	}

	@Override
	public Triggerable getTriggerable() {
		if(type == null)
			return new Triggerable()
		{
			public void trigger(TriggerCarrier tc, Event e){}
		};

		return type;
	}

	public boolean isOnTurn() {
		if(game.thread == null)return false;
		return game.thread.playerOnTurn == this;
	}
}
