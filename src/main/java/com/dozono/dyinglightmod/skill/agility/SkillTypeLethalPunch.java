package com.dozono.dyinglightmod.skill.agility;

import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.ElderGuardianEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeLethalPunch extends SkillType {
    public SkillTypeLethalPunch() {
        super(Builder.create().addParent(SkillTypeDoubleJump.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);

    }

    public static final SkillTypeLethalPunch INSTANCE = new SkillTypeLethalPunch();

    @SubscribeEvent
    public void onAttackMob(AttackEntityEvent event) {
        Entity target = event.getTarget();
        PlayerEntity player = event.getPlayer();
        if (player.level.isClientSide) return;
        player.getCapability(CapabilitySkillContainer).ifPresent(c -> c.getSkill(this).ifPresent(skill -> {
            if (skill.getLevel() == 0) return;
            if(!(target instanceof MobEntity)) return;
            if (target instanceof EnderDragonEntity && player.level.random.nextInt(200) <=skill.getLevel() ) {
                target.kill();
            } else if (target instanceof EnderDragonEntity && player.level.random.nextInt(200) <=skill.getLevel()) {
                target.kill();
            }else if (target instanceof SkeletonHorseEntity && player.level.random.nextInt(200) <=skill.getLevel()) {
                target.kill();
            }else if (target instanceof ElderGuardianEntity && player.level.random.nextInt(200) <=skill.getLevel()) {
                target.kill();
            } else if(player.level.random.nextInt(40) <=skill.getLevel()) {
                target.kill();
            }
        }));

    }
}
