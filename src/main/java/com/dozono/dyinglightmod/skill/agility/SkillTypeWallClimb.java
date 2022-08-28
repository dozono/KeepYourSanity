package com.dozono.dyinglightmod.skill.agility;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeWallClimb extends SkillType {

    public SkillTypeWallClimb() {
        super(Builder.create().addParent(SkillTypeDoubleJump.INSTANCE));
    }

    public static final SkillTypeWallClimb INSTANCE = new SkillTypeWallClimb();


    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        World level = player.level;
        player.getCapability(CapabilitySkillContainer).ifPresent(c -> c.getSkill(this).ifPresent(skill -> {
            if ((player.getDeltaMovement().x <= 0 || player.getDeltaMovement().z <= 0)
                    && !level.getBlockState(player.blockPosition()).is(Blocks.LADDER)
                    && !level.getBlockState(player.blockPosition()).is(Blocks.SCAFFOLDING)
                    && player.horizontalCollision
            ) {
                player.setDeltaMovement(player.getDeltaMovement().x, ((double) skill.getLevel()) + player.getDeltaMovement().y, player.getDeltaMovement().z);

            }
        }));


    }

}
