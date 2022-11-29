package com.dozono.dyinglightmod.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    private static final DataParameter<Integer> COOL_DOWN = EntityDataManager.defineId(PlayerEntity.class, DataSerializers.INT);

    @Inject(method = "defineSynchedData", at = @At("TAIL"), cancellable = true)
    protected void inject(CallbackInfo info) {
        PlayerEntity e = ((PlayerEntity) (Object) this);
        System.out.println("WTF");
        e.getEntityData().define(COOL_DOWN, 0);
    }
}
