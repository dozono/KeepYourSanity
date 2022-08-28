package com.dozono.dyinglightmod.skill.agility;

import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeBoneCrusher extends SkillType {
    public SkillTypeBoneCrusher() {
        super(Builder.create().addParent(SkillTypeDoubleJump.INSTANCE));
    }

    public static final SkillTypeBoneCrusher INSTANCE = new SkillTypeBoneCrusher();

    @SubscribeEvent
    public void onHurtingMob(LivingHurtEvent event){
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();
        LivingEntity entityLiving = event.getEntityLiving();
        if (attacker instanceof PlayerEntity){
            if(attacker.level.isClientSide) return;
            attacker.getCapability(CapabilitySkillContainer).ifPresent(c->c.getSkill(this).ifPresent(skill -> {
                entityLiving.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN,(int)(skill.getLevel()*2.5f),5));
            }));
        }
        }

}
