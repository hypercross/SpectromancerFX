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
import game.Power;

@SuppressWarnings("unused")
public class Water extends SpPower{
	public static Water instance = new Water();

	private Water() {
		super("Water", 12);
		
		this.cards[0] = new SpSpell("Meditation", 1, this)
		{
			@Trigger
			public void onUse(EntityCard meditation, Event.TryUseCard use)
			{
				if(meditation != use.card)return;
				use.user.regeneratePower(0, 1);
				use.user.regeneratePower(2, 1);
				use.user.regeneratePower(3, 1);
				use.valid = true;
				//TODO
			}
		};
		
		this.cards[1] = new SpCreature("Sea Sprite", 2, 5, 22, this)
		{
			@Trigger
			public void onTurnStart(EntityCreature creature, Event.TurnStart start)
			{
				if(creature.slot.player == start.player)
				{
					start.player.damage(2, DamageType.Ability);
				}
			}
		};
		
		this.cards[2] = new SpCreature("Merfolk Apostate", 3, 3, 10, this)
		{
			@Trigger
			public void onSummon(EntityCreature merfolk, Event.CreatureSummoned summon)
			{
				if(summon.creature == merfolk)
				{
					merfolk.slot.player.regeneratePower(Fire.instance, 2);
				}
			}
		};
		
		this.cards[3] = new SpCreature("Ice Golem" , 4, 4, 12, this)
		{
			@Trigger
			public void onDamaged(EntityCreature golem, Event.Damage damage)
			{
				if(damage.creature == golem && damage.type != DamageType.Attack)
				{
					damage.modifier -= 100;
				}
			}
		};
		
		this.cards[4] = new SpCreature("Merfolk Elder", 5, 3, 16, this)
		{
			@Trigger
			public void onTurnStart(EntityCreature merfolk, Event.TurnStart start)
			{
				if(start.player == merfolk.slot.player)
				{
					start.player.regeneratePower(2, 1);
				}
			}
		};
		
		this.cards[5] = new SpCreature("Ice Guard", 6, 3, 20, this)
		{
			@Trigger
			public void onDamage(EntityCreature guard, Event.Damage damage)
			{
				if(damage.player == guard.slot.player)
				{
					damage.modifier -= damage.amount/2;
				}
			}
		};
		
		this.cards[6] = new SpCreature("Giant Turtle", 7, 5, 16, this)
		{
			@Trigger
			public void onDamage(EntityCreature turtle, Event.Damage damage)
			{
				if(damage.creature == turtle)
				{
					damage.modifier -= 5;
				}
			}
		};
		
		this.cards[7] = new SpSpell("Acidic Rain", 8, this)
		{
			@Trigger
			public void onUse(EntityCard rain, Event.TryUseCard use)
			{
				if(rain != use.card)return;

				for(EntitySlot slot : EntitySlot.getSlotsOf(use.user.getOpponent()))
				{
					if(slot.creature != null)slot.creature.damage(15, DamageType.Spell);
				}
				for(EntitySlot slot : EntitySlot.getSlotsOf(use.user))
				{
					if(slot.creature != null)slot.creature.damage(15, DamageType.Spell);
				}
				
				for(Power pwr : use.user.getOpponent().getPowers())
					use.user.getOpponent().savagePower(pwr, 1);
				
				use.valid = true;
			}
		};
		
		this.cards[8] = new SpCreature("Merfolk Overlord", 9, 7, 34, this)
		{
			@Trigger
			public void onSummon(EntityCreature overlord, Event.CreatureSummoned summon)
			{
				if(overlord.isNeighbor(summon.creature))
					summon.creature.summonSickness = false;
			}
		};
		
		this.cards[9] = new SpCreature("Water Elemental", 10, -1, 38, this)
		{
			@Trigger
			public void getAtk(EntityCreature waterElemental, Event.GetAttack atk)
			{
				if(atk.attacker == waterElemental)
					atk.override = waterElemental.slot.player.getPowerValue(this.getPower());
			}
			
			@Trigger
			public void onTurnStart(EntityCreature creature, Event.TurnStart turnStart) 
			{
				if(turnStart.player == creature.slot.player)
					creature.slot.player.regeneratePower(Water.instance, 1);
			}
		};
		
		this.cards[10] = new SpCreature("Mind Master", 11, 6, 22, this)
		{
			@Trigger
			public void onTurnStart(EntityCreature master, Event.TurnStart turn)
			{
				if(turn.player == master.slot.player)
				{
					for(Power pwr : turn.player.getPowers())
						turn.player.regeneratePower(pwr, 1);
				}
			}
		};
		
		this.cards[11] = new SpCreature("Astral Guard", 12, 1, 17, this)
		{
			@Trigger
			public void onTurnStart(EntityCreature master, Event.TurnStart turn)
			{
				if(turn.player == master.slot.player.getOpponent())
				{
					for(Power pwr : turn.player.getPowers())
						turn.player.savagePower(pwr, 1);
				}
			}
		};
	}

}
