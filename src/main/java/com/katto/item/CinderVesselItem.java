package com.katto.item;

import com.katto.component.StoredItemComponent;
import com.katto.index.RustReqComponents;
import com.katto.index.RustReqItems;
import net.acoyt.acornlib.api.item.KillEffectItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CinderVesselItem extends Item implements KillEffectItem {
        public CinderVesselItem(Settings settings) {
            super(settings.component(RustReqComponents.STORED_ITEM, new StoredItemComponent(ItemStack.EMPTY)));
        }

    @Override
    public void killEntity(World world, ItemStack stack, LivingEntity victim, LivingEntity killer) {
        if (world.isClient()) return; // server only

        if (!(killer instanceof PlayerEntity player)) return;

        // Must be in off-hand
        ItemStack offhand = player.getOffHandStack();
        if (offhand.getItem() != this) return; // only trigger if this item is in off-hand

        ItemStack stored = getStoredItem(stack);
        if (stored.isEmpty()) return; // nothing stored → do nothing

        // Explosion effect (no block damage)
        world.createExplosion(
                player,
                victim.getX(),
                victim.getY(),
                victim.getZ(),
                40.0f,
                World.ExplosionSourceType.NONE
        );

        // Calculate direction from player to victim
        double dx = victim.getX() - player.getX();
        double dz = victim.getZ() - player.getZ();
        double distance = Math.sqrt(dx*dx + dz*dz);

        if (distance != 0) {
            dx /= distance; // normalize
            dz /= distance;
        }

        // Velocity multipliers
        double horizontalPower = 2.5;
        double verticalPower = 1.2;

        // Launch victim away from player
        victim.setVelocity(dx * horizontalPower, verticalPower, dz * horizontalPower);
        victim.velocityModified = true;

        // Launch player (opposite direction)
        player.setVelocity(-dx * horizontalPower * 40, verticalPower * 35, -dz * horizontalPower * 40);
        player.velocityModified = true;

        // Clear the stored item
        setStoredItem(stack, ItemStack.EMPTY);
    }



    public static ItemStack getStoredItem(ItemStack vessel) {
        StoredItemComponent comp = vessel.get(RustReqComponents.STORED_ITEM);
        return comp == null ? ItemStack.EMPTY : comp.stack();
    }

    public static void setStoredItem(ItemStack vessel, ItemStack stored) {
        vessel.set(RustReqComponents.STORED_ITEM, new StoredItemComponent(stored));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack vessel = user.getStackInHand(hand);
        ItemStack stored = getStoredItem(vessel);

        if (!stored.isEmpty()) {
            if (!world.isClient())
                user.sendMessage(Text.literal("The vessel is already charged."), true);
            return TypedActionResult.fail(vessel);
        }

        ItemStack offhand = user.getOffHandStack();

        // Only allow RustReqItems.CINDER_CRYSTAL
        if (!offhand.isEmpty() && offhand.getItem() == RustReqItems.CINDER_CRYSTAL) {
            setStoredItem(vessel, offhand.copy());
            offhand.decrement(1);

            if (!world.isClient())
                user.sendMessage(Text.literal("Charged the Vessel."), true);
        } else {
            if (!world.isClient() && !offhand.isEmpty())
                user.sendMessage(Text.literal("Only a Volatile Crystal can be stored."), true);
        }

        return TypedActionResult.success(vessel);
    }



    public boolean onClicked(ItemStack vessel, ItemStack clicked, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) return false;

        ItemStack stored = getStoredItem(vessel);

        // If vessel is empty → try to store clicked stack
        if (stored.isEmpty()) {
            if (!clicked.isEmpty() && clicked.getItem() == RustReqItems.CINDER_CRYSTAL) {
                setStoredItem(vessel, clicked.copy());
                clicked.decrement(1);
                return true;
            }
        }
        // Vessel has item → extract on right-click
        else {
            if (clicked.isEmpty()) {
                slot.setStack(stored.copy());
                setStoredItem(vessel, ItemStack.EMPTY);
                return true;
            }
        }

        return false;
    }


    @Override
    public boolean onStackClicked(ItemStack vessel, Slot slot, ClickType clickType, PlayerEntity player) {
        // same rule: only right click interacts
        if (clickType != ClickType.RIGHT) return false;

        return this.onClicked(vessel, slot.getStack(), slot, clickType, player);
    }
}