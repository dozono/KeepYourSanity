package com.dozono.dyinglightmod.skill.combat;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import com.dozono.dyinglightmod.skill.survival.SkillTypeLuck;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeDisguise extends SkillType {
    public static final SkillTypeDisguise Instance = new SkillTypeDisguise();

    private SkillTypeDisguise() {
        super(Builder.create().dependOn(SkillTypeLuck.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void mount(PlayerEntity playerEntity, Skill skill) {

    }

    @Override
    public void onLevelUp(PlayerEntity player, Skill skill) {

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
