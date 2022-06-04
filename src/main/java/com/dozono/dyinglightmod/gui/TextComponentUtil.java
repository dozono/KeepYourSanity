package com.dozono.dyinglightmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.CharacterManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.Style;

import java.util.List;

public class TextComponentUtil {
    private static final int[] TEST_SPLIT_OFFSETS = new int[]{0, 10, -10, 25, -25};

    public static float getMaxWidth(CharacterManager characterManager, List<ITextProperties> properties) {
        return (float) properties.stream().mapToDouble(characterManager::stringWidth).max().orElse(0.0D);
    }

    /**
     * Split the text component into multiple line according to line width
     */
    public static List<ITextProperties> splitOptimalLines(ITextComponent iTextComponent, int lineWidth) {
        CharacterManager manager = Minecraft.getInstance().font.getSplitter();
        List<ITextProperties> result = null;
        float lastWidthDiff = Float.MAX_VALUE;

        for (int i : TEST_SPLIT_OFFSETS) {
            List<ITextProperties> lines = manager.splitLines(iTextComponent, lineWidth - i, Style.EMPTY);
            float widthDiff = Math.abs(getMaxWidth(manager, lines) - (float) lineWidth);
            if (widthDiff <= 10.0F) {
                return lines;
            }

            if (widthDiff < lastWidthDiff) {
                lastWidthDiff = widthDiff;
                result = lines;
            }
        }

        return result;
    }
}
