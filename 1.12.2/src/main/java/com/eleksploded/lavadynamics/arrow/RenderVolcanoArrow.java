package com.eleksploded.lavadynamics.arrow;

import com.eleksploded.lavadynamics.Reference;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderVolcanoArrow extends RenderArrow<EntityVolcanoArrow>
{
    public static final ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/volcanoarrow.png");

    public RenderVolcanoArrow(RenderManager manager)
    {
        super(manager);
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityVolcanoArrow entity) {
		return texture;
	}
	
	public static class VolcanoArrowRenderFactory implements IRenderFactory<EntityVolcanoArrow> {

	    public static final VolcanoArrowRenderFactory INSTANCE = new VolcanoArrowRenderFactory();

	    @Override
	    public Render<? super EntityVolcanoArrow> createRenderFor(RenderManager manager) {
	        return new RenderVolcanoArrow(manager);
	    }
	}
}