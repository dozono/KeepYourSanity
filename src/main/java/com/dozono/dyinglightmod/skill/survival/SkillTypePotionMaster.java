package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypePotionMaster extends SkillType {
    public static final SkillTypePotionMaster INSTANCE = new SkillTypePotionMaster();

    public SkillTypePotionMaster() {
        super(Builder.create().addParent(SkillTypeGastrosoph.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGettingPotionEffect(PotionEvent.PotionAddedEvent event) {
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving.level.isClientSide) return;
        if (entityLiving instanceof PlayerEntity) {
            entityLiving.getCapability(CapabilitySkillContainer).ifPresent(c -> {
                Optional<Skill> skill = c.getSkill(this);
                if (skill.isPresent()) {
                    EffectInstance effect = event.getPotionEffect();
                    effect.update(new EffectInstance(effect.getEffect(),
                            effect.getDuration() * 2,
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.isVisible(),
                            effect.showIcon(),
                            null));
                }
            });
        }
    }


}
