package com.dozono.dyinglightmod.skill.combat;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeCamouflage extends SkillType {
    public static final SkillTypeCamouflage INSTANCE = new SkillTypeCamouflage();

    private SkillTypeCamouflage() {
        super(Builder.create().addParent(SkillTypeTBD.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onSneaking(LivingSetAttackTargetEvent event) {
        LivingEntity attacker = event.getEntityLiving();
        LivingEntity victim = event.getTarget();
        if (attacker.level.isClientSide) return;

        if (victim instanceof PlayerEntity) {
            int lastHurtByMobTimestamp = victim.getLastHurtByMobTimestamp();
            int tickCount = victim.tickCount;
            int diff = tickCount - lastHurtByMobTimestamp;
            victim.getCapability(CapabilitySkillContainer).ifPresent(c -> c.getSkill(this).ifPresent(skill -> {
                        if (attacker instanceof MobEntity && victim.isShiftKeyDown() && diff >= 100) {
                            ((MobEntity) attacker).setTarget(null);
                        }
                    }
            ));
        }
    }
}
