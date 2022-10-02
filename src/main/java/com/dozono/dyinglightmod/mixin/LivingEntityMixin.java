package com.dozono.dyinglightmod.mixin;

import com.dozono.dyinglightmod.skill.agility.SkillTypeWallClimb;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "onClimbable", at = @At("HEAD"), cancellable = true)
    private void inject(CallbackInfoReturnable<Boolean> info) {
        Object a = this;
        if (a instanceof PlayerEntity) {
            if (SkillTypeWallClimb.INSTANCE.onHook(((PlayerEntity) a))) {
                info.setReturnValue(true);
            }
        }
    }
}
