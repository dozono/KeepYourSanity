package com.dozono.dyinglightmod.skill.agility;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.msg.WallClimbMessage;
import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillContainer;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

public class SkillTypeWallClimb extends SkillType {
    static KeyBinding wKey = new KeyBinding("w", 87, DyingLight.MODID);
    public static KeyBinding vKey = new KeyBinding("v",86,DyingLight.MODID);

    public SkillTypeWallClimb() {
        super(Builder.create().addParent(SkillTypeDoubleJump.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);

    }

    @Override
    public Skill createSkill(SkillContainer skillContainer, PlayerEntity player) {
        return new WallClimbSkill(this, skillContainer, player);
    }

    public static final SkillTypeWallClimb INSTANCE = new SkillTypeWallClimb();


    public static class WallClimbSkill extends Skill {
        public WallClimbSkill(SkillType type, SkillContainer skillContainer, PlayerEntity player) {
            super(type, skillContainer, player);
        }

        public boolean canClimb = false;
        public float climbHeight = 0;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            return;
        }
        PlayerEntity player = event.player;

        this.getSkill(player).map(v -> ((WallClimbSkill) v)).ifPresent(skill -> {
            if (skill.getLevel() == 0) return;
            if (wKey.isDown() && vKey.isDown() && player.horizontalCollision) {
                if (!skill.canClimb) {
                    skill.canClimb = true;
                    WallClimbMessage message = new WallClimbMessage();
                    DyingLight.CHANNEL.sendToServer(message);
                }
            } else {
                if (skill.canClimb) {
                    skill.canClimb = false;
                    WallClimbMessage message = new WallClimbMessage();
                    DyingLight.CHANNEL.sendToServer(message);
                }
            }
        });
    }

    public boolean onHook(PlayerEntity player) {
        Optional<WallClimbSkill> cap = this.getSkill(player).map(v -> ((WallClimbSkill) v));
        if (cap.isPresent()) {
            WallClimbSkill skill = cap.get();
            return skill.canClimb;
        }

        return false;
    }


}
