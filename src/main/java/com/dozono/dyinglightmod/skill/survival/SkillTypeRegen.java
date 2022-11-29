package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeRegen extends SkillType {
    public static final SkillTypeRegen INSTANCE = new SkillTypeRegen();


    private SkillTypeRegen() {
        super(Builder.create().addParent(SkillTypeLuck.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }


    @Override
    public void mount(PlayerEntity playerEntity, Skill skill) {

    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        LivingEntity victim = event.getEntityLiving();
        if (victim instanceof PlayerEntity) {
            if (victim.level.isClientSide) return;
            this.getSkill(victim).ifPresent(c -> {
                if (c.getLevel() == 0) return;
                if (event.getSource().getEntity() instanceof MobEntity && !victim.hasEffect(Effects.REGENERATION)) {
//                    victim.addEffect(new EffectInstance(Effects.REGENERATION, c.getLevel() * 25, 1));
                    victim.addEffect(new EffectInstance(Effects.REGENERATION, c.getLevel() * 20,1,true,
                            false,false));
                }
            });
        }
    }

    @Override
    public TextComponent getDescription(Skill skill) {
        return getCommonDescriptionContent(skill,"1","2","3");
    }
}
