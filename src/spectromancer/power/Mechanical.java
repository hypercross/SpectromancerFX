package spectromancer.power;

import spectromancer.SpCreature;
import spectromancer.SpPower;
import spectromancer.SpSpell;
import spectromancer.Trigger;
import entity.EntityCard;
import entity.EntityCreature;
import game.DamageType;
import game.Event;

@SuppressWarnings("unused")
public class Mechanical extends SpPower{
	public static Mechanical instance = new Mechanical();

	private Mechanical() {
		super("Mechanical",8);
		
		this.cards[0] = new SpSpell("Overtime", 0, this)
		{
			@Trigger
			public void onUse(EntityCard card, Event.TryUseCard use)
			{
				if(use.card != card)return;
				
				use.valid = true;
				use.user.regeneratePower(this.getPower(), 1);
			}
		};
		
		this.cards[1] = new SpCreature("Dwarven Rifleman", 2, 4, 17, this)
		{
			@Trigger
			public void onSummoned(EntityCreature c, Event.CreatureSummoned e)
			{
				if(e.creature.slot.player != c.slot.player)
					e.creature.damage(4, DamageType.Ability);
			}
		};
		
		this.cards[2] = new SpCreature("Dwarven Craftsman", 3, 2, 17, this)
		{
			@Trigger
			public void onTurnStart(EntityCreature c, Event.TurnStart ts)
			{
				if(ts.player == c.slot.player)ts.player.regeneratePower(this.getPower(), 1);
			}
		};
		
		this.cards[3] = new SpCreature("Ornithopter", 4, 4, 24, this)
		{
			@Trigger
			public void onTurnStart(EntityCreature c, Event.TurnStart ts)
			{
				if(ts.player == c.slot.player)
				{
					for(EntityCreature creature : EntityCreature.getCreaturesOf(ts.player.getOpponent()))
						creature.damage(2, DamageType.Ability);
				}
			}
		};
		
		this.cards[4] = new SpCreature("Steel Golem", 5, 6, 20, this)
		{
			@Trigger
			public void onDamage(EntityCreature c, Event.Damage d)
			{
				if(d.creature == c)
				{
					if(d.type == DamageType.Ability || d.type == DamageType.Spell )
						d.modifier -= 100;
					else d.modifier -=1;
				}
			}
		};
		
		this.cards[5] = new SpCreature("Cannon" , 6, 8, 29, this)
		{
			@Trigger
			public void onTurnStart(EntityCreature c, Event.TurnStart ts)
			{
				if(ts.player == c.slot.player)
				{
					EntityCreature most_life = null;
					for(EntityCreature creature : EntityCreature.getCreaturesOf(ts.player.getOpponent()))
						if(most_life == null || most_life.hp < creature.hp)most_life = creature;
					if(most_life != null)most_life.damage(8, DamageType.Ability);
				}
			}
		};
		
		this.cards[6] = new SpSpell("Cannonade", 7, this)
		{
			@Trigger
			public void onUse(EntityCard card, Event.TryUseCard use)
			{
				if(use.card != card)return;
				use.valid = true;
				
				for(EntityCreature creature : EntityCreature.getCreaturesOf(use.user.getOpponent()))
					creature.damage(19, DamageType.Spell);
			}
		};
		
		this.cards[7] = new SpCreature("Steam Tank", 8, 6, 54, this)
		{
			@Trigger
			public void onDamage(EntityCreature tank, Event.CreatureSummoned summoned)
			{
				if(summoned.creature == tank)
					for(EntityCreature creature : EntityCreature.getCreaturesOf(tank.slot.player.getOpponent()))
						creature.damage(12, DamageType.Ability);
			}
		};
	}
}
