package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.msg.SprintMessage;
import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeToolMaster extends SkillType {
    public static final SkillTypeToolMaster Instance = new SkillTypeToolMaster();

    public SkillTypeToolMaster() {
        super(Builder.create().addParent(SkillTypeMandom.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void mount(PlayerEntity playerEntity, Skill skill) {

    }

    @SubscribeEvent
    public void onUsingTools(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        if (event.getPlayer().level.isClientSide) return;
        player.getCapability(DyingLight.CapabilitySkillContainer).ifPresent((c) -> c.getSkill(this).ifPresent(skill -> {
            if (skill.getLevel() == 0) return;
            Item item = event.getPlayer().getMainHandItem().getItem();
            if (item instanceof HoeItem
                    || item instanceof PickaxeItem
                    || item instanceof ShovelItem
                    || item instanceof AxeItem) {
                ((ToolItem) item).getDefaultInstance().setDamageValue(0);
                item.setDamage(item.getDefaultInstance(),0);
            }
        }));
    }

}
