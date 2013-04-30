package provider;

import java.util.Comparator;
import java.util.TreeSet;

import entity.EntityCard;
import entity.EntityCreature;
import entity.EntityPlayer;
import entity.TriggerCarrier;
import game.Event;

public class EventFactory {
	
	private static TreeSet<TriggerCarrier> carriers  = new TreeSet<TriggerCarrier>(new TriggerPriority());
	
	public static void add(TriggerCarrier carrier)
	{
		carriers.add(carrier);
	}
	
	public static void remove(TriggerCarrier carrier)
	{
		carriers.remove(carrier);
	}
	
	private static class TriggerPriority implements Comparator<TriggerCarrier>
	{
		private int priority(TriggerCarrier tc)
		{
			if(tc instanceof EntityPlayer)return 1;
			if(tc instanceof EntityCreature)return 2;
			if(tc instanceof EntityCard)return 3;
			return 4;
		}
		
		@Override
		public int compare(TriggerCarrier a, TriggerCarrier b) {
			int level = priority(a) - priority(b);
			if(level != 0)return level;
			
			return a.hashCode() - b.hashCode();
		}
	}

	public synchronized static void post(Event event)
	{
		if(!event.isSilentOnStart())GameThread.announce(event.toString());
		for(TriggerCarrier tc : carriers.toArray(new TriggerCarrier[carriers.size()]))tc.getTriggerable().trigger(tc, event);
		if(!event.isSilentOnEnd())GameThread.announce(event.toString());
	};
}
