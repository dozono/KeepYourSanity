package com.dozono.dyinglightmod.skill.agility;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
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
            ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (skill.getLevel() == 0) return;
            if (attribute != null) {
                if (player.isInWater() && player.getEffect(Effects.WATER_BREATHING)==null) {
                    player.addEffect(new EffectInstance(Effects.WATER_BREATHING,2400,1,true,false,false));
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
    public void dolphinTickEvent(LivingDeathEvent event){
        LivingEntity entityLiving = event.getEntityLiving();
        DamageSource source = event.getSource();
        if(source.getEntity() ==null) return;
        if(entityLiving instanceof MobEntity && source.getEntity().getTags().contains("aqua_man")){
            source.getEntity().kill();
        }
    }

    @SubscribeEvent
    public void playerHurtEvent(LivingHurtEvent event) {
        LivingEntity victim = event.getEntityLiving();
        Entity attacker = event.getSource().getEntity();
        if (victim instanceof PlayerEntity && attacker instanceof MobEntity) {
            if (victim.level.isClientSide) return;
            victim.getCapability(CapabilitySkillContainer).ifPresent(c -> c.getSkill(this).ifPresent(skill -> {
                if (victim.isInWater() && attacker instanceof MobEntity) {
                    spawnDolphin((PlayerEntity) victim,(MobEntity)attacker);
                }
            }));
        }
    }

    private void spawnDolphin(PlayerEntity player, LivingEntity target) {
        DolphinEntity dolphin = new DolphinEntity(EntityType.DOLPHIN, target.level);
        dolphin.addTag("aqua_man");
        dolphin.setPos(player.position().x, player.position().y, player.position().z);
        player.level.addFreshEntity(dolphin);
        dolphin.setTarget(target);
        dolphin.doHurtTarget(target);
    }

}
