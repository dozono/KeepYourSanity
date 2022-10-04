package com.dozono.dyinglightmod.msg;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.skill.agility.SkillTypeDoubleJump;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SkillStatusMessage {
    public CompoundNBT nbt;

    public SkillStatusMessage() {
    }

    public static void encode(SkillStatusMessage msg, PacketBuffer buffer) {
        buffer.writeNbt(msg.nbt);
    }

    public static SkillStatusMessage decode(PacketBuffer buffer) {
        SkillStatusMessage skillStatusMessage = new SkillStatusMessage();
        skillStatusMessage.nbt = buffer.readNbt();
        return skillStatusMessage;
    }

    public static void handle(SkillStatusMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(msg.nbt));
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(CompoundNBT nbt) {
        Minecraft.getInstance().player.getCapability(DyingLight.CapabilitySkillContainer).ifPresent(c -> c.deserializeNBT(nbt));
    }
}
