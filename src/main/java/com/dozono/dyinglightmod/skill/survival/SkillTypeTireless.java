package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.msg.SprintMessage;
import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillContainer;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.FoodStats;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SkillTypeTireless extends SkillType {
    public static final SkillTypeTireless INSTANCE = new SkillTypeTireless();

    private SkillTypeTireless() {
        super(Builder.create().addParent(SkillTypeMandom.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public Skill createSkill(SkillContainer skillContainer, PlayerEntity player) {
        return new SkillTypeTireless.SkillTireless(this, skillContainer, player);
    }


    public static class SkillTireless extends Skill {
        public SkillTireless(SkillType type, SkillContainer skillContainer, PlayerEntity player) {
            super(type, skillContainer, player);
        }

        public boolean toggleSprintKey = false;
    }

    @SubscribeEvent
    public void onLowHunger(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (player.level.isClientSide) return;
        if (event.phase == TickEvent.Phase.START) return;
        FoodStats foodData = player.getFoodData();
        player.getCapability(DyingLight.CapabilitySkillContainer).ifPresent((c) -> c.getSkill(this).map(skill -> (SkillTireless) skill).ifPresent(skill -> {
            if (skill.getLevel() == 0) return;
            if (foodData.getFoodLevel() <= 6 && skill.toggleSprintKey) {
                player.setSprinting(true);
            }
            if (foodData.getFoodLevel() <= 6 && !skill.toggleSprintKey) {
                player.setSprinting(false);
            }
        }));


    }

    @SubscribeEvent
    public void toggleSprint(InputEvent.KeyInputEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;
        int sprintKey = Minecraft.getInstance().options.keySprint.getKey().getValue();
        int wKey = Minecraft.getInstance().options.keyUp.getKey().getValue();
        int sKey = Minecraft.getInstance().options.keyDown.getKey().getValue();
        player.getCapability(DyingLight.CapabilitySkillContainer).ifPresent((c) -> c.getSkill(this).map(skill -> (SkillTireless) skill).ifPresent(skill -> {
            if (skill.getLevel() == 0) return;
            if (event.getKey() == sprintKey && event.getAction() == 1) {
                skill.toggleSprintKey = true;
                SprintMessage sprintMessage = new SprintMessage();
                sprintMessage.value = true;
                DyingLight.CHANNEL.sendToServer(sprintMessage);
            } else if (event.getKey() == wKey && event.getAction() == 2) {
                skill.toggleSprintKey = false;
                SprintMessage sprintMessage = new SprintMessage();
                sprintMessage.value = false;
                DyingLight.CHANNEL.sendToServer(sprintMessage);
            } else if (event.getKey() == sKey && event.getAction() == 1){
                skill.toggleSprintKey = false;
                SprintMessage sprintMessage = new SprintMessage();
                sprintMessage.value = false;
                DyingLight.CHANNEL.sendToServer(sprintMessage);
            }
        }));


    }


}
