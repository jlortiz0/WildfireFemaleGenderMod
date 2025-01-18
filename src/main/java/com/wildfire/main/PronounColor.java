package com.wildfire.main;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Formatting;
import net.minecraft.util.function.ValueLists;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

public enum PronounColor {
    WHITE(Formatting.WHITE),
    DARK_BLUE(Formatting.DARK_BLUE),
    DARK_GREEN(Formatting.DARK_GREEN),
    DARK_AQUA(Formatting.DARK_AQUA),
    DARK_RED(Formatting.DARK_RED),
    DARK_PURPLE(Formatting.DARK_PURPLE),
    GOLD(Formatting.GOLD),
    GRAY(Formatting.GRAY),
    DARK_GRAY(Formatting.DARK_GRAY),
    BLUE(Formatting.BLUE),
    GREEN(Formatting.GREEN),
    AQUA(Formatting.AQUA),
    RED(Formatting.RED),
    LIGHT_PURPLE(Formatting.LIGHT_PURPLE),
    YELLOW(Formatting.YELLOW),

    TRANS("Transgender", 0x6ac2e4, 0xeb92ea, 0xffffff, 0xeb92ea, 0x6ac2e4),
    NB("Nonbinary", 0xebf367, 0xffffff, 0x7210bc, 0x333233),
    AGENDER("Agender", 0x222222, 0xbcc4c7, 0xffffff, 0xb7f684, 0xffffff, 0xbcc4c7, 0x222222),
    QUEER("Genderqueer", 0xca78ef, 0xffffff, 0x2db418),
    FLUID("Genderfluid", 0xfbacf9, 0xffffff, 0x9c2bd0, 0x333233, 0x2f4dd8),
    DEMIBOY("Demiboy", 0x7f7f7f, 0x9a9fa1, 0xa9ffff, 0xffffff, 0xa9ffff, 0x9a9fa1, 0x7f7f7f),
    DEMIGIRL("Demigirl", 0x7f7f7f, 0x9a9fa1, 0xfcb1ff, 0xffffff, 0xfcb1ff, 0x9a9fa1, 0x7f7f7f),
    BI("Bigender", 0xc479a2, 0xeda5cd, 0xd6c7e8, 0xffffff, 0xd6c7e8, 0x9ac7e8, 0x6d82d1),
    INTERSEX("Intersex", 0xd8abcf, 0xffffff, 0xa0cdee, 0xf0b7d6, 0xffffff, 0xd8abcf),
    PRIDE(null, 0xe40303, 0xff8c00, 0xffed00, 0x008026, 0x24408e, 0x732982),
    FAE("Genderfae", 0x97c3a5, 0xc3deae, 0xf9facd, 0xffffff, 0xfca2c4, 0xdb8ae4, 0xa97edd),
    FAWN("Genderfawn", 0xfcc689, 0xfff09d, 0xfbf9cc, 0xffffff, 0x8edfd8, 0x8dabdc, 0x9781eb),
    FLUX("Genderflux", 0xf57694, 0xf2a3b9, 0xcfcfcf, 0x7be1f5, 0x3ecdfa, 0xfff48c),
    PAN("Pangender", 0xfff798, 0xffddcd, 0xffebfb, 0xffffff, 0xffebfb, 0xffddcd, 0xfff798);

    public final int[] colors;
    public final @Nullable String name;

    PronounColor(Formatting f) {
        this(null, f.getColorValue());
    }

    PronounColor(@Nullable String name, int... colors) {
        this.name = name;
        this.colors = colors;
    }

    public static final IntFunction<PronounColor> BY_ID = ValueLists.createIdToValueFunction(PronounColor::ordinal, values(), ValueLists.OutOfBoundsHandling.ZERO);
    public static final PacketCodec<ByteBuf, PronounColor> CODEC = PacketCodecs.indexed(BY_ID, PronounColor::ordinal);
}
