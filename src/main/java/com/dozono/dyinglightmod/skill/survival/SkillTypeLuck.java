package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeLuck extends SkillType {
    public static final SkillTypeLuck INSTANCE = new SkillTypeLuck();

    private SkillTypeLuck() {
        super(Builder.create().addParent(SkillTypeMandom.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGettingExp(PlayerXpEvent.PickupXp event) {
        PlayerEntity player = event.getPlayer();
        if (player.level.isClientSide) return;
        Optional<Skill> skill = this.getSkill(player);
        if (skill.isPresent() && skill.get().getLevel() > 0) {
            Skill luck = skill.get();
            event.getOrb().value *= luck.getLevel();
        }
    }
}
