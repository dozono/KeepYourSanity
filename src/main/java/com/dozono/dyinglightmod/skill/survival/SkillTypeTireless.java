package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.msg.WallClimbMessage;
import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillContainer;
import com.dozono.dyinglightmod.skill.SkillType;
import com.dozono.dyinglightmod.skill.agility.SkillTypeWallClimb;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftGame;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.FoodStats;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.gui.GuiUtils;

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

        public boolean toggleSprintKey = true;
    }

    @SubscribeEvent
    public void onHungerDecrease(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (player.level.isClientSide) return;
        if (event.phase == TickEvent.Phase.START) return;
        FoodStats foodData = player.getFoodData();
        player.getCapability(DyingLight.CapabilitySkillContainer).ifPresent((c) -> c.getSkill(this).ifPresent(skill -> {
            if(skill.getLevel()==0) return;
            if(foodData.getFoodLevel()<=6 && InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(),341)){
                player.setSprinting(true);
                if(InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(),340)){
                    player.setSprinting(false);
                }
            }
        }));
    }


}
