package com.dozono.dyinglightmod.skill.agility;

import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeStrongLegs extends SkillType {
    public SkillTypeStrongLegs() {
        super(Builder.create().addParent(SkillTypeDoubleJump.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);

    }

    public static final SkillTypeStrongLegs INSTANCE = new SkillTypeStrongLegs();

    @SubscribeEvent
    public void onTakingFallDamage(LivingFallEvent event){
        LivingEntity entity = event.getEntityLiving();
        if(entity instanceof PlayerEntity){
            if(entity.level.isClientSide) return;
            entity.getCapability(CapabilitySkillContainer).ifPresent(c->c.getSkill(this).ifPresent(skill -> {
                event.setDamageMultiplier(event.getDamageMultiplier()/(skill.getLevel()+1));

            }));
        }
        }
    }



