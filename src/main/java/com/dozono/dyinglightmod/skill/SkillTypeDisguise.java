package com.dozono.dyinglightmod.skill;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeDisguise extends SkillType {
    public static final SkillTypeDisguise Instance = new SkillTypeDisguise();
    private boolean canDisguise;
    private int tick = 0;

    private SkillTypeDisguise() {
        super(Builder.create().dependOn(SkillTypeLuck.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void mount(PlayerEntity playerEntity, Skill skill) {

    }

    @SubscribeEvent
    public void canDisguise(LivingHurtEvent event){
        Entity entity = event.getEntity();
        if (!(entity instanceof PlayerEntity) && event.getEntity().level.isClientSide) {
            canDisguise =  false;
        }
        if(entity instanceof PlayerEntity){
            DamageSource source = event.getSource();
            canDisguise =  !(source.getEntity() instanceof MobEntity);
            tick = 0;
        }else {
            canDisguise = tick<100;
        }
    }

    @SubscribeEvent
    public void tick(TickEvent.PlayerTickEvent event){
        if(tick<100 && !canDisguise){
            tick++;
        }else if(tick>=100 && canDisguise) {
            tick = 0;
        }
    }

    @SubscribeEvent
    public void onSneaking(LivingSetAttackTargetEvent event) {
        LivingEntity attacker = event.getEntityLiving();
        LivingEntity victim = event.getTarget();
        if (attacker.level.isClientSide) return;

        if (victim instanceof PlayerEntity && canDisguise) {
            victim.getCapability(CapabilitySkillContainer).ifPresent(c -> c.getSkill(this).ifPresent(skill -> {
                        if (attacker instanceof MobEntity && victim.isShiftKeyDown()) {
                            ((MobEntity) attacker).setTarget(null);
                        }
                    }
            ));
        }
    }
}
