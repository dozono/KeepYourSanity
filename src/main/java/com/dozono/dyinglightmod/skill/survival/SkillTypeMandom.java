package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;
import java.util.UUID;

public class SkillTypeMandom extends SkillType {
    private static final UUID MAX_HEALTH_UUID = UUID.fromString("56a11baf-7594-4ae8-8b9b-47c0370801f2");
    public static final SkillType INSTANCE = new SkillTypeMandom();

    private SkillTypeMandom() {
        super(Builder.create());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean onLevelUp(PlayerEntity player, Skill skill, int newLevel) {
        if (super.onLevelUp(player, skill, newLevel)) {
            if (player.level.isClientSide) {
                return true;
            }
            this.updateMaxHealth(player, newLevel);
            return true;
        }
        return false;
    }


    private void updateMaxHealth(PlayerEntity player, int newLevel) {
        ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MAX_HEALTH);
        int additionalHealth = newLevel * 6 + 2;
        if (attribute == null) {
            return;
        }
        AttributeModifier modifier = attribute.getModifier(MAX_HEALTH_UUID);
        if (modifier != null) {
            if (modifier.getAmount() == additionalHealth) return;
            attribute.removeModifier(modifier);
        }
        attribute.addPermanentModifier(new AttributeModifier(MAX_HEALTH_UUID, "skill_additional_health",
                additionalHealth, AttributeModifier.Operation.ADDITION));
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.level.isClientSide) return;
        Optional<Skill> skill = this.getSkill(player);
        if (skill.isPresent() && player.isAlive() && skill.get().getLevel() > 0) {
            this.updateMaxHealth(player, skill.get().getLevel());
            player.setHealth(40f);
        }
    }


    @Override
    public TextComponent getDescription(Skill skill) {
        return getCommonDescriptionContent(skill, "4", "7", "10");
    }
}
