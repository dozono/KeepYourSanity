package com.dozono.dyinglightmod.skill.agility;

import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeRunner extends SkillType {
    public SkillTypeRunner() {
        super(Builder.create());
    }

    @SubscribeEvent
    public void onSprint(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        if(player.level.isClientSide) return;
        player.getCapability(CapabilitySkillContainer).ifPresent(c->c.getSkill(this).ifPresent(skill->{
            if(player.isSprinting()){
                ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);

            }
        }));
    }

}
