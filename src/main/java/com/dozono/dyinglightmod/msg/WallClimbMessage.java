package com.dozono.dyinglightmod.msg;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import com.dozono.dyinglightmod.skill.agility.SkillTypeDoubleJump;
import com.dozono.dyinglightmod.skill.agility.SkillTypeWallClimb;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class WallClimbMessage {
    public boolean value;
    public WallClimbMessage() {
    }

    public static void encode(WallClimbMessage msg, PacketBuffer buffer) {
        buffer.writeBoolean(msg.value);
    }

    public static WallClimbMessage decode(PacketBuffer buffer) {
        WallClimbMessage message = new WallClimbMessage();
        message.value = buffer.readBoolean();
        return message;
    }

    public static void handle(WallClimbMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();
            Optional<Skill> cap = SkillTypeWallClimb.INSTANCE.getSkill(sender);
            if (cap.isPresent()){
                SkillTypeWallClimb.WallClimbSkill skill = (SkillTypeWallClimb.WallClimbSkill) cap.get();
                skill.canClimb = msg.value;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
