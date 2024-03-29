package com.dozono.dyinglightmod.skill.combat;

import com.dozono.dyinglightmod.mixin.PlayerEntityMixin;
import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;

import java.lang.reflect.Field;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;
import static com.dozono.dyinglightmod.DyingLight.UNDYING_EFFECT;

public class SkillTypeDeathDenied extends SkillType {
    public static final SkillTypeDeathDenied INSTANCE = new SkillTypeDeathDenied();
    public static DataParameter<Integer> COOL_DOWN;

    static {
        try {
//            COOL_DOWN= PlayerEntity["COOL_DOWN"]
            Field field = PlayerEntity.class.getDeclaredField("COOL_DOWN");
            field.setAccessible(true);
            COOL_DOWN = (DataParameter<Integer>) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public SkillTypeDeathDenied() {
        super(Builder.create().addParent(SkillTypePlunder.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void mount(PlayerEntity playerEntity, Skill skill) {
//        playerEntity.getEntityData().define(COOL_DOWN, 0);
    }


    public static class UndyingEffect extends Effect {
        public UndyingEffect(EffectType p_i50391_1_, int p_i50391_2_) {
            super(p_i50391_1_, p_i50391_2_);
        }

        @Override
        public void applyEffectTick(LivingEntity e, int tick) {
            super.applyEffectTick(e, tick);
        }

        @Override
        public void applyInstantenousEffect(@Nullable Entity p_180793_1_, @Nullable Entity p_180793_2_, LivingEntity p_180793_3_, int p_180793_4_, double p_180793_5_) {
            super.applyInstantenousEffect(p_180793_1_, p_180793_2_, p_180793_3_, p_180793_4_, p_180793_5_);
        }

        @Override
        public void removeAttributeModifiers(LivingEntity e, AttributeModifierManager p_111187_2_, int p_111187_3_) {
            e.getEntityData().set(COOL_DOWN, 3600);
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        LivingEntity target = event.getEntityLiving();

        if (target.level.isClientSide) return;

        if (target instanceof PlayerEntity) {
            target.getCapability(CapabilitySkillContainer).ifPresent(c -> c.getSkill(this).ifPresent(skill -> {
                if (skill.getLevel() == 0) {
                    return;
                }
                if (target.getEffect(UNDYING_EFFECT.get()) != null) {
                    event.setCanceled(true);
                    target.setHealth(0.5f);
                } else if (!((PlayerEntity) target).inventory.contains(Items.TOTEM_OF_UNDYING.getDefaultInstance())) {
                    if (target.getEntityData().get(COOL_DOWN) == 0) {
                        event.setCanceled(true);
                        target.setHealth(0.5f);
                        target.addEffect(new EffectInstance(UNDYING_EFFECT.get(), skill.getLevel()*20+80));
                    }
                }
            }));
        }

        if (!event.isCanceled()) {
            Entity entity = event.getSource().getEntity();
            if (entity instanceof LivingEntity) {
                EffectInstance effect = ((LivingEntity) entity).getEffect(UNDYING_EFFECT.get());
                if (effect != null) {
                    ((LivingEntity) entity).removeEffect(UNDYING_EFFECT.get());
                    ((LivingEntity) entity).heal(6);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerHeal(LivingHealEvent event) {
        if (event.getEntityLiving().getEffect(UNDYING_EFFECT.get()) != null) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.level.isClientSide) return;
        Integer integer = event.player.getEntityData().get(COOL_DOWN);
        if (integer != null && integer > 0) {
            event.player.getEntityData().set(COOL_DOWN, integer.intValue() - 1);
        }
    }

    @Override
    public TextComponent getDescription(Skill skill) {
        return getCommonDescriptionContent(skill,"5","6","7");
    }
}
