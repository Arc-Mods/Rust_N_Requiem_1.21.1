package com.katto.index;

import com.katto.RustNRequiem;
import com.katto.component.StoredItemComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RustReqComponents {
    public static final ComponentType<StoredItemComponent> STORED_ITEM =
            Registry.register(
                    Registries.DATA_COMPONENT_TYPE,
                    Identifier.of(RustNRequiem.MOD_ID, "stored_item"),
                    ComponentType.<StoredItemComponent>builder()
                            .codec(ItemStack.CODEC.xmap(StoredItemComponent::new, StoredItemComponent::stack))
                            .build()
            );

    public static void init() {}
}
