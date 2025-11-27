package com.katto.index;

import com.katto.RustNRequiem;
import com.katto.item.CinderVesselItem;
import com.katto.item.CrystalItem;
import com.katto.item.ScytheItem;
import net.acoyt.acornlib.api.item.AcornItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public interface RustReqItems {

    Item SCYTHE = register("scythe",
            new ScytheItem(ToolMaterials.NETHERITE, new AcornItemSettings())
    );

    Item CINDER_VESSEL =register("cinder_vessel", new CinderVesselItem(new AcornItemSettings().rarity(Rarity.UNCOMMON)));

    Item CINDER_CRYSTAL =register("crystal", new CrystalItem(new AcornItemSettings().rarity(Rarity.UNCOMMON)));

    private static Item register(String id, Item item) {
        return Registry.register(
                Registries.ITEM,
                Identifier.of(RustNRequiem.MOD_ID, id),
                item
        );
    }

    static void init() {
        // interface init, do nothing lol
    }
}