package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeRegen extends SkillType {
    public static final SkillTypeRegen INSTANCE = new SkillTypeRegen();

    private SkillTypeRegen() {
        super(Builder.create().addParent(SkillTypePotionMaster.Instance).addParent(SkillTypeToolMaster.Instance));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void mount(PlayerEntity playerEntity, Skill skill) {

    }

    @Override
    public void onLevelUp(PlayerEntity player, Skill skill) {

    }

    //TODO:xie zai PlayerTickEvent limian
    @SubscribeEvent
    public void onHurt(LivingSetAttackTargetEvent event) {
        LivingEntity attacker = event.getEntityLiving();
        LivingEntity victim = event.getTarget();
        if (attacker.level.isClientSide) return;

        if (victim instanceof PlayerEntity) {
            int lastHurtByMobTimestamp = victim.getLastHurtByMobTimestamp();
            int tickCount = victim.tickCount;
            int diff = tickCount - lastHurtByMobTimestamp;
            victim.getCapability(CapabilitySkillContainer).ifPresent(c -> c.getSkill(this).ifPresent(skill -> {
                for (int i = 0; i < skill.getLevel() + 1; i++) {
                    if (diff % 20 == 0) {
                        victim.heal(1.0f);
                    }
                }
            }));
        }
    }

}
