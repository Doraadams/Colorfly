package net.local.color.util;

import net.local.color.Colorfly;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

@SuppressWarnings("SameParameterValue")
public class ModTags {
    public static final TagKey<Block> COLORFLY_SPAWNABLE_ON = createTag("colorfly_spawnable_on");
    private static TagKey<Block> createTag(String name) {
        return TagKey.of(RegistryKeys.BLOCK, new Identifier(Colorfly.MOD_ID, name));
    }
    private static TagKey<Block> createCommonTag(String name) {
        return TagKey.of(RegistryKeys.BLOCK, new Identifier("c", name));
    }
}
