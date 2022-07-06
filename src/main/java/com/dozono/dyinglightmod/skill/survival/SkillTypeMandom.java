package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;
import java.util.UUID;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeMandom extends SkillType {
    private static final UUID MAX_HEALTH_UUID = UUID.fromString("56a11baf-7594-4ae8-8b9b-47c0370801f2");
    public static final SkillType INSTANCE = new SkillTypeMandom();

    private SkillTypeMandom() {
        super(Builder.create());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean onLevelUp(PlayerEntity player, Skill skill) {
        if (super.onLevelUp(player, skill)) {
            if (player.level.isClientSide) {
                return true;
            }
            this.updateMaxHealth(player, skill);
            return true;
        }
        return false;
    }

    private void updateMaxHealth(PlayerEntity player, Skill skill) {
        ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MAX_HEALTH);
        int additionalHealth = skill.getLevel() * 20;
        if (attribute != null) {
            AttributeModifier modifier = attribute.getModifier(MAX_HEALTH_UUID);
            if (modifier == null || modifier.getAmount() != additionalHealth) {
                if (modifier != null) {
                    attribute.removeModifier(modifier);
                }
                attribute.addTransientModifier(new AttributeModifier("skill_additional_health",
                        additionalHealth, AttributeModifier.Operation.ADDITION));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.level.isClientSide) return;
        Optional<Skill> skill = this.getSkill(player);
        if (skill.isPresent() && player.isAlive() && skill.get().getLevel() > 0) {
            this.updateMaxHealth(player, skill.get());
        }
    }
//
//    @SubscribeEvent
//    public void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
//        updateMaxHealth(player);
//    }
}
