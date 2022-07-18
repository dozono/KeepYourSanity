package com.dozono.dyinglightmod.skill.combat;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeWeaponMaster extends SkillType {
    public static final SkillTypeWeaponMaster INSTANCE = new SkillTypeWeaponMaster();

    public SkillTypeWeaponMaster() {
        super(Builder.create().addParent(SkillTypeTBD.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }


    //TODO:increase attack speed
    @SubscribeEvent
    public void onAttacking(AttackEntityEvent event) {
        LivingEntity victim = event.getEntityLiving();
        PlayerEntity player = event.getPlayer();
        Item heldItem = player.getMainHandItem().getItem();
        if (player.level.isClientSide) return;
        if (victim instanceof MobEntity && heldItem instanceof SwordItem || heldItem instanceof AxeItem) {
            player.getCapability(CapabilitySkillContainer).ifPresent(c -> c.getSkill(this).ifPresent(skill -> {
                        victim.hurt(new DamageSource("player_additional_attack"), heldItem.getDamage(heldItem.getDefaultInstance()));

                    }
            ));
        }
    }

}
