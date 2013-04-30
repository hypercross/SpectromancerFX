package spectromancer.power;

import spectromancer.SpCreature;
import spectromancer.SpPower;
import spectromancer.SpSpell;
import spectromancer.Trigger;
import util.Logger;
import entity.EntityCard;
import entity.EntityCreature;
import game.Creature;
import game.DamageType;
import game.Event;

@SuppressWarnings("unused")
public class Holy extends SpPower{
	public static Holy instance = new Holy();

	private Holy() {
		super("Holy",8);
		
		this.cards[0] = new SpCreature("Paladin", 1, 4, 9, this)
		{
			@Trigger
			public void onSummoned(EntityCreature creature, Event.CreatureSummoned summon)
			{
				if(summon.creature == creature)
				{
					for(EntityCreature c : EntityCreature.getCreaturesOf(creature.slot.player))
					{
						c.heal(4);
					}
				}
			}
		};
		
		this.cards[1] = new SpCreature("Monk", 2, 4, 13, this)
		{
			@Trigger
			public void onDeath(EntityCreature creature, Event.CreatureDied event)
			{
				if(event.creature == creature)
				{
					creature.slot.player.regeneratePower(this.getPower(), 2);
				}
			}
		};
		
		this.cards[2] = new SpCreature("Holy Guard", 3, 4, 23, this)
		{
			@Trigger
			public void onDamage(EntityCreature c, Event.Damage e)
			{
				if(e.creature != null && c.isNeighbor(e.creature))e.modifier-=2;
			}
		};
		
		this.cards[3] = new SpSpell("Divine Justice", 4, this)
		{
			@Trigger
			public void onUse(EntityCard c, Event.TryUseCard use)
			{
				if(c != use.card)return;
				if(use.slot == null || use.slot.player != use.user || use.slot.creature == null)return;
				
				use.slot.creature.heal(12);
				
				for(EntityCreature ac : EntityCreature.getCreaturesOf(use.user.getOpponent()))
				{
					ac.damage(12, DamageType.Spell);
				}
				
				for(EntityCreature ac : EntityCreature.getCreaturesOf(use.user))
				{
					if(ac != use.slot.creature)ac.damage(12, DamageType.Spell);
				}
				use.valid = true;
			}
		};
		
		this.cards[4] = new SpSpell("Divine Intervention", 5, this)
		{
			@Trigger
			public void onUse(EntityCard c, Event.TryUseCard use)
			{
				if(c != use.card)return;
				use.valid = true;
				use.user.regeneratePower(Fire.instance, 2);
				use.user.regeneratePower(Water.instance, 2);
				use.user.regeneratePower(Air.instance, 2);
				use.user.regeneratePower(Earth.instance, 2);
				use.user.heal(10);
			}
		};
		
		this.cards[5] = new SpSpell("Wrath of God", 6, this)
		{
			@Trigger
			public void onUse(EntityCard c, Event.TryUseCard use)
			{
				if(c != use.card)return;
				use.valid = true;
				
				int amount = 0;
				
				for(EntityCreature ac : EntityCreature.getCreaturesOf(use.user.getOpponent()))
				{
					if(!ac.damage(12, DamageType.Spell))amount++;
				}
				
				use.user.regeneratePower(this.getPower(), amount);
			}	
		};
		
		this.cards[6] = new SpCreature("Angel", 7, 8, 42, this)
		{
			@Trigger
			public void onSummoned(EntityCreature c, Event.CreatureSummoned e)
			{
				if(c == e.creature)
					c.slot.player.regeneratePower(this.getPower(), 3);
			}
		};
		
		this.cards[7] = new SpCreature("Archangel", 8, 8, 48, this)
		{
			@Trigger
			public void onSummoned(EntityCreature c, Event.CreatureSummoned e)
			{
				if(c == e.creature)
				{
					for(EntityCreature ac : EntityCreature.getCreaturesOf(c.slot.player))
					{
						ac.heal(ac.card.getHp());
					}
				}
			}
		};
	}
}
