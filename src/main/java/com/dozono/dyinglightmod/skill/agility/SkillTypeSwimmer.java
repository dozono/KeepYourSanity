package com.dozono.dyinglightmod.skill.agility;

import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeSwimmer extends SkillType {
    public static final SkillTypeSwimmer INSTANCE = new SkillTypeSwimmer();
    public static final UUID uuid = UUID.fromString("a66736b2-6b79-4ad9-8de4-c8bca3ff52d4");
    public SkillTypeSwimmer() {
        super(Builder.create().addParent(SkillTypeRunner.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onSwimming(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        if (player.level.isClientSide) return;
        if(player.swinging){
            player.getCapability(CapabilitySkillContainer).ifPresent(c->c.getSkill(this).ifPresent(skill -> {
                ModifiableAttributeInstance attribute = player.getAttribute(Attributes.FLYING_SPEED);
                if (attribute != null) {
                    if (player.isInWater()) {
                        AttributeModifier existed = attribute.getModifier(uuid);
                        if (existed == null) {
                            attribute.addTransientModifier(new AttributeModifier(uuid, "swim_speed", (float)skill.getLevel()/2.5f, AttributeModifier.Operation.MULTIPLY_TOTAL));
                        }
                    } else {
                        attribute.removeModifier(uuid);
                    }
                }
            }));
        }
    }
}
