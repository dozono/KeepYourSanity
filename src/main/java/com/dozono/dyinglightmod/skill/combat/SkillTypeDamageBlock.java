package com.dozono.dyinglightmod.skill.combat;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeDamageBlock extends SkillType {
    public static final SkillTypeDamageBlock INSTANCE = new SkillTypeDamageBlock();

    public SkillTypeDamageBlock() {
        super(Builder.create().addParent(SkillTypeWeaponMaster.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTakingDamage(LivingHurtEvent event) {
        Entity victim = event.getEntity();
        if (victim instanceof PlayerEntity) {
            if (victim.level.isClientSide) return;
            victim.getCapability(CapabilitySkillContainer).ifPresent(c -> c.getSkill(this).ifPresent(skill -> {
                        if (event.getAmount() >= 1) {
                            event.setAmount(0.5f);
                        } else if (event.getAmount() >= 2 && event.getAmount() <= 5) {
                            event.setAmount(event.getAmount() - skill.getLevel());
                        }
                    }
            ));
        }
    }
}
