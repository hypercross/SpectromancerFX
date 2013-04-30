package spectromancer;

import entity.EntityCard;
import entity.EntityCreature;
import entity.TriggerCarrier;
import game.Creature;
import game.Event;
import game.Power;

import java.lang.reflect.Method;

import util.Logger;

public class SpCreature implements  Creature{

	int cst,atk,hp;
	Power pwr;
	private String name;
	
	public SpCreature(String name, int cst, int atk, int hp, Power pwr)
	{
		this.cst = cst;
		this.atk = atk;
		this.hp = hp;
		this.pwr = pwr;
		this.name = name;
	}
	
	public String toString()
	{
		return name;
	}
	
	@Override
	public int getAtk() {
		return atk;
	}

	@Override
	public int getHp() {
		return hp;
	}

	@Override
	public int getCost() {
		return cst;
	}

	@Override
	public Power getPower() {
		return pwr;
	}

	@Override
	public void trigger(TriggerCarrier source, Event event) {
		for(Method m : this.getClass().getMethods())
		{
			if(m.getAnnotation(Trigger.class) != null)
			{
				Class<?>[] types = m.getParameterTypes();
				if(types.length != 2)continue;
				if(!types[0].isAssignableFrom(source.getClass()))continue;
				if(!types[1].isAssignableFrom(event.getClass()))continue;
				
				try {
					m.setAccessible(true);
					m.invoke(this, source, event);
				} catch (Exception e)
				{
					Logger.log(e);
				}
			}
		}
	}
	
	@Trigger
	public void onUse(EntityCard source, Event.TryUseCard tryUse)
	{
		if(source != tryUse.card)return;
		Logger.log("trying use of creature card " + name);
		if(tryUse.card == source && tryUse.slot != null && tryUse.slot.creature == null && tryUse.slot.player == tryUse.user)
		{
			tryUse.valid = true;
			new EntityCreature(tryUse.slot, (Creature) tryUse.card.card).summonOn(tryUse.slot);
		}
	}
}
