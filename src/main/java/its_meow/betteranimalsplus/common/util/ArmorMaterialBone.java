package its_meow.betteranimalsplus.common.util;

import its_meow.betteranimalsplus.init.ItemRegistry;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;

public class ArmorMaterialBone implements IArmorMaterial {

	@Override
	public int getDamageReductionAmount(EntityEquipmentSlot arg0) {
		return 2;
	}

	@Override
	public int getDurability(EntityEquipmentSlot arg0) {
		return 50;
	}

	@Override
	public int getEnchantability() {
		return 12;
	}

	@Override
	public String getName() {
		return "bone";
	}

	@Override
	public Ingredient getRepairMaterial() {
		return Ingredient.fromItems(Items.BONE, ItemRegistry.antler);
	}

	@Override
	public SoundEvent getSoundEvent() {
		return SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
	}

	@Override
	public float getToughness() {
		return 0;
	}

}