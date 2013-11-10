package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import java.util.Locale;
import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.*;

//Todo: Fix this up
//Todo: now overlaps some functions of killall, which should be deprecated and removed once all functions are covered
public class Commandremove extends EssentialsCommand
{
	public Commandremove()
	{
		super("remove");
	}

	@Override
	protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		ToRemove toRemove;
		World world = user.getWorld();
		int radius = 0;

		if (args.length >= 2)
		{
			try
			{
				radius = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException e)
			{
				throw new Exception(_("numberRequired"), e);
			}
		}

		if (args.length >= 3)
		{
			world = ess.getWorld(args[2]);
		}

		try
		{
			toRemove = ToRemove.valueOf(args[0].toUpperCase(Locale.ENGLISH));
		}
		catch (IllegalArgumentException e)
		{
			try
			{
				toRemove = ToRemove.valueOf(args[0].concat("S").toUpperCase(Locale.ENGLISH));
			}
			catch (IllegalArgumentException ee)
			{
				throw new NotEnoughArgumentsException(ee); //TODO: translate and list types
			}
		}
		removeEntities(user.getSource(), world, toRemove, radius);
	}

	@Override
	protected void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}
		World world = ess.getWorld(args[1]);

		ToRemove toRemove;
		try
		{
			toRemove = ToRemove.valueOf(args[0].toUpperCase(Locale.ENGLISH));
		}
		catch (IllegalArgumentException e)
		{
			try
			{
				toRemove = ToRemove.valueOf(args[0].concat("S").toUpperCase(Locale.ENGLISH));
			}
			catch (IllegalArgumentException ee)
			{
				throw new NotEnoughArgumentsException(ee); //TODO: translate and list types
			}
		}
		removeEntities(sender, world, toRemove, 0);
	}

	private void removeEntities(final CommandSource sender, final World world, final ToRemove toRemove, int radius) throws Exception
	{
		int removed = 0;
		if (radius > 0)
		{
			radius *= radius;
		}
		for (Chunk chunk : world.getLoadedChunks())
		{
			for (Entity e : chunk.getEntities())
			{
				if (radius > 0)
				{
					if (sender.getPlayer().getLocation().distanceSquared(e.getLocation()) > radius)
					{
						continue;
					}
				}
				if (e instanceof Tameable)
				{
					if (((Tameable)e).isTamed())
					{
						continue;
					}
				}
				switch (toRemove)
				{
				case DROPS:
					if (e instanceof Item)
					{
						e.remove();
						removed++;
					}
					;
					break;
				case ARROWS:
					if (e instanceof Projectile)
					{
						e.remove();
						removed++;
					}
					break;
				case BOATS:
					if (e instanceof Boat)
					{
						e.remove();
						removed++;
					}
					break;
				case MINECARTS:
					if (e instanceof Minecart)
					{
						e.remove();
						removed++;
					}
					break;
				case XP:
					if (e instanceof ExperienceOrb)
					{
						e.remove();
						removed++;
					}
					break;
				case PAINTINGS:
					if (e instanceof Painting)
					{
						e.remove();
						removed++;
					}
					break;
				case ITEMFRAMES:
					if (e instanceof ItemFrame)
					{
						e.remove();
						removed++;
					}
					break;
				case ENDERCRYSTALS:
					if (e instanceof EnderCrystal)
					{
						e.remove();
						removed++;
					}
					break;
				case AMBIENT:
					if (e instanceof Flying)
					{
						e.remove();
						removed++;
					}
					break;
				case HOSTILE:
				case MONSTERS:
					if (e instanceof Monster || e instanceof ComplexLivingEntity || e instanceof Flying || e instanceof Slime)
					{
						e.remove();
						removed++;
					}
					break;
				case PASSIVE:
				case ANIMALS:
					if (e instanceof Animals || e instanceof NPC || e instanceof Snowman || e instanceof WaterMob)
					{
						e.remove();
						removed++;
					}
					break;
				case MOBS:
					if (e instanceof Animals || e instanceof NPC || e instanceof Snowman || e instanceof WaterMob
						|| e instanceof Monster || e instanceof ComplexLivingEntity || e instanceof Flying || e instanceof Slime)
					{
						e.remove();
						removed++;
					}
					break;
				case ENTITIES:
					if (e instanceof Entity)
					{
						if (e instanceof HumanEntity)
						{
							continue;
						}
						e.remove();
						removed++;
					}
					break;
				}
			}
		}
		sender.sendMessage(_("removed", removed));
	}


	private enum ToRemove
	{
		DROPS,
		ARROWS,
		BOATS,
		MINECARTS,
		XP,
		PAINTINGS,
		ITEMFRAMES,
		ENDERCRYSTALS,
		HOSTILE,
		MONSTERS,
		PASSIVE,
		ANIMALS,
		AMBIENT,
		MOBS,
		ENTITIES
	}
}
