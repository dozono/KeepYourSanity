package com.dozono.dyinglightmod.skill.agility;

import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeSwimmer extends SkillType {
    public static final SkillTypeSwimmer INSTANCE = new SkillTypeSwimmer();
    public SkillTypeSwimmer() {
        super(Builder.create().addParent(SkillTypeRunner.INSTANCE));
        this.setRegistryName("swimmer");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onSwimming(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        if (player.level.isClientSide) return;
        if(player.swinging){
            player.getCapability(CapabilitySkillContainer).ifPresent(c->c.getSkill(this).ifPresent(skill -> {

            }));
        }
    }
}
