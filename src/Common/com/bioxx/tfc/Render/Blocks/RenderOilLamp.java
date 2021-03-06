package com.bioxx.tfc.Render.Blocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderOilLamp implements ISimpleBlockRenderingHandler
{
	static float min = 0.1F;
	static float max = 0.9F;

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		renderer.setRenderBounds(0.41, 0.0, 0.41, 0.59, 0.05F, 0.59);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.setRenderBounds(0.4, 0.05, 0.4, 0.6, 0.3F, 0.6);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.setRenderBounds(0.45, 0.3, 0.45, 0.55, 0.35F, 0.55);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.setRenderBounds(0.48, 0.35, 0.48, 0.52, 0.42F, 0.52);
		renderer.renderStandardBlock(block, x, y, z);
		return true;
	}

	@Override
	public void renderInventoryBlock(Block block, int meta, int modelID, RenderBlocks renderer)
	{
		renderer.setRenderBounds(0.45, 0.0, 0.45, 0.55, 0.05F, 0.55);
		renderInvBlock(block, meta, renderer);
		renderer.setRenderBounds(0.4, 0.05, 0.4, 0.6, 0.3F, 0.6);
		renderInvBlock(block, meta, renderer);
		renderer.setRenderBounds(0.45, 0.3, 0.45, 0.55, 0.35F, 0.55);
		renderInvBlock(block, meta, renderer);
		renderer.setRenderBounds(0.48, 0.35, 0.48, 0.52, 0.42F, 0.52);
		renderInvBlock(block, meta, renderer);
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return 0;
	}

	public static void renderInvBlock(Block block, int m, RenderBlocks renderer)
	{
		Tessellator var14 = Tessellator.instance;
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		var14.startDrawingQuads();
		var14.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, m));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, m));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, m));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, m));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, m));
		var14.draw();
		var14.startDrawingQuads();
		var14.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, m));
		var14.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
}
