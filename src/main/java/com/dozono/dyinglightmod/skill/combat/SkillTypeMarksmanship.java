package com.dozono.dyinglightmod.skill.combat;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.block.BarrelBlock;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeMarksmanship extends SkillType {

    public static final UUID SPEED_UUID = UUID.randomUUID();
    public static final SkillTypeMarksmanship INSTANCE = new SkillTypeMarksmanship();

    public SkillTypeMarksmanship() {

        super(Builder.create().addParent(SkillTypeTBD.INSTACE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    //TODO:increase nocking speed && "movement speed while nocking"
    @SubscribeEvent
    public void onShooting(ArrowNockEvent event) {
        PlayerEntity player = event.getPlayer();
        ActionResult<ItemStack> result = event.getAction();
        ItemStack bow = event.getBow();
        if (player.level.isClientSide) return;
        player.getCapability(CapabilitySkillContainer).ifPresent((c) -> c.getSkill(this).ifPresent(skill -> {
            ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (attribute != null) {
                double defaultSpeed = attribute.getValue();
                attribute.addTransientModifier(new AttributeModifier(SPEED_UUID, ("skill_speed"), defaultSpeed, AttributeModifier.Operation.ADDITION));
            }
        }));
    }

    @SubscribeEvent
    public void onLoose(ArrowLooseEvent event) {
        PlayerEntity player = event.getPlayer();
        ItemStack bow = event.getBow();
        if (player.level.isClientSide) return;
        player.getCapability(CapabilitySkillContainer).ifPresent((c) -> c.getSkill(this).ifPresent(skill -> {
            ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (attribute != null) {
                bow.setDamageValue(bow.getDamageValue() + skill.getLevel());
                attribute.removeModifier(SPEED_UUID);
            }
        }));
    }
}
