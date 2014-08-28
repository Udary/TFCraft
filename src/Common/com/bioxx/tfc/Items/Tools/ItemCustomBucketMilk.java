package com.bioxx.tfc.Items.Tools;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.bioxx.tfc.TFCItems;
import com.bioxx.tfc.Core.TFCTabs;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Core.TFC_Time;
import com.bioxx.tfc.Core.Player.FoodStatsTFC;
import com.bioxx.tfc.Items.ItemTerra;
import com.bioxx.tfc.api.TFCOptions;
import com.bioxx.tfc.api.Enums.EnumFoodGroup;
import com.bioxx.tfc.api.Enums.EnumItemReach;
import com.bioxx.tfc.api.Interfaces.IFood;

public class ItemCustomBucketMilk extends ItemTerra implements IFood
{
	public ItemCustomBucketMilk()
	{
		super();
		this.setMaxStackSize(1);
		setCreativeTab(TFCTabs.TFCFoods);
		this.setFolder("tools/");
	}

	@Override
	public boolean canStack()
	{
		return false;
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list)
	{
		list.add(createTag(new ItemStack(this, 1), 20));
	}

	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag)
	{
		if(is.hasTagCompound() && is.getTagCompound().hasKey("foodWeight"))
		{
			float ounces = is.getTagCompound().getFloat("foodWeight");
			if(ounces > 0)
				arraylist.add(ounces+" oz");
			float decay = is.getTagCompound().getFloat("foodDecay");
			if(decay > 0)
			{
				float perc = decay/ounces;
				String s = EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("gui.milk.fresh");
				if(perc > 50)
					s = EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("gui.milk.old");
				if(perc > 70)
					s = EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("gui.milk.sour");

				arraylist.add(s);
			}
			if(TFCOptions.enableDebugMode)
				arraylist.add(EnumChatFormatting.DARK_GRAY + "Decay: " + decay);
		}
	}

	public static ItemStack createTag(ItemStack is, float weight)
	{
		NBTTagCompound nbt = is.getTagCompound();
		if(nbt == null)
			nbt = new NBTTagCompound();
		nbt.setFloat("foodWeight", weight);
		nbt.setFloat("foodDecay", 0);
		nbt.setInteger("decayTimer", (int)TFC_Time.getTotalHours() + 1);

		is.setTagCompound(nbt);
		return is;
	}

	@Override
	public ItemStack onEaten(ItemStack is, World world, EntityPlayer player)
	{
		FoodStatsTFC foodstats = TFC_Core.getPlayerFoodStats(player);
		if(!world.isRemote && foodstats.needFood())
		{
			world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);

			if(is.hasTagCompound())
			{
				NBTTagCompound nbt = is.getTagCompound();
				float weight = ((IFood)(is.getItem())).getFoodWeight(is);
				float decay = Math.max(((IFood)(is.getItem())).getFoodDecay(is), 0);

				float tasteFactor = foodstats.getTasteFactor(is);
				foodstats.addNutrition(((IFood)(is.getItem())).getFoodGroup(), 20*tasteFactor);
			}

			foodstats.restoreWater(player, 16000);

			TFC_Core.setPlayerFoodStats(player, foodstats);

			is = new ItemStack(TFCItems.WoodenBucketEmpty);
			is.stackTagCompound = null;
		}
		return is;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 32;
	}

	/**
	 * returns the action that specifies what animation to play when the items is being used
	 */
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{
		return EnumAction.drink;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer entity)
	{
		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, entity, true);
		FoodStatsTFC fs = TFC_Core.getPlayerFoodStats(entity);

		if(mop == null)
		{
			if (fs.needDrink() && fs.needFood())
				entity.setItemInUse(is, this.getMaxItemUseDuration(is));
		}
		return is;
	}

	@Override
	public EnumFoodGroup getFoodGroup()
	{
		return EnumFoodGroup.Dairy;
	}

	@Override
	public int getFoodID()
	{
		return -1;
	}

	@Override
	public ItemStack onDecayed(ItemStack is, World world, int i, int j, int k) 
	{
		return new ItemStack(TFCItems.WoodenBucketEmpty);
	}

	@Override
	public float getDecayRate()
	{
		return 6f;
	}

	@Override
	public EnumItemReach getReach(ItemStack is)
	{
		return EnumItemReach.SHORT;
	}

	@Override
	public float getFoodWeight(ItemStack is)
	{
		if(is.hasTagCompound() && is.getTagCompound().hasKey("foodWeight"))
		{
			NBTTagCompound nbt = is.getTagCompound();
			return nbt.getFloat("foodWeight");
		}
		return 0f;
	}

	@Override
	public float getFoodDecay(ItemStack is)
	{
		if(is.hasTagCompound() && is.getTagCompound().hasKey("foodDecay"))
		{
			NBTTagCompound nbt = is.getTagCompound();
			return nbt.getFloat("foodDecay");
		}
		return 0f;
	}

	@Override
	public boolean isEdible()
	{
		return true;
	}

	@Override
	public boolean isUsable()
	{
		return false;
	}

	@Override
	public int getTasteSweet(ItemStack is) {
		return 0;
	}

	@Override
	public int getTasteSour(ItemStack is) {
		return 0;
	}

	@Override
	public int getTasteSalty(ItemStack is) {
		return 0;
	}

	@Override
	public int getTasteBitter(ItemStack is) {
		return 0;
	}

	@Override
	public int getTasteSavory(ItemStack is) {
		return 10;
	}

	@Override
	public float getFoodMaxWeight(ItemStack is) {
		// TODO Auto-generated method stub
		return 80;
	}

	@Override
	public boolean renderDecay() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean renderWeight() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canSmoke() {
		return false;
	}

	@Override
	public float getSmokeAbsorbMultiplier() {
		// TODO Auto-generated method stub
		return 0;
	}
}
