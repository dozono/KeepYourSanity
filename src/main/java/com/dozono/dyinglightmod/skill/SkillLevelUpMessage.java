package com.dozono.dyinglightmod.skill;

import com.dozono.dyinglightmod.DyingLight;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SkillLevelUpMessage {
    public final ResourceLocation skillRegistryName;

    public SkillLevelUpMessage(ResourceLocation skillRegistry) {
        this.skillRegistryName = skillRegistry;
    }

    public static void encode(SkillLevelUpMessage msg, PacketBuffer buffer) {
        buffer.writeResourceLocation(msg.skillRegistryName);
    }

    public static SkillLevelUpMessage decode(PacketBuffer buffer) {
        return new SkillLevelUpMessage(buffer.readResourceLocation());
    }

    public static void handle(SkillLevelUpMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();
            SkillType skillType = DyingLight.SKILL_REGISTRY.get().getValue(msg.skillRegistryName);
            assert sender != null;
            sender.getCapability(DyingLight.CapabilitySkillContainer).ifPresent(c -> {
                Skill skill = c.getSkill(skillType).orElseThrow(() -> new RuntimeException());
                skill.levelUp();
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
