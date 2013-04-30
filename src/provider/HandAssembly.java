package provider;

import entity.EntityCard;
import entity.EntityPlayer;
import game.Card;
import game.Power;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import spectromancer.power.Air;
import spectromancer.power.Death;
import spectromancer.power.Earth;
import spectromancer.power.Fire;
import spectromancer.power.Water;

public class HandAssembly {
	private static final int[][] min = new int[][] {
		new int[] {1, 2, 5, 10},
		new int[] {1, 2, 4, 9},
		new int[] {1, 2, 4, 9},
		new int[] {1, 2, 6, 10},
		new int[] {1, 3, 5, 7},
	};
	private static final int[][] max = new int[][] {
		new int[] {4, 9, 11, 12},
		new int[] {3, 8, 11, 12},
		new int[] {3, 8, 11, 12},
		new int[] {4, 9, 11, 12},
		new int[] {2, 4, 6, 8},
	};

	private interface RuleEntry
	{
		public boolean check(Card[][] c);
	}

	private static class RepeatEntry implements RuleEntry
	{

		@Override
		public boolean check(Card[][] c) {
			for(Card[] cards : c)
				for(Card ca : cards)
				{
					boolean found = false;
					for(Card cb : cards)
					{
						if(ca == cb && found)return true;
						if(ca == cb)found = true;
					}
				}
			return false;
		}

	}

	private static class BanEntry  implements RuleEntry
	{
		int ca,cb;
		Power pa,pb;

		private static Power[] power_table = new Power[26];
		static
		{
			power_table['F' - 'A'] = Fire.instance;
			power_table['E' - 'A'] = Earth.instance;
			power_table['A' - 'A'] = Air.instance;
			power_table['W' - 'A'] = Water.instance;
			power_table['D' - 'A'] = Death.instance;
		}

		private static Power getPower(char c)
		{
			return power_table[c - 'A'];
		}

		public BanEntry(String s1, String s2)
		{
			this(
					getPower(s1.charAt(0)), Integer.parseInt(s1.substring(1)),
					getPower(s2.charAt(0)), Integer.parseInt(s2.substring(1))
					);
		}

		public BanEntry(Power pa, int ca,Power pb, int cb)
		{
			this.pa = pa;
			this.pb = pb;
			this.ca = ca;
			this.cb = cb;
		}

		public boolean check(Card[][] all)
		{
			for(Card[] cards : all)
				for(Card c1 : cards)
				{
					if(c1.getCost() != ca)continue;
					if(c1.getPower()!= pa)continue;

					for(Card[] cards_other : all)
						for(Card c2 : cards_other)
						{
							if(c2.getCost() != cb)continue;
							if(c2.getPower() != pb)continue;
							return true;
						}
				}
			return false;
		}
	}

	private static class SecureEntry implements RuleEntry
	{
		int min,max;
		Card[] list;

		public SecureEntry(int min, int max, String... strs)
		{
			this.max = max;
			this.min = min;
			
			list =new Card[strs.length];
			
			for(int i = 0 ; i < list.length;i ++)
			{
				list[i] = BanEntry.getPower( strs[i].charAt(0)).getCard(Integer.parseInt(strs[i].substring(1)) - 1);
			}
		}
		
		public SecureEntry(int min, int max, Card...cards)
		{
			list = cards.clone();
			this.max = max;
			this.min = min;
		}

		public boolean check(Card[][] cards)
		{
			int count = 0;

			for(Card ca : list)
				for(Card[] all : cards)
					for(Card cb : all)
					{
						if(ca == cb)
						{
							count++;
							break;
						}
					}

			return count < min|| count > max;
		}
	}

	private static class HandGenerator
	{
		ArrayList<RuleEntry> rules = new ArrayList<RuleEntry>();
		Random rand = new Random();
		boolean[] used = new boolean[12];
		public HandGenerator()
		{
			rules.add(new RepeatEntry());
			rules.add(new BanEntry("F5","E3"));
			rules.add(new BanEntry("F9","F11"));
			rules.add(new BanEntry("W1","E9"));
			rules.add(new BanEntry("E5","E6"));
			rules.add(new BanEntry("D2","W1"));
			rules.add(new BanEntry("D2","W4"));
			rules.add(new SecureEntry(1,1, "F6", "F9", "A8"));
			rules.add(new SecureEntry(1,1, "F3", "W5", "E5"));
			rules.add(new SecureEntry(1,2, "F11", "A6", "A8", "E6"));
			rules.add(new SecureEntry(2,5, "W6", "W10", "A4", "E1", "E2", "E4", "E11"));
		}

		public void generate(Power[] pa, Card[][] a, Power[] pb, Card[][] b)
		{
			while(true)
			{
				boolean fail = true;
				while(fail)
				{
					try{
						generateRaw(pa,a,pb,b);
						fail = false;
					}catch(Exception e)
					{
						fail = true;
					}
				}
				boolean valid = true;
				for(RuleEntry e : rules)
				{
					if(e.check(a) || e.check(b))
					{
						valid = false;
						break;
					}
				}
				if(valid)break;
			}
		}

		public void generateRaw(Power[] pa, Card[][] a, Power[] pb, Card[][] b) throws Exception
		{
			for(int i = 0 ; i < 4; i ++)
			{
				Arrays.fill(used, false);
				for(int j = 0; j < 4;j ++)
				{
					int x = rndInRangeExcept(min[i][j], max[i][j] +1);
					used[x-1] = true;
					int y = rndInRangeExcept(min[i][j], max[i][j] +1);
					used[y-1] = true;

					a[i][j] = pa[i].getCard(x -1);
					b[i][j] = pb[i].getCard(y -1);
				}
			}
			Arrays.fill(used, false);
			for(int i = 0 ; i < 4; i ++)
			{
				int x = rndInRangeExcept(min[4][i], max[4][i] +1);
				used[x-1] = true;
				int y = rndInRangeExcept(min[4][i], max[4][i] +1);				
				used[y-1] = true;

				a[4][i] = pa[4].getCard(x-1);
				b[4][i] = pb[4].getCard(y-1);
			}
		}

		private int rndInRangeExcept(int min, int max) throws Exception
		{
			if(max <= min) throw new Exception();

			int range = 0 ;
			for(int i = min; i < max; i ++)
				if(!used[i-1])range++;

			int r = rand.nextInt(range);
			for(int i = min; i < max; i ++)
				if(!used[i-1])
				{
					if(r == 0)return i;
					else r--;
				}
			throw new Exception();
		}
	}

	public static void assignCards(EntityPlayer a, EntityPlayer b)
	{
		HandGenerator hg = new HandGenerator();
		Card[][] ca = new Card[5][4];
		Card[][] cb = new Card[5][4];

		hg.generate(a.getPowers(), ca, b.getPowers(), cb);

		for(Card[] cards : ca)
			for(Card card : cards)
				a.giveCard(new EntityCard(card, a));

		for(Card[] cards : cb)
			for(Card card : cards)
				b.giveCard(new EntityCard(card, b));
	}
}
