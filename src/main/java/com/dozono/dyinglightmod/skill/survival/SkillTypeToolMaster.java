package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.player.PlayerEntity;
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
        if (event.getPlayer().level.isClientSide) return;
        event.getPlayer().getCapability(CapabilitySkillContainer).ifPresent((c) -> {
            Optional<Skill> skill = c.getSkill(this);
            if (skill.isPresent()) {
                event.getPlayer().getMainHandItem().setDamageValue(0);
            }
        });
    }
}
