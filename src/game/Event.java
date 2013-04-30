package game;

import java.util.ArrayList;

import entity.Damageable;
import entity.EntityCard;
import entity.EntityCreature;
import entity.EntityPlayer;
import entity.EntitySlot;


public class Event {

	public boolean isSilentOnStart()
	{
		return false;
	}
	
	public boolean isSilentOnEnd()
	{
		return true;
	}
	
	public static class Damage extends Event
	{
		public final DamageType type;
		public final int amount;
		
		public EntityPlayer player;
		public EntityCreature creature;
		
		public int modifier = 0;
		public int override = -1;
		
		private Damage(int amount, DamageType type, EntityPlayer player, EntityCreature creature)
		{
			this.amount = amount;
			this.type   = type;
			this.creature = creature;
			this.player = player;
		}
		
		public Damage(int amount, DamageType type, EntityPlayer player)
		{
			this(amount,type,player, null);
		}
		
		public Damage(int amount, DamageType type, EntityCreature creature)
		{
			this(amount,type,null, creature);
		}
		
		public int result()
		{
			int result = amount;
			if(override >= 0)result = override;
			result += modifier;
			return Math.max(result, 0);
		}
		
		public String toString()
		{
			return (player == null ? creature : player) + " takes " + result() + " damage.";
		}
	}
	
	public static class TurnEnd extends Event
	{
		public final EntityPlayer player;
		
		public TurnEnd(EntityPlayer player)
		{
			this.player = player;
		}
		
		public String toString()
		{
			return player + "'s turn ended.";
		}
	}

	public static class TurnStart extends Event
	{
		public final EntityPlayer player;
		
		public TurnStart(EntityPlayer player)
		{
			this.player = player;
		}
		
		public String toString()
		{
			return player + "'s turn started.";
		}
	}
	
	public static class GameStart extends Event
	{
		public final EntityPlayer player;
		
		public GameStart(EntityPlayer player)
		{
			this.player = player;
		}
		
		public String toString()
		{
			return "Game Started.";
		}
	}
	
	public static class CreatureDied extends Event
	{
		public final EntityCreature creature;
		
		public CreatureDied(EntityCreature creature)
		{
			this.creature = creature;
		}
		
		public String toString()
		{
			return creature + " died.";
		}
	}
	
	public static class CreatureSummoned extends Event
	{
		public final EntityCreature creature;
		
		public CreatureSummoned(EntityCreature creature)
		{
			this.creature = creature;
		}
		
		public String toString()
		{
			return creature + " is summoned on " + creature.slot + ".";
		}
	}
	
	public static class GetAttack extends Event
	{
		public final EntityCreature attacker;
		public final int original;
		
		public int modifier = 0;
		public int override = -1;
		
		public GetAttack(EntityCreature attacker)
		{
			this.attacker = attacker;
			this.original = attacker.card.getAtk();
		}
		
		public int result()
		{
			int amount = original;
			if(override >= 0)amount = override;
			amount += modifier;
			return Math.max(amount, 0);
		}
		
		public boolean isSilentOnStart()
		{
			return true;
		}
	}
	
	public static class PerformAttack extends Event
	{
		public final EntityCreature attacker;
		public final int attack;
		public final ArrayList<Damageable> targets;
		
		public PerformAttack(EntityCreature attacker, int attack)
		{
			this.attacker = attacker;
			this.attack = attack;
			this.targets = new ArrayList<Damageable>();
			
			EntitySlot slot = attacker.slot.getOpposite();
			targets.add(attacker.isBlocked() ? slot.creature : slot.player);
		}
		
		public String toString()
		{
			return attacker + " attacks " + targets + " (atk = " + attack + ").";
		}
		
		public boolean isSilentOnStart()
		{
			return true;
		}
		
		public boolean isSilentOnEnd()
		{
			return false;
		}
	}
	
	public static class PerformedAttack extends Event
	{
		public final EntityCreature attacker;
		public final int attack;
		public final ArrayList<Damageable> targets;
		
		public PerformedAttack(PerformAttack perform) {
			attacker = perform.attacker;
			attack = perform.attack;
			targets = perform.targets;
		}
		
		public boolean isSilentOnStart()
		{
			return true;
		}
	}
	
	public static class TryUseCard extends Event
	{
		public final EntityCard card;
		public final EntityPlayer user;
		public final EntitySlot slot;
		
		public boolean valid = false;
		
		public TryUseCard(EntityCard card, EntityPlayer user, EntitySlot slot)
		{
			this.card = card;
			this.user = user;
			this.slot = slot;
		}
		
		public boolean isSilentOnStart()
		{
			return !valid;
		}
		
		public boolean isSilentOnEnd()
		{
			return !valid;
		}
		
		public String toString()
		{
			return user+ " uses " + card + " on " + slot + ".";
		}
	}
	
	public static class UsedCard extends Event
	{
		public final Card card;
		public final EntityPlayer user;
		public final EntitySlot slot;
		
		public UsedCard(Card card, EntityPlayer user, EntitySlot slot)
		{
			this.card = card;
			this.user = user;
			this.slot = slot;
		}
		
		public String toString()
		{
			return user+ " used " + card + " on " + slot + ".";
		}
	}
}
