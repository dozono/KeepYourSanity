package com.dozono.dyinglightmod.skill.combat;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeProjectileDeflection extends SkillType {
    public static final SkillTypeProjectileDeflection INSTANCE = new SkillTypeProjectileDeflection();

    public SkillTypeProjectileDeflection() {
        super(Builder.create().addParent(SkillTypePlunder.INSTANCE));
    }

    @SubscribeEvent
    public void onShot(LivingHurtEvent event) {
        LivingEntity victim = event.getEntityLiving();
        if (victim instanceof PlayerEntity) {
            if (victim.level.isClientSide) return;
            victim.getCapability(CapabilitySkillContainer).ifPresent(c -> c.getSkill(this).ifPresent(skill -> {
                if (skill.getLevel() == 0) return;

                Random probability = new Random();
                if (probability.nextFloat()< (float) skill.getLevel() / 10 + 0.1 && event.getSource().isProjectile()) {
                    event.setCanceled(true);
                }
            }));
        }


    }

    @Override
    public TextComponent getDescription(Skill skill) {
        return getCommonDescriptionContent(skill,"20%","30%","40%");
    }
}
