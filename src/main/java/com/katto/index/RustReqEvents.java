package com.katto.index;

import com.katto.item.CinderVesselItem;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public class RustReqEvents {
    public static void init() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ItemStack offhand = player.getOffHandStack();

            // only run if off-hand has CinderVessel AND entity is a LivingEntity
            if (offhand.getItem() instanceof CinderVesselItem vessel && entity instanceof LivingEntity victim) {
                vessel.killEntity(world, offhand, victim, player);
            }

            return ActionResult.PASS;
        });
    }
}