package com.dozono.dyinglightmod.skill;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeGastrosoph extends SkillType {
    public static final SkillTypeGastrosoph Instance = new SkillTypeGastrosoph();

    private SkillTypeGastrosoph() {
        super(Builder.create());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void mount(PlayerEntity playerEntity, Skill skill) {

    }

    @SubscribeEvent
    public void onEatingFood(LivingEntityUseItemEvent.Finish event) {
        Entity entity = event.getEntity();
        if (entity.level.isClientSide) return;
        Food foodProperties = event.getItem().getItem().getFoodProperties();
        if (foodProperties == null) return;
        entity.getCapability(CapabilitySkillContainer).ifPresent((c) -> {
            Optional<Skill> skill = c.getSkill(this);
            if (skill.isPresent()) {
                Skill gastrosoph = skill.get();
                int foodLevel = ((PlayerEntity) entity).getFoodData().getFoodLevel();
                int nutrition = foodProperties.getNutrition();
                int modified = (int) (foodLevel + nutrition * Math.pow(gastrosoph.getLevel() + 0.5, 2));
                ((PlayerEntity) entity).getFoodData().setFoodLevel(modified);
            }
        });
    }
}
