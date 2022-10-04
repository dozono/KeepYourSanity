package com.dozono.dyinglightmod.msg;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.agility.SkillTypeAquaMan;
import com.dozono.dyinglightmod.skill.agility.SkillTypeWallClimb;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class AquaManMessage {
    public int value;
    public AquaManMessage() {
    }

    public static void encode(AquaManMessage msg, PacketBuffer buffer) {
        buffer.writeInt(msg.value);
    }

    public static AquaManMessage decode(PacketBuffer buffer) {
        AquaManMessage message = new AquaManMessage();
        message.value = buffer.readInt();
        return message;
    }

    public static void handle(AquaManMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();
            Optional<Skill> cap = SkillTypeAquaMan.INSTANCE.getSkill(sender);
            if (cap.isPresent()){
                SkillTypeAquaMan.AquaManSkill skill = (SkillTypeAquaMan.AquaManSkill) cap.get();
                skill.maxDolphin = msg.value;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
