package com.dozono.dyinglightmod.skill.agility;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeRunner extends SkillType {

    public SkillTypeRunner() {
        super(Builder.create().addParent(SkillTypeStrongLegs.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static final SkillTypeRunner INSTANCE = new SkillTypeRunner();

    public static final UUID uuid = UUID.fromString("0e4cc2e6-3e69-4e0e-8577-76f9e5cdae8e");

    @SubscribeEvent
    public void onSprint(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (player.level.isClientSide) return;
        player.getCapability(CapabilitySkillContainer).ifPresent(c -> c.getSkill(this).ifPresent(skill -> {
            ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (skill.getLevel() == 0) return;

            if (attribute != null) {
                if (player.isSprinting()) {
                    AttributeModifier existed = attribute.getModifier(uuid);
                    if (existed == null) {
                        attribute.addTransientModifier(new AttributeModifier(uuid, "sprint_speed", skill.getLevel()/10f, AttributeModifier.Operation.MULTIPLY_TOTAL));
                    }
                } else {
                    attribute.removeModifier(uuid);
                }
            }
        }));
    }

    @Override
    public TextComponent getDescription(Skill skill) {
        return getCommonDescriptionContent(skill,"10%","20%","30%");
    }
}
