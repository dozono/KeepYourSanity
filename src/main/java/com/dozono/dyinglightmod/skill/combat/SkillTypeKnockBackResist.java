package com.dozono.dyinglightmod.skill.combat;

import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeKnockBackResist extends SkillType {
    public static final SkillTypeKnockBackResist INSTANCE = new SkillTypeKnockBackResist();
    public SkillTypeKnockBackResist() {
        super(Builder.create().addParent(SkillTypeTBD.INSTANCE));
    }

    @SubscribeEvent
    public void onKnockBack(LivingKnockBackEvent event){
        LivingEntity target = event.getEntityLiving();
        if(target instanceof PlayerEntity){
            if(target.level.isClientSide) return;
            target.getCapability(CapabilitySkillContainer).ifPresent(c->c.getSkill(this).ifPresent(skill->{
                event.setStrength(event.getStrength()/skill.getLevel()+1);
            }));
        }
    }
}
