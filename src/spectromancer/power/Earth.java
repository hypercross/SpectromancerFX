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
public class Earth extends SpPower{
	public static Earth instance = new Earth();

	private SpCreature small_spider = new SpCreature("Forest Spider", 0, 2, 11, this);

	private Earth()
	{
		super("Earth", 12 );

		this.cards[0] = new SpCreature("Elven Healer" , 1, 2, 12, this)
		{
			@Trigger
			public void onTurnStart(EntityCreature elf, Event.TurnStart start)
			{
				if(elf.slot.player == start.player)
					start.player.heal(3);
			}
		};

		this.cards[1] = new SpSpell("Nature's Ritual" , 2, this)
		{
			@Trigger
			public void onuse(EntityCard card, Event.TryUseCard use)
			{
				if(card != use.card)return;
				if(use.slot != null && use.slot.player == use.user && use.slot.creature != null)
				{
					use.slot.creature.heal(8);
					use.user.heal(8);
					use.valid = true;
				}
			}
		};

		this.cards[2] = new SpCreature("Forest Sprite", 3, 1 ,22, this)
		{
			@Trigger
			public void onattack(EntityCreature sprite, Event.PerformAttack attack)
			{
				if(attack.attacker == sprite)
				{
					for(EntitySlot slot : EntitySlot.getSlotsOf(sprite.slot.player.getOpponent()))
						if(slot.creature!= null && !attack.targets.contains(slot.creature))
							attack.targets.add(slot.creature);

					if(!attack.targets.contains(sprite.slot.player.getOpponent()))
						attack.targets.add(sprite.slot.player.getOpponent());
				}
			}
		};

		this.cards[3] = new SpSpell("Rejuvenation", 4, this)
		{
			@Trigger
			public void onuse(EntityCard card, Event.TryUseCard use)
			{
				if(card != use.card)return;
				use.user.heal(2 * use.user.getPowerValue(this.getPower()));
				use.valid = true;
			}
		};

		this.cards[4] = new SpCreature("Elf Hermit", 5, 1, 13, this)
		{
			@Trigger
			public void onturnstart(EntityCreature elf, Event.TurnStart ts)
			{
				if(ts.player == elf.slot.player)
					ts.player.regeneratePower(this.getPower(), 2);
			}
		};

		this.cards[5] = new SpSpell("Nature's Fury", 6, this)
		{
			@Trigger
			public void onuse(EntityCard card, Event.TryUseCard use)
			{
				if(card != use.card)return;

				int a=0,b=0;
				for(EntitySlot slot : EntitySlot.getSlotsOf(use.user))
				{
					if(slot.creature != null)
					{
						int c = slot.creature.getAttack();

						if(c > a)
						{
							b = a ;
							a = c ;
							continue;
						}
						if(c > b)
						{
							b = c;
							continue;
						}
					}
				}

				if(a + b > 0)
				{
					use.user.getOpponent().damage(a + b, DamageType.Spell);
					use.valid = true;
				}
			}
		};

		this.cards[6] = new SpCreature("Giant Spider", 7, 4, 21, this)
		{
			@Trigger
			public void onSummon(EntityCreature c, Event.CreatureSummoned summon)
			{
				if(c == summon.creature)
				{
					for(EntitySlot slot : c.slot.getNeighbors())
					{
						if(slot.creature == null)
						{
							new EntityCreature(slot, small_spider).summonOn(slot);
						}
					}
				}
			}
		};

		this.cards[7] = new SpCreature("Troll", 8, 6, 25, this)
		{
			@Trigger
			public void onTurnStart(EntityCreature c, Event.TurnStart start)
			{
				if(c.slot.player == start.player)
					c.heal(4);
			}
		};

		this.cards[8] = new SpSpell("Stone Rain", 9, this)
		{
			@Trigger
			public void onuse(EntityCard card, Event.TryUseCard use)
			{
				if(card != use.card)return;

				for(EntitySlot slot : EntitySlot.getSlotsOf(use.user.getOpponent()))
				{
					if(slot.creature != null)slot.creature.damage(25, DamageType.Spell);
				}
				for(EntitySlot slot : EntitySlot.getSlotsOf(use.user))
				{
					if(slot.creature != null)slot.creature.damage(25, DamageType.Spell);
				}
				use.valid = true;
			}
		};

		this.cards[9] = new SpCreature("Earth Elemental", 10, -1, 49, this)
		{
			@Trigger
			public void getAtk(EntityCreature elemental, Event.GetAttack atk)
			{
				if(atk.attacker == elemental)
					atk.override = elemental.slot.player.getPowerValue(this.getPower());
			}

			@Trigger
			public void onTurnStart(EntityCreature creature, Event.TurnStart turnStart) 
			{
				if(turnStart.player == creature.slot.player)
					creature.slot.player.regeneratePower(Earth.instance, 1);
			}
		};
		
		this.cards[10] = new SpCreature("Master Healer", 11, 3, 35, this)
		{
			@Trigger
			public void onTurnStart(EntityCreature creature, Event.TurnStart turnStart) 
			{
				if(turnStart.player == creature.slot.player)
					for(EntitySlot slot : creature.slot.getAllies())
					{
						if(slot.creature != null)slot.creature.heal(3);
					}
				turnStart.player.heal(3);
			}	
		};
		
		this.cards[11] = new SpCreature("Hydra", 12, 3, 40, this)
		{
			@Trigger
			public void onattack(EntityCreature hydra, Event.PerformAttack attack)
			{
				if(attack.attacker == hydra)
				{
					for(EntitySlot slot : EntitySlot.getSlotsOf(hydra.slot.player.getOpponent()))
						if(slot.creature!= null && !attack.targets.contains(slot.creature))
							attack.targets.add(slot.creature);

					if(!attack.targets.contains(hydra.slot.player.getOpponent()))
						attack.targets.add(hydra.slot.player.getOpponent());
				}
			}
			
			
			@Trigger
			public void onTurnStart(EntityCreature c, Event.TurnStart start)
			{
				if(c.slot.player == start.player)
					c.heal(4);
			}
		};
	}
}
