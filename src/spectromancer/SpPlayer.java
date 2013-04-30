package spectromancer;

import entity.EntityPlayer;
import entity.EntitySlot;
import entity.TriggerCarrier;
import game.Event;
import game.PlayerClass;
import game.Power;

import java.lang.reflect.Method;

import provider.EntityAssembly;
import spectromancer.playerClass.Cleric;
import spectromancer.playerClass.Mechanician;
import spectromancer.playerClass.Necromancer;
import spectromancer.power.Air;
import spectromancer.power.Earth;
import spectromancer.power.Fire;
import spectromancer.power.Water;
import util.Logger;

public class SpPlayer implements PlayerClass{

	public static final PlayerClass[] vanilla = new PlayerClass[]{Necromancer.instance, Cleric.instance, Mechanician.instance};
	
	Power[] powers = new Power[]{Fire.instance, Water.instance, Air.instance, Earth.instance, null};

	public SpPlayer(Power p)
	{
		powers[4] = p;
	}

	public String toString()
	{
		return this.getClass().getSimpleName();
	}
	
	@Override
	public Power[] getPowers() {
		return powers;
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
	public void onTurnStart(EntityPlayer player, Event.TurnStart start)
	{
		if(player == start.player)
		{
			for(int i = 0 ; i < player.getPowers().length; i++)player.regeneratePower(i, 1);
			for(EntitySlot slot : EntitySlot.getSlotsOf(player))
				if(slot.creature != null)slot.creature.summonSickness = false;
		}
	}

	@Trigger
	public void onGameStart(EntityPlayer player, Event.GameStart start)
	{
		for(int i = 0 ; i < 4; i++)
		{
			int r = EntityAssembly.rand.nextInt(8);
			player.regeneratePower(i, 1);
			if(r<1)player.regeneratePower(i, 1);
			if(r<4)player.regeneratePower(i, 1);
			if(r<7)player.regeneratePower(i, 1);
			if(r<8)player.regeneratePower(i, 1);
		}
		
		player.regeneratePower(4, 1);
	}
}
