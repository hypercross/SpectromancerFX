package entity;

import provider.EventFactory;
import game.Creature;
import game.DamageType;
import game.Event;
import game.Triggerable;

public class EntityCreature extends Entity implements TriggerCarrier, Damageable{

	public int attack;
	public int hp,maxhp;
	public EntitySlot slot;
	public boolean summonSickness;
	
	public Creature card;
	
	public EntityCreature(EntitySlot slot, Creature card)
	{
		this.attack = card.getAtk();
		this.hp = card.getHp();
		this.maxhp = hp;
		this.slot = slot;
		this.card = card;
		this.summonSickness = true;
	}
	
	public String toString()
	{
		return card.toString();
	}
	
	public void performAttack()
	{
		Event.GetAttack attack = new Event.GetAttack(this);
		EventFactory.post(attack);
		
		Event.PerformAttack perform = new Event.PerformAttack(this, attack.result());
		EventFactory.post(perform);
		
		for(Damageable target : perform.targets)target.damage(attack.result(), DamageType.Attack);
		
		Event.PerformedAttack performed = new Event.PerformedAttack(perform);
		EventFactory.post(performed);
	}
	
	public boolean damage(int x, DamageType type)
	{
		Event.Damage damage = new Event.Damage(x, type, this);
		EventFactory.post(damage);
		
		hp -= damage.result();
		
		if(hp <= 0)
		{
			this.die();
			return true;
		}
		return false;
	}
	
	public void summonOn(EntitySlot slot)
	{
		slot.creature = this;
		this.join();
		EventFactory.post(new Event.CreatureSummoned(this));
	}
	
	public void die()
	{
		slot.creature = null;
		this.remove();
		EventFactory.post(new Event.CreatureDied(this));
	}
	
	public boolean isOpposite(EntityCreature creature)
	{
		return this.slot.getOpposite() == creature.slot;
	}
	
	public boolean isAlly(EntityCreature creature)
	{
		return this.slot.player == creature.slot.player;
	}
	
	public boolean isNeighbor(EntityCreature creature)
	{
		for(EntitySlot slot : creature.slot.getNeighbors())
		{
			if(slot == this.slot)return true;
		}
		return false;
	}
	
	public boolean isBlocked()
	{
		return this.slot.getOpposite().creature != null;
	}

	public int getAttack() {
		Event.GetAttack attack = new Event.GetAttack(this);
		EventFactory.post(attack);
		return attack.result();
	}

	@Override
	public Triggerable getTriggerable() {
		return card;
	}

	public void heal(int i) {
		this.hp += i;
		if(this.hp > this.maxhp)this.hp = this.maxhp;
	}
	
	public static EntityCreature[] getCreaturesOf(EntityPlayer player)
	{
		EntitySlot slots[] = EntitySlot.getSlotsOf(player);
		int size = 0;		
		
		for(EntitySlot slot : slots)if(slot.creature != null)size++;
		
		EntityCreature[] ec = new EntityCreature[size];
		size = 0;
		
		for(EntitySlot slot : slots)
		{
			if(slot.creature != null)ec[size++] = slot.creature;
		}
		
		return ec;
	}
}
