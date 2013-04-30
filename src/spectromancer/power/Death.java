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
public class Death extends SpPower{
	public static Death instance = new Death();

	private SpSpell rage_of_souls = new SpSpell("Rage of Souls", 7, this)
	{
		@Trigger
		public void onUse(EntityCard card, Event.TryUseCard use)
		{
			if(card != use.card)return;
			
			int amount = 9 + use.user.getPowerValue(this.getPower());
			int heal = 0;
			
			for(EntityCreature c : EntityCreature.getCreaturesOf(use.user.getOpponent()))
			{
				boolean killed = c.damage(amount, DamageType.Spell);
				if(killed)heal+=2;
			}
			use.user.heal(heal);
		}
	};
	
	private Death() {
		super("Death",8);
		
		this.cards[0] = new SpSpell("Dark Ritual", 1, this)
		{
			@Trigger
			public void onUse(EntityCard card, Event.TryUseCard use)
			{
				if(card != use.card)return;
				
				for(EntityCreature entityCreature : EntityCreature.getCreaturesOf(use.user.getOpponent()))
				{
					entityCreature.damage(3,DamageType.Spell);
				}
				
				for(EntityCreature entityCreature : EntityCreature.getCreaturesOf(use.user))
				{
					entityCreature.heal(3);
				}
				
				use.valid = true;
			}
		};
		
		this.cards[1] = new SpSpell("Cursed Fog", 2, this)
		{
			@Trigger
			public void onUse(EntityCard card, Event.TryUseCard use)
			{
				if(card != use.card)return;
				
				for(EntityCreature entityCreature : EntityCreature.getCreaturesOf(use.user.getOpponent()))
				{
					entityCreature.damage(12,DamageType.Spell);
				}
				
				for(EntityCreature entityCreature : EntityCreature.getCreaturesOf(use.user))
				{
					entityCreature.damage(12,DamageType.Spell);
				}
				
				use.user.getOpponent().damage(3,DamageType.Spell);
				
				use.valid = true;
			}
		};
		
		this.cards[2] = new SpCreature("Banshee", 3, 4, 21, this)
		{
			@Trigger
			public void onSummoned(EntityCreature banshee, Event.CreatureSummoned summon)
			{
				if(summon.creature == banshee)
				{
					EntityCreature c = banshee.slot.getOpposite().creature;
					if(c != null)c.damage( (c.hp + 1)/2, DamageType.Ability);
				}
			}
		};
		
		this.cards[3] = new SpCreature("Emissary of Dorlak", 4, 7, 48, this)
		{
			@Trigger
			public void onUse(EntityCard source, Event.TryUseCard tryUse)
			{
				if(source != tryUse.card)return;
				Logger.log("trying use of Emissary of Dorlak");
				if(tryUse.card == source && tryUse.slot != null && tryUse.slot.creature != null && tryUse.slot.player == tryUse.user)
				{
					tryUse.valid = true;
					new EntityCreature(tryUse.slot, (Creature) tryUse.card.card).summonOn(tryUse.slot);
				}
			}
		};
		
		this.cards[4] = new SpSpell("Blood Ritual",5, this)
		{
			@Trigger
			public void onUse(EntityCard card, Event.TryUseCard use)
			{
				if(card != use.card)return;
				if(use.slot == null || use.slot.creature == null || use.slot.player != use.user)return;
				
				int amount = use.slot.creature.hp;
				use.slot.creature.die();
				for(EntityCreature c : EntityCreature.getCreaturesOf(use.user.getOpponent()))
					c.damage(amount, DamageType.Spell);
				
				use.valid = true;
			}
			
		};
		
		this.cards[5] = new SpCreature("Keeper of Death", 6, 7, 35, this)
		{
			@Trigger
			public void onDeath(EntityCreature c, Event.CreatureDied die)
			{
				if(die.creature.slot.player != c.slot.player)
				{
					c.slot.player.regeneratePower(this.getPower(), 1);
				}
			}
		};
		
		this.cards[6] = new SpSpell("Drain Souls", 7, this)
		{
			@Trigger
			public void onUse(EntityCard card, Event.TryUseCard use)
			{
				if(card != use.card)return;
				int amount = 0 ;
				
				for(EntityCreature c : EntityCreature.getCreaturesOf(use.user.getOpponent()))
				{
					c.die();
					amount += 2;
				}
				
				for(EntityCreature c : EntityCreature.getCreaturesOf(use.user))
				{
					c.die();
					amount += 2;
				}
				
				use.user.heal(amount);
				use.user.takeCard(card);
				use.user.giveCard(new EntityCard(rage_of_souls, use.user));
				use.valid = true;
			}
		};
		
		this.cards[7] = new SpCreature("Master Lich", 8, 8, 46, this)
		{
			@Trigger
			public void onSummoned(EntityCreature lich, Event.CreatureSummoned summon)
			{
				if(summon.creature == lich)
				{
					for(EntityCreature c : EntityCreature.getCreaturesOf(lich.slot.getOpposite().player))
					{
						c.damage(8, DamageType.Ability);
					}
				}
			}
			
			@Trigger
			public void onDamage(EntityCreature lich, Event.PerformedAttack attack)
			{
				if(attack.attacker == lich && attack.targets.contains(lich.slot.player.getOpponent()))
				{
					lich.slot.player.regeneratePower(this.getPower(), 2);
				}
			}
		};
	}

}
