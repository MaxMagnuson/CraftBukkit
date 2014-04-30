package org.bukkit.craftbukkit.enchantments;

import java.util.HashMap;

import net.minecraft.server.EnchantmentSlotType;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class CraftEnchantment extends Enchantment {
    private final net.minecraft.server.Enchantment target;
    private static final HashMap<String, EnchantmentTarget> slotTarget = 
            new HashMap<String, EnchantmentTarget>();
    static
    {
        slotTarget.put("ALL", EnchantmentTarget.ALL);
        slotTarget.put("ARMOR", EnchantmentTarget.ARMOR);
        slotTarget.put("ARMOR_FEET", EnchantmentTarget.ARMOR_FEET);
        slotTarget.put("ARMOR_HEAD", EnchantmentTarget.ARMOR_HEAD);
        slotTarget.put("ARMOR_LEGS", EnchantmentTarget.ARMOR_LEGS);
        slotTarget.put("ARMOR_TORSO", EnchantmentTarget.ARMOR_TORSO);
        slotTarget.put("DIGGER", EnchantmentTarget.TOOL);
        slotTarget.put("WEAPON", EnchantmentTarget.WEAPON);
        slotTarget.put("BOW", EnchantmentTarget.BOW);
        slotTarget.put("FISHING_ROD", EnchantmentTarget.FISHING_ROD);
    }
    private static final HashMap<Integer, String> nameMap = new HashMap<Integer, String>();
    static
    {
        nameMap.put(0, "PROTECTION_ENVIRONMENTAL");
        nameMap.put(1, "PROTECTION_FIRE");
        nameMap.put(2, "PROTECTION_FALL");
        nameMap.put(3, "PROTECTION_EXPLOSIONS");
        nameMap.put(4, "PROTECTION_PROJECTILE");
        nameMap.put(5, "OXYGEN");
        nameMap.put(6, "WATER_WORKER");
        nameMap.put(7, "THORNS");
        nameMap.put(16, "DAMAGE_ALL");
        nameMap.put(17, "DAMAGE_UNDEAD");
        nameMap.put(18, "DAMAGE_ARTHROPODS");
        nameMap.put(19, "KNOCKBACK");
        nameMap.put(20, "FIRE_ASPECT");
        nameMap.put(21, "LOOT_BONUS_MOBS");
        nameMap.put(32, "DIG_SPEED");
        nameMap.put(33, "SILK_TOUCH");
        nameMap.put(34, "DURABILITY");
        nameMap.put(35, "LOOT_BONUS_BLOCKS");
        nameMap.put(48, "ARROW_DAMAGE");
        nameMap.put(49, "ARROW_KNOCKBACK");
        nameMap.put(50, "ARROW_FIRE");
        nameMap.put(51, "ARROW_INFINITE");
        nameMap.put(61, "LUCK");
        nameMap.put(62, "LURE");
    }

    public CraftEnchantment(net.minecraft.server.Enchantment target) {
        super(target.id);
        this.target = target;
    }

    @Override
    public int getMaxLevel() {
        return target.getMaxLevel();
    }

    @Override
    public int getStartLevel() {
        return target.getStartLevel();
    }

    @Override
    public EnchantmentTarget getItemTarget() {
    	return slotTarget.get(target.slot.toString());
  
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return target.canEnchant(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public String getName() {
    	if(nameMap.get(target.id)==null){
    		return "UNKNOWN_ENCHANT_" + target.id;
    	} else {
    		return nameMap.get(target.id);
    	}
    }

    public static net.minecraft.server.Enchantment getRaw(Enchantment enchantment) {
        if (enchantment instanceof EnchantmentWrapper) {
            enchantment = ((EnchantmentWrapper) enchantment).getEnchantment();
        }

        if (enchantment instanceof CraftEnchantment) {
            return ((CraftEnchantment) enchantment).target;
        }

        return null;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        if (other instanceof EnchantmentWrapper) {
            other = ((EnchantmentWrapper) other).getEnchantment();
        }
        if (!(other instanceof CraftEnchantment)) {
            return false;
        }
        CraftEnchantment ench = (CraftEnchantment) other;
        return !target.a(ench.target);
    }
}
