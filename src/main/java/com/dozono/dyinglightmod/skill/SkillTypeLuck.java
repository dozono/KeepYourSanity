package com.dozono.dyinglightmod.skill;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeLuck extends SkillType {
    public static final SkillTypeLuck INSTANCE = new SkillTypeLuck();

    private SkillTypeLuck() {
        super(Builder.create());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void mount(PlayerEntity playerEntity, Skill skill) {

    }

    @SubscribeEvent
    public void onGettingExp(PlayerXpEvent.PickupXp event) {
        PlayerEntity player = event.getPlayer();
        if (player.level.isClientSide) return;
        player.getCapability(CapabilitySkillContainer).ifPresent((c) -> {
            Optional<Skill> skill = c.getSkill(this);
            if (skill.isPresent()) {
                Skill luck = skill.get();
                event.getOrb().value *= luck.getLevel();
                ;
            }
        });
    }
}
