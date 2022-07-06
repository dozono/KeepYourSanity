package com.dozono.dyinglightmod.skill.agility;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import com.dozono.dyinglightmod.skill.combat.SkillTypeTBD;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeDoubleJump extends SkillType {
    public static final SkillTypeDoubleJump INSTANCE = new SkillTypeDoubleJump();

    public SkillTypeDoubleJump() {
        super(Builder.create().addParent(SkillTypeTBD.INSTACE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static class DoubleJumpSkill extends Skill {
        public boolean doubleJumped = false;
        public int doubleJumpDelay;
        public boolean releaseJump = false;

        public DoubleJumpSkill(SkillType type,PlayerEntity player) {
            super(type,player);
        }
    }

    @Override
    public Skill createSkill(PlayerEntity player) {
        return new DoubleJumpSkill(this,player);
    }

    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity) {
            entityLiving.getCapability(CapabilitySkillContainer).ifPresent(c -> c.<DoubleJumpSkill>getSkill(this).ifPresent(skill -> {
                skill.doubleJumpDelay = 5;
                skill.releaseJump = false;
            }));
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.level.isClientSide) {
            return;
        }
        PlayerEntity player = event.player;
        if (player instanceof ClientPlayerEntity) {
            ClientPlayerEntity clientPlayer = (ClientPlayerEntity) player;
            boolean jumping = clientPlayer.input.jumping;

            clientPlayer.getCapability(CapabilitySkillContainer).ifPresent(c -> c.<DoubleJumpSkill>getSkill(this).ifPresent(skill -> {
                if (!jumping) {
                    skill.releaseJump = true;
                } else if (skill.doubleJumpDelay-- <= 0 && !skill.doubleJumped && !clientPlayer.isOnGround() && !clientPlayer.isInWater() && !clientPlayer.isInWall() && skill.releaseJump) {
                    skill.doubleJumped = true;
                    performPlayerDoubleJump(clientPlayer);
                    DyingLight.CHANNEL.sendToServer(new DoubleJumpMessage());
                }
            }));
        }
    }

    public static void performPlayerDoubleJump(PlayerEntity player) {
        float f = 0.42F;
        if (player.hasEffect(Effects.JUMP)) {
            f += 0.1F * (float) (player.getEffect(Effects.JUMP).getAmplifier() + 1);
        }

        Vector3d vector3d = player.getDeltaMovement();
        player.setDeltaMovement(vector3d.x, f, vector3d.z);
        if (player.isSprinting()) {
            float f1 = player.yRot * ((float) Math.PI / 180F);
            player.setDeltaMovement(player.getDeltaMovement().add(-MathHelper.sin(f1) * 0.2F, 0.0D, MathHelper.cos(f1) * 0.2F));
        }

        player.hasImpulse = true;
    }

    @SubscribeEvent
    public void onPlayerTickUpdateDoubleJump(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;

        player.getCapability(CapabilitySkillContainer).ifPresent(c -> c.<DoubleJumpSkill>getSkill(this).ifPresent(skill -> {
            if (skill.doubleJumped && player.isOnGround() || player.isInWater() || player.isInWall()) {
                skill.doubleJumped = false;
            }
        }));
    }


}
