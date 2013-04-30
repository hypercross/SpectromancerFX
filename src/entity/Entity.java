package entity;

import provider.EventFactory;

public abstract class Entity {

	public void join()
	{
		if(this instanceof TriggerCarrier)EventFactory.add((TriggerCarrier)this);
	}
	
	public void remove()
	{
		if(this instanceof TriggerCarrier)EventFactory.remove((TriggerCarrier)this);
	}
}
