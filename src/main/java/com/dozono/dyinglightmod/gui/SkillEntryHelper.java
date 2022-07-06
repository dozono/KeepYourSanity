package com.dozono.dyinglightmod.gui;

import com.dozono.dyinglightmod.skill.SkillType;
import com.dozono.dyinglightmod.skill.agility.SkillTypeAquaMan;
import com.dozono.dyinglightmod.skill.agility.SkillTypeDoubleJump;
import com.dozono.dyinglightmod.skill.agility.SkillTypeRunner;
import com.dozono.dyinglightmod.skill.agility.SkillTypeSwimmer;
import com.dozono.dyinglightmod.skill.combat.SkillTypeMarksmanship;
import com.dozono.dyinglightmod.skill.survival.*;
import net.minecraft.client.Minecraft;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dozono.dyinglightmod.gui.SkillEntryGui.ICON_DIMENSION;
import static com.dozono.dyinglightmod.gui.SkillEntryGui.ICON_TEXTURE_DIMENSION;

/**
 * The hardcode class for the sprite uv and xy
 */
public class SkillEntryHelper {
    public static SkillEntryGui createSkillGuiTree(Minecraft mc, SkillTabGui tab, SkillType skillType, Map<SkillType, SkillEntryGui> registry) {
        List<SkillEntryGui> children = skillType.getChildren().size() == 0
                ? Collections.emptyList()
                : skillType.getChildren().stream().map((d) -> createSkillGuiTree(mc, tab, d, registry)).collect(Collectors.toList());

        int[] iconPosition = SkillEntryHelper.getIconPosition(skillType);
        int[] iconUV = SkillEntryHelper.getIconUV(skillType);

        SkillEntryGui gui = new SkillEntryGui(mc, tab, skillType, iconPosition[0], iconPosition[1], iconUV[0], iconUV[1], children);
        registry.put(skillType, gui);
        return gui;
    }


    public static int[] getIconPosition(SkillType type) {
        HashMap<SkillType, int[]> map = new HashMap<>();

        int unit = 24;
        int base = 64;

//        agility
        map.put(SkillTypeAquaMan.INSTANCE, new int[]{0, 0});
        map.put(SkillTypeDoubleJump.INSTANCE, new int[]{0, 0});
        map.put(SkillTypeRunner.INSTANCE, new int[]{0, 0});
        map.put(SkillTypeSwimmer.INSTANCE, new int[]{0, 0});

        //survival
        map.put(SkillTypeMandom.INSTANCE, new int[]{0, 0});

        map.put(SkillTypeTireless.INSTANCE, new int[]{0, -unit});
        map.put(SkillTypeMiner.Instance, new int[]{unit, -unit * 2});
        map.put(SkillTypeLumberman.Instance, new int[]{-unit, -unit * 2});

        map.put(SkillTypeGastrosoph.INSTANCE, new int[]{-unit, 0});
        map.put(SkillTypePotionMaster.INSTANCE, new int[]{-unit * 2, 0});

        map.put(SkillTypeMender.INSTANCE, new int[]{0, unit});
        map.put(SkillTypeSmeltingMaster.INSTANCE, new int[]{unit, unit * 2});
        map.put(SkillTypeToolMaster.Instance, new int[]{-unit, unit * 2});

        map.put(SkillTypeLuck.INSTANCE, new int[]{unit, 0});
        map.put(SkillTypeRegen.INSTANCE, new int[]{unit * 2, 0});

        if (map.containsKey(type)) {
            int[] ints = map.get(type);
            return new int[]{ints[0] + base, ints[1] + base};
        }

        return new int[]{base, base};
    }

    private static int getIconIndex(SkillType skillType) {
        HashMap<SkillType, Integer> map = new HashMap<>();

//        map.put(SkillTypeMiner.Instance, 1);
        map.put(SkillTypeDoubleJump.INSTANCE, 3);


        map.put(SkillTypeMandom.INSTANCE, 0);
        map.put(SkillTypeLuck.INSTANCE, 1);
        map.put(SkillTypeTireless.INSTANCE, 2);
        map.put(SkillTypeToolMaster.Instance, 3);
        map.put(SkillTypeMender.INSTANCE, 4);
        map.put(SkillTypeGastrosoph.INSTANCE, 5);
        map.put(SkillTypeSmeltingMaster.INSTANCE, 6);
        map.put(SkillTypeMiner.Instance, 7);
        map.put(SkillTypeLumberman.Instance, 8);
        map.put(SkillTypePotionMaster.INSTANCE, 9);
        map.put(SkillTypeRegen.INSTANCE, 10);

//        map.put(SkillTypeMandom.INSTANCE, 0);
//        map.put(SkillTypeMandom.INSTANCE, 0);
//        map.put(SkillTypeMandom.INSTANCE, 0);
//        map.put(SkillTypeMandom.INSTANCE, 0);
//        map.put(SkillTypeMandom.INSTANCE, 0);
//        map.put(SkillTypeMandom.INSTANCE, 0);

        Integer integer = map.get(skillType);
        if (integer == null) {
            return 0;
        }
        return integer;
    }

    public static int[] getIconUV(SkillType skillType) {
        int index = SkillEntryHelper.getIconIndex(skillType);
        int uOffset = index * ICON_DIMENSION;
        int u = 0;
        int v = 0;
        if (uOffset + ICON_DIMENSION >= ICON_TEXTURE_DIMENSION) {
            int vIndex = (uOffset + ICON_DIMENSION) / ICON_TEXTURE_DIMENSION;
            v = vIndex * ICON_DIMENSION;
            u = (uOffset + ICON_DIMENSION) % ICON_TEXTURE_DIMENSION;
        } else {
            u = uOffset;
        }
        System.out.println(skillType.getRegistryName());
        System.out.println(u + " " + v);
        return new int[]{u, v};
    }
}
