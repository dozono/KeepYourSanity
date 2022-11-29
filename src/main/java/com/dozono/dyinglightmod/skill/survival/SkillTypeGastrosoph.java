package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeGastrosoph extends SkillType {
    public static final SkillType INSTANCE = new SkillTypeGastrosoph();

    private SkillTypeGastrosoph() {
        super(Builder.create().addParent(SkillTypeMandom.INSTANCE));
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
            if(gastrosoph.getLevel()==0) return;
            int foodLevel = ((PlayerEntity) entity).getFoodData().getFoodLevel();
            int nutrition = foodProperties.getNutrition();
            int modified;
            if(gastrosoph.getLevel()==1){
                modified = (int) (foodLevel + nutrition * 1.15D);
            }else if(gastrosoph.getLevel()==2){
                modified = (int) (foodLevel + nutrition * 1.25D);
            }else {
                modified = (int) (foodLevel + nutrition * 1.35D);
            }
            ((PlayerEntity) entity).getFoodData().setFoodLevel(modified);
        }
    }

    @Override
    public TextComponent getDescription(Skill skill) {
        return getCommonDescriptionContent(skill,"15%","25%","35%");
    }
}
