package spectromancer.power;

import spectromancer.SpCreature;
import spectromancer.SpPower;
import spectromancer.SpSpell;
import spectromancer.Trigger;
import entity.EntityCard;
import entity.EntityCreature;
import entity.EntitySlot;
import game.DamageType;
import game.Event;

@SuppressWarnings("unused")
public class Air extends SpPower{
	public static Air instance = new Air();
	
	private Air() {
		super("Air", 12);
		
		this.cards[0] = new SpCreature("Faerie Apprentice", 1, 4, 12, this)
		{
			@Trigger
			public void onDamage(EntityCreature faerie, Event.Damage dmg)
			{
				if(faerie.slot.player.getOpponent() == dmg.player && dmg.type == DamageType.Spell)
				{
					dmg.modifier+=1;
				}
			}
		};
		
		this.cards[1] = new SpCreature("Griffin", 2, 3, 15, this)
		{
			@Trigger
			public void onSummon(EntityCreature griffin, Event.CreatureSummoned summon)
			{
				if(summon.creature == griffin && griffin.slot.player.getPowerValue(getPower()) >= 5)
				{
					griffin.slot.player.getOpponent().damage(5, DamageType.Ability);
				}
			}
		};
		
		this.cards[2] = new SpSpell("Call to Thunder", 3, this)
		{
			@Trigger
			public void onUse(EntityCard card, Event.TryUseCard use)
			{
				if(card != use.card)return;
				if(use.slot != null && use.slot.creature != null && use.slot.player.getOpponent() == use.user)
				{
					use.slot.creature.damage(6,DamageType.Spell);
					use.slot.player.damage(6, DamageType.Spell);
					use.valid = true;
				}
			}
		};
		
		this.cards[3] = new SpCreature("Faerie Sage", 4, 4, 19 , this)
		{
			@Trigger
			public void onSummon(EntityCreature sage, Event.CreatureSummoned summon)
			{
				if(summon.creature == sage )
				{
					sage.slot.player.heal(Math.min(sage.slot.player.getPowerValue(Earth.instance),10));
				}
			}
		};
		
		this.cards[4] = new SpCreature("Wall of Lightning", 5, 0, 28, this)
		{
			@Trigger
			public void onTurnStart(EntityCreature wall, Event.TurnStart start)
			{
				if(start.player == wall.slot.player)
				{
					start.player.getOpponent().damage(4,DamageType.Ability);
				}
			}
		};
		
		this.cards[5] = new SpSpell("Lightning Bolt", 6, this)
		{
			@Trigger
			public void resolve(EntityCard card, Event.TryUseCard use)
			{
				if(card != use.card)return;
				use.user.getOpponent().damage(5 + use.user.getPowerValue(getPower()), DamageType.Spell);
				use.valid = true;
			}
		};
		
		this.cards[6] = new SpCreature("Phoenix", 7, 6, 20, this)
		{
			@Trigger
			public void onDeath(EntityCreature phoenix, Event.CreatureDied death)
			{
				if(phoenix == death.creature && phoenix.slot.player.getPowerValue(Fire.instance) > 9)
				{
					phoenix.summonOn(phoenix.slot);
				}
			}
		};
		
		this.cards[7] = new SpSpell("Chain Lightning", 8, this)
		{
			@Trigger
			public void resolve(EntityCard card, Event.TryUseCard use)
			{
				if(card != use.card)return;
				for(EntitySlot slot : EntitySlot.getSlotsOf(use.user.getOpponent()))
				{
					if(slot.creature != null)slot.creature.damage(9, DamageType.Spell);
				}
				use.user.getOpponent().damage(9, DamageType.Spell);
				use.valid = true;
			}
		};
		
		this.cards[8] = new SpCreature("Lightning Cloud", 9, 4, 20, this)
		{
			@Trigger
			public void onAttack(EntityCreature cloud, Event.PerformAttack perform)
			{
				if(perform.attacker != cloud)return;
				for(EntitySlot slot : EntitySlot.getSlotsOf(cloud.slot.player.getOpponent()))
					if(slot.creature!= null && !perform.targets.contains(slot.creature))
						perform.targets.add(slot.creature);
				
				if(!perform.targets.contains(cloud.slot.player.getOpponent()))
					perform.targets.add(cloud.slot.player.getOpponent());
			}
		};
		
		this.cards[9] = new SpSpell("Tornado", 10, this)
		{
			@Trigger
			public void onUse(EntityCard card, Event.TryUseCard use)
			{
				if(card != use.card)return;
				if(use.slot != null && use.slot.player != use.user && use.slot.creature != null)
				{
					use.slot.creature.die();
					use.valid = true;
				}
			}
		};
		
		this.cards[10] = new SpCreature("Air Elemental", 11, -1, 44, this)
		{
			@Trigger
			public void summoned(EntityCreature airElemental, Event.CreatureSummoned summon)
			{
				if(airElemental == summon.creature)
				{
					airElemental.slot.player.getOpponent().damage(8, DamageType.Ability);
				}
			}
			
			@Trigger
			public void getAtk(EntityCreature airElemental, Event.GetAttack atk)
			{
				if(atk.attacker == airElemental)
					atk.override = airElemental.slot.player.getPowerValue(this.getPower());
			}
			
			@Trigger
			public void onTurnStart(EntityCreature creature, Event.TurnStart turnStart) 
			{
				if(turnStart.player == creature.slot.player)
					creature.slot.player.regeneratePower(Air.instance, 1);
			}
		};
		
		this.cards[11] = new SpCreature("Titan", 12, 9, 40, this)
		{
			@Trigger
			public void summoned(EntityCreature titan, Event.CreatureSummoned summon)
			{
				if(titan == summon.creature)
				{
					EntityCreature target = titan.slot.getOpposite().creature;
					if(target!= null)target.damage(15, DamageType.Ability);
				}
			}
		};
	}

}
