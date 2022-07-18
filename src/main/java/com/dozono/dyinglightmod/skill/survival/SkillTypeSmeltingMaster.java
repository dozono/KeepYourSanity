package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeSmeltingMaster extends SkillType {

    public static final SkillTypeSmeltingMaster INSTANCE = new SkillTypeSmeltingMaster();

    public SkillTypeSmeltingMaster() {
        super(Builder.create().addParent(SkillTypeToolMaster.Instance));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void tick(TickEvent.PlayerTickEvent event) {
        if (event.phase== TickEvent.Phase.START) return;
        PlayerEntity player = event.player;
        if (player.level.isClientSide) return;
        AxisAlignedBB bb = player.getBoundingBox().inflate(5, 2, 5);
        player.getCapability(CapabilitySkillContainer).ifPresent(c -> {
            Optional<Skill> skill = c.getSkill(this);
            if (skill.isPresent()) {
                List<Chunk> chunks = new ArrayList<>(4);
                int minX = ((int) bb.minX) >> 4;
                int maxZ = ((int) bb.maxZ) >> 4;
                Chunk topLeft = player.level.getChunk(minX, maxZ);
                chunks.add(topLeft);
                int maxX = ((int) bb.maxX >> 4);
                int minZ = (int) bb.minZ >> 4;
                boolean xDiff = maxX != topLeft.getPos().x;
                boolean zDiff = minZ != topLeft.getPos().z;
                if (xDiff) {
                    chunks.add(player.level.getChunk(maxX, maxZ));
                }
                if (zDiff) {
                    chunks.add(player.level.getChunk(minX, minZ));
                }
                if (xDiff && zDiff) {
                    chunks.add(player.level.getChunk(maxX, minZ));
                }
                for (Chunk chunk : chunks) {
                    for (Map.Entry<BlockPos, TileEntity> entry : chunk.getBlockEntities().entrySet()) {
                        BlockPos pos = entry.getKey();
                        TileEntity entity = entry.getValue();
                        if (entity instanceof AbstractFurnaceTileEntity) {
                            double distSqr = pos.distSqr(player.blockPosition());
                            if (distSqr < 50) {
                                AbstractFurnaceTileEntity furnaceTileEntity = (AbstractFurnaceTileEntity) entity;
                                if (player.getRandom().nextInt(1) == 0) {
                                    furnaceTileEntity.tick();
                                }
                            }
                        }
                    }
                }
            }
        });
    }

}
