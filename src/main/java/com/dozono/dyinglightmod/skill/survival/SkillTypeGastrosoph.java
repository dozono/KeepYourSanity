package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeGastrosoph extends SkillType {
    public static final SkillType INSTANCE = new SkillTypeGastrosoph();

    private SkillTypeGastrosoph() {
        super(Builder.create().addParent(SkillTypeTireless.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEatingFood(LivingEntityUseItemEvent.Finish event) {
        Entity entity = event.getEntity();
        if (entity.level.isClientSide) return;
        Food foodProperties = event.getItem().getItem().getFoodProperties();
        if (foodProperties == null) return;
        Optional<Skill> skill = this.getSkill(event.getEntityLiving());
        if (skill.isPresent()) {
            Skill gastrosoph = skill.get();
            int foodLevel = ((PlayerEntity) entity).getFoodData().getFoodLevel();
            int nutrition = foodProperties.getNutrition();
            int modified = (int) (foodLevel + nutrition * Math.pow(gastrosoph.getLevel() + 0.5, 2));
            ((PlayerEntity) entity).getFoodData().setFoodLevel(modified);
        }
    }
}
