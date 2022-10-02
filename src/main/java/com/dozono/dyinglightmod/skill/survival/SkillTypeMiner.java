package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeMiner extends SkillType {
    public static final SkillTypeMiner Instance = new SkillTypeMiner();

    public SkillTypeMiner() {
        super(Builder.create().addParent(SkillTypeTireless.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onMining(PlayerEvent.BreakSpeed event) {
        event.getPlayer().getCapability(CapabilitySkillContainer).ifPresent((c) -> {
            Optional<Skill> skill = c.getSkill(this);
            Item heldItem = event.getPlayer().getMainHandItem().getItem();
            if (skill.isPresent() && heldItem instanceof PickaxeItem) {
                event.setNewSpeed(event.getOriginalSpeed()+skill.get().getLevel());
                event.getPlayer().getMainHandItem().getItem().setDamage(heldItem.getDefaultInstance(), heldItem.getDamage(heldItem.getDefaultInstance())+skill.get().getLevel());
            }
        });
    }
}
