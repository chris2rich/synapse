package com.ragemachine.moon.impl.client.modules.misc;

import com.google.common.collect.Sets;
import com.ragemachine.moon.api.event.events.PacketEvent;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

public class NoSoundLag extends Module {

    private final Value<Boolean> crystals = addSetting(new Value("Crystals", true));
    private final Value<Boolean> armor = addSetting(new Value("Armor", true));

    private static final Set<SoundEvent> BLACKLIST = Sets.newHashSet(
        SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
        SoundEvents.ITEM_ARMOR_EQIIP_ELYTRA,
        SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
        SoundEvents.ITEM_ARMOR_EQUIP_IRON,
        SoundEvents.ITEM_ARMOR_EQUIP_GOLD,
        SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
        SoundEvents.ITEM_ARMOR_EQUIP_LEATHER
    );

    public NoSoundLag() {
        super("NoSoundLag", "Prevents Lag through sound spam.", Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive event) {
        if(event.getPacket() != null && mc.player != null && mc.world != null) {
            if (event.getPacket() instanceof SPacketSoundEffect) {
                SPacketSoundEffect packet = event.getPacket();
                if (crystals.getValue() && packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    for (Entity entity : mc.world.loadedEntityList) {
                        if (entity instanceof EntityEnderCrystal && entity.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0f) {
                            entity.setDead();
                        }
                    }
                }

                if (BLACKLIST.contains(packet.getSound()) && armor.getValue()) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
