package com.dozono.dyinglightmod.skill.combat;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeMarksmanship extends SkillType {

    public static final UUID SPEED_UUID = UUID.randomUUID();
    public static final SkillTypeMarksmanship INSTANCE = new SkillTypeMarksmanship();

    public SkillTypeMarksmanship() {
        super(Builder.create().addParent(SkillTypePlunder.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onShooting(ArrowNockEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.level.isClientSide) return;
        Skill skill = this.getSkill(player).orElse(null);
        if (skill == null) return;
        ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (skill.getLevel() == 0) return;
        if (attribute != null && event.hasAmmo()) {
            double defaultSpeed = 0.04 * (skill.getLevel());
            System.out.println(attribute.getValue());
            if (attribute.getModifier(SPEED_UUID) == null) {
                attribute.addTransientModifier(new AttributeModifier(SPEED_UUID, ("skill_speed"), defaultSpeed, AttributeModifier.Operation.ADDITION));
            }
        }
    }

    @SubscribeEvent
    public void onItemUseFinished(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        PlayerEntity player = event.player;
        if (player.level.isClientSide) return;
        Skill skill = this.getSkill(player).orElse(null);
        if (skill == null) return;
        ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute == null) return;
        ItemStack item = player.getUseItem();
        if (item.getItem() != Items.BOW) {
            attribute.removeModifier(SPEED_UUID);
        }
    }


    @SubscribeEvent
    public void onLoose(ArrowLooseEvent event) {
        PlayerEntity player = event.getPlayer();
        ItemStack bow = event.getBow();
        if (player.level.isClientSide) return;
        player.getCapability(CapabilitySkillContainer).ifPresent((c) -> c.getSkill(this).ifPresent(skill -> {
            ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (skill.getLevel() == 0) return;
            if (attribute != null) {
                bow.setDamageValue(bow.getDamageValue() + skill.getLevel());
                attribute.removeModifier(SPEED_UUID);
            }
        }));
    }

    @SubscribeEvent
    public void onArrowCreated(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ArrowEntity) {
            if (((ArrowEntity) entity).getOwner() instanceof PlayerEntity) {
                ((ArrowEntity) entity).getOwner().getCapability(CapabilitySkillContainer).ifPresent((c) -> c.getSkill(this).ifPresent(skill -> {
                    if (skill.getLevel() == 0) return;
                    Vector3d movement = entity.getDeltaMovement();
                    double multiplier = 1d + skill.getLevel() / 10d;
//                    Vector3d normalize = entity.getDeltaMovement().normalize();
                    entity.setDeltaMovement(movement.scale(multiplier));
                }));
            }
        }
    }
}
