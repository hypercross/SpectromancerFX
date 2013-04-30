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
public class Fire extends SpPower{
	public static Fire instance = new Fire();

	private Fire() {
		super("Fire", 12);
		
		this.cards[0] = new SpCreature("Goblin Berserker", 1, 4, 16, this)
		{
			@Trigger
			public void onTurnStart(EntityCreature creature, Event.TurnStart turnStart) {
				if(turnStart.player == creature.slot.player)
				for(EntitySlot slot : creature.slot.getNeighbors())
					if(slot.creature != null)
						slot.creature.damage(2, DamageType.Ability);
			}
		};
		
		this.cards[1] = new SpCreature("Wall of Fire", 2, 0, 5, this)
		{
			@Trigger
			public void onSummoned(EntityCreature creature, Event.CreatureSummoned summon)
			{
				if(summon.creature == creature)
				for(EntityCreature c : EntityCreature.getCreaturesOf(summon.creature.slot.player.getOpponent()))
					c.damage(5, DamageType.Ability);
			}
		};
		
		this.cards[2] = new SpCreature("Priest of Fire", 3, 3, 13, this)
		{
			@Trigger
			public void onTurnStart(EntityCreature creature, Event.TurnStart turnStart) 
			{
				if(turnStart.player == creature.slot.player)
					creature.slot.player.regeneratePower(Fire.instance, 1);
			}
		};
		
		this.cards[3] = new SpCreature("Fire Drake", 4, 4, 18, this)
		{
			@Trigger
			public void onSummoned(EntityCreature creature, Event.CreatureSummoned summon)
			{
				if(creature == summon.creature)
				creature.summonSickness = false;
			}
		};
		
		this.cards[4] = new SpCreature("Orc Chieftain", 5, 3, 16, this)
		{
			@Trigger
			public void increaseAttack(EntityCreature creature, Event.GetAttack atk)
			{
				if(creature.isNeighbor(atk.attacker))
					atk.modifier += 2;
			}
		};
		
		this.cards[5] = new SpSpell("Flame Wave", 6, this)
		{
			@Trigger
			public void dealDamage(EntityCard card, Event.TryUseCard use)
			{
				if(card != use.card)return;
				for(EntitySlot slot : EntitySlot.getSlotsOf(use.user.getOpponent()))
				{
					if(slot.creature != null)slot.creature.damage(9, DamageType.Spell);
				}
				use.valid = true;
			}
		};
		
		this.cards[6] = new SpCreature("Minotaur Commander", 7, 6, 20, this)
		{
			@Trigger
			public void increaseAttack(EntityCreature creature, Event.GetAttack atk)
			{
				if(creature.isAlly(atk.attacker))
					atk.modifier += 1;
			}
		};
		
		this.cards[7] = new SpCreature("Bargul", 8,8,25,this)
		{
			@Trigger
			public void damageAll(EntityCreature bargul, Event.CreatureSummoned event)
			{
				if(event.creature == bargul)
				{
					for(EntitySlot slot : EntitySlot.getSlotsOf(bargul.slot.player.getOpponent()))
					{
						if(slot.creature != null)slot.creature.damage(4, DamageType.Ability);
					}
					for(EntitySlot slot : EntitySlot.getSlotsOf(bargul.slot.player))
					{
						if(slot.creature != bargul && slot.creature != null)slot.creature.damage(4, DamageType.Ability);
					}
				}
			}
		};
		
		this.cards[8] = new SpSpell("Inferno", 9, this)
		{
			@Trigger
			public void onUse(EntityCard card, Event.TryUseCard use)
			{
				if(card != use.card)return;
				if(use.slot!=null && use.slot.creature != null && use.slot.player == use.user)
				{
					use.slot.creature.damage(18,DamageType.Spell);
					for(EntitySlot slot : use.slot.getAllies())
					{
						if(slot != use.slot)slot.creature.damage(10, DamageType.Spell);
					}
					use.valid = true;
				}
				
			}
		};
		
		this.cards[9] = new SpCreature("Fire Elemental", 10, -1, 37, this)
		{
			@Trigger
			public void getAtk(EntityCreature fireElemental, Event.GetAttack atk)
			{
				if(atk.attacker == fireElemental)
					atk.override = fireElemental.slot.player.getPowerValue(this.getPower());
			}
			
			@Trigger
			public void onTurnStart(EntityCreature creature, Event.TurnStart turnStart) 
			{
				if(turnStart.player == creature.slot.player)
					creature.slot.player.regeneratePower(Fire.instance, 1);
			}
		};
		
		this.cards[10] = new SpSpell("Armageddon", 11, this)
		{
			@Trigger
			public void tryUse(EntityCard card, Event.TryUseCard use)
			{
				if(card != use.card)return;
				
				int amount = use.user.getPowerValue(Fire.instance);
				
				for(EntitySlot slot : EntitySlot.getSlotsOf(use.user.getOpponent()))
				{
					if(slot.creature != null)slot.creature.damage(amount, DamageType.Spell);
				}
				for(EntitySlot slot : EntitySlot.getSlotsOf(use.user))
				{
					if(slot.creature != null)slot.creature.damage(amount, DamageType.Spell);
				}
				
				use.user.getOpponent().damage(amount, DamageType.Spell);
				use.valid = true;
			}
		};
		
		this.cards[11] = new SpCreature("Dragon", 12, 9, 40, this)
		{
			@Trigger
			public void increaseDamage(EntityCreature creature, Event.Damage damage)
			{
				boolean valid = false;
				valid |= damage.player == creature.slot.player.getOpponent();
				valid |= damage.creature != null && damage.creature.slot.player == creature.slot.player.getOpponent();
				
				if(valid)
					damage.modifier += damage.amount/2;
			}
		};
	}	
}