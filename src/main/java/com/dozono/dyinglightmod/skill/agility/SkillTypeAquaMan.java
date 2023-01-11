package com.dozono.dyinglightmod.skill.agility;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillContainer;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class SkillTypeAquaMan extends SkillType {
    public static final SkillTypeAquaMan INSTANCE = new SkillTypeAquaMan();
    public static final UUID uuid = UUID.fromString("a66736b2-6b79-4ad9-8de4-c8bca3ff52d4");


    public SkillTypeAquaMan() {
        super(Builder.create().addParent(SkillTypeDoubleJump.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public Skill createSkill(SkillContainer skillContainer, PlayerEntity player) {
        return new SkillTypeAquaMan.AquaManSkill(this, skillContainer, player);
    }


    public static class AquaManSkill extends Skill {
        public AquaManSkill(SkillType type, SkillContainer skillContainer, PlayerEntity player) {
            super(type, skillContainer, player);
        }

        DolphinEntity dolphin;

    }

    @SubscribeEvent
    public void playerInWater(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (player.level.isClientSide) return;

        this.getSkill(player).map(v -> ((SkillTypeAquaMan.AquaManSkill) v)).ifPresent(skill -> {
                    if (skill.getLevel() == 0) return;

                    if (player.isInWater()) {
                        player.addEffect(new EffectInstance(Effects.DOLPHINS_GRACE,20,1,false,false,false));
                        player.addEffect(new EffectInstance(Effects.WATER_BREATHING, 20, 1, false, false, false));
                        if (skill.dolphin == null || !skill.dolphin.isAlive()) {
                            skill.dolphin = new DolphinEntity(EntityType.DOLPHIN, player.level);
                            skill.dolphin.addTag("aqua_man");
                            skill.dolphin.setPos(player.position().x, player.position().y, player.position().z);
                            player.level.addFreshEntity(skill.dolphin);
                        }

                    } else if (!player.isInWater() && skill.dolphin != null) {
                        skill.dolphin.remove();
                        skill.dolphin = null;
                    }
                }

        );
    }

}
