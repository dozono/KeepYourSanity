package com.dozono.dyinglightmod.skill.agility;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SkillTypeAquaMan extends SkillType {
    public static final SkillTypeAquaMan INSTANCE = new SkillTypeAquaMan();
    public SkillTypeAquaMan() {
        super(Builder.create().addParent(SkillTypeSwimmer.INSTANCE));
    }

    @SubscribeEvent
    public void playerInWater(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        if(player.level.isClientSide) return;
        player.getCapability(DyingLight.CapabilitySkillContainer).ifPresent(c->c.getSkill(this).ifPresent(skill -> {
            if(player.isInWater()){
                player.addEffect(new EffectInstance(Effects.WATER_BREATHING));
            }

        }));
    }

    @SubscribeEvent
    public void playerHurtEvent(LivingHurtEvent event){
        LivingEntity victim = event.getEntityLiving();
        if(victim instanceof PlayerEntity && victim.level.isClientSide) return;
        victim.getCapability(DyingLight.CapabilitySkillContainer).ifPresent(c->c.getSkill(this).ifPresent(skill -> {
            if(event.getSource().getEntity() instanceof MobEntity){
                DolphinEntity dolphin = new DolphinEntity(EntityType.DOLPHIN,victim.level);
                dolphin.setPos(victim.position().x,victim.position().y,victim.position().z);
                victim.level.addFreshEntity(dolphin);
                dolphin.setTarget((LivingEntity) event.getSource().getEntity());
            }
        }));
    }
}
