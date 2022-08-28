package com.dozono.dyinglightmod.msg;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.skill.agility.SkillTypeDoubleJump;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class DoubleJumpMessage {
    public DoubleJumpMessage() {
    }

    public static void encode(DoubleJumpMessage msg, PacketBuffer buffer) {
    }

    public static DoubleJumpMessage decode(PacketBuffer buffer) {
        return new DoubleJumpMessage();
    }

    public static void handle(DoubleJumpMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();
            sender.getCapability(DyingLight.CapabilitySkillContainer).ifPresent(c -> {
                SkillTypeDoubleJump.DoubleJumpSkill skill = (SkillTypeDoubleJump.DoubleJumpSkill) c.getSkill(SkillTypeDoubleJump.INSTANCE).orElseThrow(() -> new RuntimeException());
                skill.doubleJumped = true;
                SkillTypeDoubleJump.performPlayerDoubleJump(sender);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
