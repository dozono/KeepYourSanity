package com.dozono.dyinglightmod.skill.combat;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillContainer;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;
import java.util.jar.Attributes;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeChargeShooting extends SkillType {
    public static final SkillTypeChargeShooting INSTANCE = new SkillTypeChargeShooting();

    public static class ChargeSkill extends Skill {
        public int nockTick = 0;
        public int diff = 0;

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
        ChargeSkill skill = (ChargeSkill) this.getSkill(player).orElse(null);
        if (skill == null) return;
        skill.nockTick = player.tickCount;
    }

    @SubscribeEvent
    public void onUse(LivingEntityUseItemEvent.Tick event) {
        Entity entity = event.getEntity();
        ItemStack item = event.getItem();
        if (!(entity instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = ((PlayerEntity) entity);
        ChargeSkill skill = (ChargeSkill) this.getSkill(player).orElse(null);
        if (skill == null) return;

        if (player.level.isClientSide) {
            if (player.tickCount - skill.nockTick > skill.getLevel() * 50 && item.getItem()==Items.BOW) {
                spawnParticle(player, player.getRandom());
            }
        }
    }

    private void spawnParticle(PlayerEntity player, Random rnd) {
        for (int i = 0; i < 3; ++i) {
            int j = rnd.nextInt(2) * 2 - 1;
            int k = rnd.nextInt(2) * 2 - 1;

            Vector3d angle = player.getLookAngle().scale(0.8D).yRot(-(float) (Math.PI / 8));

            double d0 = player.getX() + angle.x;
            double d1 = (float) player.getEyeY() - 0.2 + angle.y;
            double d2 = player.getZ() + angle.z;
            double d3 = rnd.nextFloat() * (float) j;
            double d4 = ((double) rnd.nextFloat() - 0.5D) * 0.125D;
            double d5 = rnd.nextFloat() * (float) k;
            player.level.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }


    @SubscribeEvent
    public void onLoose(ArrowLooseEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.level.isClientSide) return;
        ChargeSkill skill = (ChargeSkill) this.getSkill(player).orElse(null);
        if (skill == null) return;
        if (skill.getLevel() == 0) return;
        skill.diff = Math.min(player.tickCount - skill.nockTick, skill.getLevel() * 50);
    }

    @SubscribeEvent
    public void onArrowJoinWorld(EntityJoinWorldEvent event) {
        if (event.getWorld().isClientSide) return;
        Entity entity = event.getEntity();
        if (entity instanceof AbstractArrowEntity) {
            AbstractArrowEntity arrow = (AbstractArrowEntity) entity;
            if (arrow.getOwner() instanceof PlayerEntity) {
                arrow.getOwner().getCapability(CapabilitySkillContainer).ifPresent(c -> c.<ChargeSkill>getSkill(this).ifPresent(skill -> {
                            arrow.setBaseDamage(arrow.getBaseDamage() + skill.diff / 20d);
                            skill.diff = 0;
                        }
                ));
            }
        }

    }

    @Override
    public TextComponent getDescription(Skill skill) {
        return getCommonDescriptionContent(skill, "2.5", "5", "7.5");
    }
}

