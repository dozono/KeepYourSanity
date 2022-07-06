package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SkillTypeTireless extends SkillType {
    public static final SkillTypeTireless INSTANCE = new SkillTypeTireless();

    private SkillTypeTireless() {
        super(Builder.create().addParent(SkillTypeMandom.INSTANCE));
    }

    @SubscribeEvent
    public void onHungerDecrease(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (player.level.isClientSide) return;
        player.getCapability(DyingLight.CapabilitySkillContainer).ifPresent(c -> c.getSkill(this).ifPresent(skill -> {
            float saturationLevel = player.getFoodData().getSaturationLevel();
            player.getFoodData().setSaturation((float) (saturationLevel * (Math.sqrt(skill.getLevel()) + 0.5)));
        }));
    }
}
