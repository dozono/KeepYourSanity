package com.dozono.dyinglightmod.skill.combat;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class SkillTypePlunder extends SkillType {
    public static final SkillTypePlunder INSTANCE = new SkillTypePlunder();
    public SkillTypePlunder() {
        super(Builder.create());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onAttackMob(AttackEntityEvent event){
        PlayerEntity player = event.getPlayer();
        if (player.level.isClientSide) return;
        player.getCapability(DyingLight.CapabilitySkillContainer).ifPresent(c->c.getSkill(this).ifPresent(skill -> {
            Entity target = event.getTarget();
            if (skill.getLevel() == 0) return;

            if(target instanceof MobEntity){
                if(player.level.random.nextInt(20)<=skill.getLevel()+1){
                    Vector3d position = target.position();
                    ItemEntity item = new ItemEntity(target.level,position.x,position.y,position.z,new ItemStack(()->Items.EMERALD));
                    target.level.addFreshEntity(item);
                }
            }
        }));
    }

}
