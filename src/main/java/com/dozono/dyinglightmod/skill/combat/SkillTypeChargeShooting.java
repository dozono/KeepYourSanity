package com.dozono.dyinglightmod.skill.combat;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillContainer;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeChargeShooting extends SkillType {
    public static final SkillTypeChargeShooting INSTANCE = new SkillTypeChargeShooting();

    public static class ChargeSkill extends Skill {
        public int nockTick = 0;

        public ChargeSkill(SkillType type, SkillContainer skillContainer, PlayerEntity player) {
            super(type, skillContainer, player);
        }
    }

    public SkillTypeChargeShooting() {
        super(Builder.create().addParent(SkillTypeMarksmanship.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public Skill createSkill(SkillContainer skillContainer, PlayerEntity player) {
        return new ChargeSkill(this, skillContainer, player);
    }

    // TODO: support trident
    @SubscribeEvent
    public void onNock(ArrowNockEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.level.isClientSide) return;
        player.getCapability(CapabilitySkillContainer).ifPresent(c -> c.<ChargeSkill>getSkill(this).ifPresent(skill -> {
                    skill.nockTick = player.tickCount;
                }
        ));
    }

    @SubscribeEvent
    public void onLoose(ArrowLooseEvent event) {
        PlayerEntity player = event.getPlayer();
        ItemStack bow = event.getBow();
        if (player.level.isClientSide) return;
        player.getCapability(CapabilitySkillContainer).ifPresent(c -> c.<ChargeSkill>getSkill(this).ifPresent(skill -> {
            if (skill.getLevel() == 0) return;

            int diff = player.tickCount - skill.nockTick;
                    bow.setDamageValue(bow.getDamageValue() + diff / 20);
                }
        ));
    }
}

