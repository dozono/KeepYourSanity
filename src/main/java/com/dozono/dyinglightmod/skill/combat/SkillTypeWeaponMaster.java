package com.dozono.dyinglightmod.skill.combat;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeWeaponMaster extends SkillType {
    public static final SkillTypeWeaponMaster INSTANCE = new SkillTypeWeaponMaster();
    private static final UUID ATTACK_SPEED_UUID = UUID.fromString("48166935-cb8a-47e9-8bf2-a45fbf9c65f5");

    public SkillTypeWeaponMaster() {
        super(Builder.create().addParent(SkillTypePlunder.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }


    @SubscribeEvent
    public void onAttacking(AttackEntityEvent event) {
        LivingEntity victim = event.getEntityLiving();
        PlayerEntity player = event.getPlayer();
        Item heldItem = player.getMainHandItem().getItem();
        if (player.level.isClientSide) return;
        if (victim instanceof MobEntity && heldItem instanceof SwordItem || heldItem instanceof AxeItem) {

            player.getCapability(CapabilitySkillContainer).ifPresent(c -> c.getSkill(this).ifPresent(skill -> {
                if (skill.getLevel() == 0) return;
                ModifiableAttributeInstance attributes = player.getAttribute(Attributes.ATTACK_SPEED);
                if(attributes != null){
                    AttributeModifier modifier = attributes.getModifier(ATTACK_SPEED_UUID);
                    if(modifier == null){
                        attributes.addTransientModifier(new AttributeModifier(ATTACK_SPEED_UUID,"attack_speed", skill.getLevel()*100, AttributeModifier.Operation.ADDITION));
                        System.out.println(attributes.getModifier(ATTACK_SPEED_UUID));
                    }
                    if(modifier!=null){
                        attributes.removeModifier(ATTACK_SPEED_UUID );
                    }
                }
                victim.hurt(new DamageSource("player_additional_attack"), heldItem.getDamage(heldItem.getDefaultInstance())/(5f-skill.getLevel()));

                    }
            ));
        }
    }

//    @SubscribeEvent
//    public void playerTick(TickEvent.PlayerTickEvent event) {
//        PlayerEntity player = event.player;
//        if (player.level.isClientSide) return;
//        player.getCapability(CapabilitySkillContainer).ifPresent(c -> c.getSkill(this).ifPresent(skill -> {
//                    if (skill.getLevel() == 0) return;
//                    ModifiableAttributeInstance attributes = player.getAttribute(Attributes.ATTACK_SPEED);
//                    if (attributes != null) {
//                        AttributeModifier modifier = attributes.getModifier(ATTACK_SPEED_UUID);
//                        if (modifier != null) {
//                            attributes.addPermanentModifier(new AttributeModifier(ATTACK_SPEED_UUID, "attack_speed", skill.getLevel(), AttributeModifier.Operation.ADDITION));
//                        }
//                    }
//                }
//        ));
//    }

    @Override
    public TextComponent getDescription(Skill skill) {
        return getCommonDescriptionContent(skill,"15%","20%","25%");
    }
}


