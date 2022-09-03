package com.dozono.dyinglightmod.skill.agility;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeAquaMan extends SkillType {
    public static final SkillTypeAquaMan INSTANCE = new SkillTypeAquaMan();
    public static final UUID uuid = UUID.fromString("a66736b2-6b79-4ad9-8de4-c8bca3ff52d4");

    public SkillTypeAquaMan() {
        super(Builder.create().addParent(SkillTypeDoubleJump.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void playerInWater(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (player.level.isClientSide) return;

        player.getCapability(DyingLight.CapabilitySkillContainer).ifPresent(c -> c.getSkill(this).ifPresent(skill -> {
            ModifiableAttributeInstance attribute = player.getAttribute(Attributes.FLYING_SPEED);
            if (attribute != null) {
                if (player.isInWater()) {
                    player.addEffect(new EffectInstance(Effects.WATER_BREATHING));
                    AttributeModifier existed = attribute.getModifier(uuid);
                    if (existed == null) {
                        attribute.addTransientModifier(new AttributeModifier(uuid, "swim_speed", (float) skill.getLevel() / 2.5f, AttributeModifier.Operation.MULTIPLY_TOTAL));
                    }
                } else {
                    attribute.removeModifier(uuid);
                }
            }

        }));

    }

    @SubscribeEvent
    public void playerHurtEvent(LivingHurtEvent event) {
        LivingEntity victim = event.getEntityLiving();
        if (victim instanceof PlayerEntity && victim.level.isClientSide) return;
        victim.getCapability(DyingLight.CapabilitySkillContainer).ifPresent(c -> c.getSkill(this).ifPresent(skill -> {
            if (event.getSource().getEntity() instanceof MobEntity) {
                DolphinEntity dolphin = new DolphinEntity(EntityType.DOLPHIN, victim.level);
                dolphin.setPos(victim.position().x, victim.position().y, victim.position().z);
                victim.level.addFreshEntity(dolphin);
                dolphin.setTarget((LivingEntity) event.getSource().getEntity());
            }
        }));
    }

}
