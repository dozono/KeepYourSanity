package com.dozono.dyinglightmod.skill.agility;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.msg.AquaManMessage;
import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillContainer;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.client.Minecraft;
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

    @Override
    public Skill createSkill(SkillContainer skillContainer, PlayerEntity player) {
        return new SkillTypeAquaMan.AquaManSkill(this, skillContainer, player);
    }


    public static class AquaManSkill extends Skill {
        public AquaManSkill(SkillType type, SkillContainer skillContainer, PlayerEntity player) {
            super(type, skillContainer, player);
        }

        public int maxDolphin = this.getLevel()*2;

        public int existingDolphin = 0;
        public int dolphinLifespan = this.getLevel() * 400 + 1200;
    }

    @SubscribeEvent
    public void playerInWater(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (player.level.isClientSide) return;

        this.getSkill(player).ifPresent(skill -> {
            ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (skill.getLevel() == 0) return;
            if (attribute != null) {
                if (player.isInWater() && player.getEffect(Effects.WATER_BREATHING) == null) {
                    player.addEffect(new EffectInstance(Effects.WATER_BREATHING, 2400, 1, true, false, false));
                    AttributeModifier existed = attribute.getModifier(uuid);
                    if (existed == null) {
                        attribute.addTransientModifier(new AttributeModifier(uuid, "swim_speed", (float) skill.getLevel() / 2.5f, AttributeModifier.Operation.MULTIPLY_TOTAL));
                    }
                } else {
                    attribute.removeModifier(uuid);
                }
            }
        });
    }

    @SubscribeEvent
    public void dolphinTickEvent(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().level.isClientSide) {
            return;
        }
        LivingEntity entityLiving = event.getEntityLiving();
        if(entityLiving instanceof DolphinEntity && entityLiving.getTags().contains("aqua_man")){
            if (Minecraft.getInstance().player == null) return;
            this.getSkill(Minecraft.getInstance().player).map(v -> (AquaManSkill) v).ifPresent((skill -> {
                if (entityLiving.tickCount>=skill.dolphinLifespan){
                    entityLiving.kill();
                    skill.existingDolphin-=1;
                    AquaManMessage msg = new AquaManMessage();
                    DyingLight.CHANNEL.sendToServer(msg);
                }
            }));
        }
    }

    @SubscribeEvent
    public void playerHurtEvent(LivingHurtEvent event) {
        LivingEntity victim = event.getEntityLiving();
        if (victim.level.isClientSide) return;
        Entity attacker = event.getSource().getEntity();
        if (victim instanceof PlayerEntity && attacker instanceof MobEntity) {
            this.getSkill(victim).map(v -> (AquaManSkill) v).ifPresent((skill -> {
                if (victim.isInWater() && skill.existingDolphin<=skill.getLevel()*3) {
                    spawnDolphin((PlayerEntity) victim, (MobEntity) attacker);
                    skill.existingDolphin+=1;
                    AquaManMessage msg = new AquaManMessage();
                    DyingLight.CHANNEL.sendToServer(msg);
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

    }
}
