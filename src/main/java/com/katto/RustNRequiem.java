package com.katto;

import com.katto.index.RustReqComponents;
import com.katto.index.RustReqItemGroup;
import com.katto.index.RustReqItems;
import com.katto.index.RustReqPackets;
import com.katto.item.CinderVesselItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RustNRequiem implements ModInitializer {
	public static final String MOD_ID = "rust-req";


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModelPredicateProviderRegistry.register(
				RustReqItems.CINDER_VESSEL,
				Identifier.of("has_item"),
				(stack, world, entity, seed) -> {
					var comp = stack.get(RustReqComponents.STORED_ITEM);
					return (comp != null && !comp.stack().isEmpty()) ? 1f : 0f;
				}
		);

		ServerLivingEntityEvents.AFTER_DEATH.register((victim, source) -> {
			if (!(source.getAttacker() instanceof PlayerEntity player)) return;

			ItemStack offhand = player.getOffHandStack();
			if (offhand.getItem() instanceof CinderVesselItem vessel) {
				vessel.killEntity(victim.getWorld(), offhand, victim, player);
			}
		});


		RustReqItems.init();
		RustReqComponents.init();
		RustReqItemGroup.init();
		RustReqPackets.registerServer();


		LOGGER.info("Rust N' Requiem is initializing.");
	}
	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}