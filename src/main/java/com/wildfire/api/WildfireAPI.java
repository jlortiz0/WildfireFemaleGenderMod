package com.wildfire.api;

import com.wildfire.main.GenderPlayer;
import com.wildfire.main.WildfireGender;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WildfireAPI {

    private static Map<Item, IGenderArmor> GENDER_ARMORS = new HashMap<>();

    /**
     * Add custom attributes to the armor you apply this to.
     *
     * @param  item  the item that you are adding {@link IGenderArmor} to.
     * @param  genderArmor the class implementing {@link IGenderArmor} to apply to the item.
     * @see    IGenderArmor
     */
    public static void addGenderArmor(Item item, IGenderArmor genderArmor) {
        GENDER_ARMORS.put(item, genderArmor);
    }

    /**
     * Add custom attributes to the armor you apply this to.
     *
     * @param  uuid  the uuid of the {@link net.minecraft.entity.player.PlayerEntity }.
     * @see    IGenderArmor
     */
    public static GenderPlayer getPlayerById(UUID uuid) {
        return WildfireGender.getPlayerById(uuid);
    }

    /**
     * Get the player's pronouns.
     *
     * @param  uuid  the uuid of the {@link PlayerEntity }.
     * @see    Text
     */
    public static Text getPlayerGender(UUID uuid) {
        return WildfireGender.getPlayerById(uuid).getPronounText();
    }

    /**
     * Load the cached Gender Settings file for the specified {@link UUID }
     *
     * @param  uuid  the uuid of the {@link PlayerEntity }.
     * @param  markForSync true if you want to send the gender settings to the server upon loading.
     */
    public static void loadGenderInfo(UUID uuid, boolean markForSync) {
        WildfireGender.loadGenderInfoAsync(uuid, markForSync);
    }

    /**
     * Get the list of armors supported by Wildfire's Female Gender Mod.
     */
    public static Map<Item, IGenderArmor> getGenderArmors() {
        return GENDER_ARMORS;
    }

    /**
     * Add a new hurt sound to the Hurt Sounds registry
     */
    public static void registerHurtSound(IHurtSound snd, Identifier id) {
        Registry.register(WildfireGender.hurtSounds, id, snd);
    }

}
