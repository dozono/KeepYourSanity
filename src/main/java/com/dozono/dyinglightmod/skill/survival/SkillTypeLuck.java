package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillTypeLuck extends SkillType {
    public static final SkillTypeLuck INSTANCE = new SkillTypeLuck();

    private SkillTypeLuck() {
        super(Builder.create().addParent(SkillTypeMandom.INSTANCE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGettingExp(PlayerXpEvent.PickupXp event) {
        PlayerEntity player = event.getPlayer();
        if (player.level.isClientSide) return;
        Optional<Skill> skill = this.getSkill(player);
        if (skill.isPresent() && skill.get().getLevel() > 0) {
            Skill luck = skill.get();
            int value = event.getOrb().value;
            float modifier = 1F + luck.getLevel() / 5F;
            event.getOrb().value = Math.round(value * modifier);
        }
    }

    @Override
    public TextComponent getDescription(Skill skill) {
        return getCommonDescriptionContent(skill, "20%", "40%", "60%");
//        TextComponent result = new StringTextComponent("");
//        TextComponent[] children = {
//                new StringTextComponent("10%"),
//                new StringTextComponent("/"),
//                new StringTextComponent("20%"),
//                new StringTextComponent("/"),
//                new StringTextComponent("30%")
//        };
//        for (TextComponent c : children) {
//            result.append(c);
//        }
//        result.setStyle(Style.EMPTY.withBold(true));
//        if (skill.getLevel() == 1) {
//            children[0].setStyle(Style.EMPTY.withColor(Color.fromLegacyFormat(TextFormatting.BLUE)));
//        } else if (skill.getLevel() == 2) {
//            children[2].setStyle(Style.EMPTY.withColor(Color.fromLegacyFormat(TextFormatting.BLUE)));
//        } else if (skill.getLevel() == 3) {
//            children[4].setStyle(Style.EMPTY.withColor(Color.fromLegacyFormat(TextFormatting.BLUE)));
//        }
//
//        return new TranslationTextComponent(getTranslationKey(), result);
    }


}
