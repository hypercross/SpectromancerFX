package game;

import entity.TriggerCarrier;

public interface Triggerable {
	public void trigger(TriggerCarrier source, Event event);
}
