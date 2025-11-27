package com.katto.index;

import com.katto.RustNRequiem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

public interface RustReqItemGroup {

    // Registry key for your item group
    RegistryKey<ItemGroup> GROUP_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, RustNRequiem.id("main"));

    // Build the group itself
    ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(RustReqItems.CINDER_VESSEL)) // icon for tab
            .displayName(Text.translatable("itemGroup.rust-req").styled(style -> style.withColor(0x6a5a56))) // tab name + color
            .build();

    static void init() {
        // Register the group
        Registry.register(Registries.ITEM_GROUP, GROUP_KEY, ITEM_GROUP);

        // Add items to the creative tab
        ItemGroupEvents.modifyEntriesEvent(GROUP_KEY).register(RustReqItemGroup::addEntries);
    }

    private static void addEntries(FabricItemGroupEntries entries) {
        // Add all your mod items here
        entries.add(RustReqItems.CINDER_VESSEL);
        entries.add(RustReqItems.CINDER_CRYSTAL);
        entries.add(RustReqItems.SCYTHE);
    }
}

