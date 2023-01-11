package com.dozono.dyinglightmod.msg;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.survival.SkillTypeTireless;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class SprintMessage {
    public boolean value = false;

    public SprintMessage() {
    }

    public static void encode(SprintMessage msg, PacketBuffer buffer) {
        buffer.writeBoolean(msg.value);
    }

    public static SprintMessage decode(PacketBuffer buffer) {
        SprintMessage message = new SprintMessage();
        message.value = buffer.readBoolean();
        return message;
    }

    public static void handle(SprintMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();
            Optional<Skill> cap = SkillTypeTireless.INSTANCE.getSkill(sender);
            if (cap.isPresent()) {
                SkillTypeTireless.SkillTireless skill = (SkillTypeTireless.SkillTireless) cap.get();
                skill.toggleSprintKey = msg.value;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
