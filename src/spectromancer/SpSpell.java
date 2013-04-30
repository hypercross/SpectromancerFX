package spectromancer;

import entity.TriggerCarrier;
import game.Card;
import game.Event;
import game.Power;

import java.lang.reflect.Method;

import util.Logger;

public class SpSpell implements Card{

	int cost;
	Power power;
	private String name;
	
	public SpSpell(String name,int cost, Power power)
	{
		this.name = name;
		this.cost = cost;
		this.power = power;
	}
	
	public String toString()
	{
		return name;
	}
	
	@Override
	public int getCost() {
		return cost;
	}

	@Override
	public Power getPower() {
		return power;
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
}
