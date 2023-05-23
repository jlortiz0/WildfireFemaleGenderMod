/*
Wildfire's Female Gender Mod is a female gender mod created for Minecraft.
Copyright (C) 2022 WildfireRomeo

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.wildfire.main;
import com.wildfire.api.IHurtSound;
import com.wildfire.main.networking.PacketSendGenderInfo;
import com.wildfire.main.networking.PacketSync;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

import java.util.UUID;


public class WildfireGenderServer implements ModInitializer {

    public static final SimpleRegistry<IHurtSound> hurtSounds = FabricRegistryBuilder.createSimple(IHurtSound.class, new Identifier(WildfireGender.MODID, "hurt_sound_registry")).buildAndRegister();
    public static final IHurtSound defaultHurtSound = new HurtSound("Disabled", null);
    @Override
    public void onInitialize() {
        Registry.register(hurtSounds, new Identifier(WildfireGender.MODID, "disabled"), defaultHurtSound);
        Identifier id =  new Identifier(WildfireGender.MODID, "male_hurt1");
        Registry.register(hurtSounds, id, new HurtSound("Masculine 1", id));
        id = new Identifier(WildfireGender.MODID, "female_hurt1");
        Registry.register(hurtSounds, id, new HurtSound("Feminine 1", id));

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(WildfireGender.MODID, "send_gender_info"),
        (server, playerEntity, handler, buf, responseSender) -> {
            PacketSendGenderInfo.handle(server, playerEntity, handler, buf, responseSender);
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (!handler.getPlayer().world.isClient()) {
                //Send all other players to the player who joined. Note: We don't send the player to
                // other players as that will happen once the player finishes sending themselves to the server
                ServerPlayerEntity uuid = handler.getPlayer();
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                    PacketSync.sendTo(uuid);
                } );
                thread.setName("WFGM_Sync-" + uuid.getUuid());
                thread.start();
            }
        });
    }
}