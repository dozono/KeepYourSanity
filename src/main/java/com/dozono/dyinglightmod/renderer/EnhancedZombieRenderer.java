package com.dozono.dyinglightmod.renderer;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.entities.EnhancedZombieEntity;
import com.dozono.dyinglightmod.model.EnhancedZombieModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class EnhancedZombieRenderer extends MobRenderer<EnhancedZombieEntity, EnhancedZombieModel<EnhancedZombieEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(DyingLight.MODID,"textures/entity/enhanced_zombie.png");
    public EnhancedZombieRenderer(EntityRendererManager renderManagerIn, EnhancedZombieModel<EnhancedZombieEntity> entityModelIn, float shadowSizeIn) {
        super(renderManagerIn, new EnhancedZombieModel<>(), 0.8f);
    }

    @Override
    public ResourceLocation getEntityTexture(EnhancedZombieEntity entity) {
        return TEXTURE;
    }
}
