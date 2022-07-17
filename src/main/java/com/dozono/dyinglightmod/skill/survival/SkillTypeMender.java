package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeMender extends SkillType {

    public static final SkillTypeMender INSTANCE = new SkillTypeMender();

    public SkillTypeMender() {
        super(Builder.create().addParent(SkillTypeToolMaster.Instance));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTakeWeapon(AnvilRepairEvent event) {
        if (event.getPlayer().level.isClientSide) return;
        Item item = event.getItemInput().getItem().getItem();
        if (item.isRepairable(item.getDefaultInstance())) {
            event.setBreakChance(0);
        }
    }

    @SubscribeEvent
    public void onPut(AnvilUpdateEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player == null || player.level.isClientSide) return;
        player.getCapability(CapabilitySkillContainer).ifPresent((c) -> {
            Optional<Skill> skill = c.getSkill(this);
            if (skill.isPresent() && player.isAlive()) {
                event.setCost(event.getCost() - skill.get().getLevel());
            }
        });
    }
//    @SubscribeEvent
//    public void onRepair(PlayerXpEvent.LevelChange event) {
//        PlayerEntity player = event.getPlayer();
//        if (event.getPlayer().level.isClientSide) return;
//        player.getCapability(CapabilitySkillContainer).ifPresent((c) -> {
//            Optional<Skill> skill = c.getSkill(this);
//            if (skill.isPresent() && player.isAlive()) {
//                event.setLevels(event.getLevels() / (skill.get().getLevel() + 1));
//            }
//        });
//    }
}
