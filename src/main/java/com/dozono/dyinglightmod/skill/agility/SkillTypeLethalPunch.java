package com.dozono.dyinglightmod.skill.agility;

import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeLethalPunch extends SkillType {
    public SkillTypeLethalPunch() {
        super(Builder.create().addParent(SkillTypeDoubleJump.INSTANCE));
    }
    public static final SkillTypeLethalPunch INSTANCE = new SkillTypeLethalPunch();

    @SubscribeEvent
    public void onAttackMob(AttackEntityEvent event){
        Entity target = event.getTarget();
        PlayerEntity player = event.getPlayer();
        if(player.level.isClientSide) return;
        player.getCapability(CapabilitySkillContainer).ifPresent(c->c.getSkill(this).ifPresent(skill -> {
            if(target instanceof MobEntity && player.level.random.nextInt(10)>=8){
                target.kill();
            }
        }));

    }
}
