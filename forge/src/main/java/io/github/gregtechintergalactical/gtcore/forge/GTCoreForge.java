package io.github.gregtechintergalactical.gtcore.forge;

import com.terraformersmc.terraform.boat.api.client.TerraformBoatClientHelper;
import io.github.gregtechintergalactical.gtcore.blockentity.BlockEntityMassStorage;
import io.github.gregtechintergalactical.gtcore.proxy.ClientHandler;
import io.github.gregtechintergalactical.gtcore.GTCore;
import io.github.gregtechintergalactical.gtcore.tree.RubberFoliagePlacer;
import muramasa.antimatter.event.forge.AntimatterCraftingEvent;
import muramasa.antimatter.event.forge.AntimatterLoaderEvent;
import muramasa.antimatter.event.forge.AntimatterProvidersEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(GTCore.ID)
public class GTCoreForge extends GTCore {
    public GTCoreForge(){
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onProvidersEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCraftingEvent);
        MinecraftForge.EVENT_BUS.<AntimatterLoaderEvent>addListener(GTCoreForge::registerRecipeLoaders);
        MinecraftForge.EVENT_BUS.addListener(this::onChunkWatch);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(FoliagePlacerType.class, this::onRegistration);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
            TerraformBoatClientHelper.registerModelLayer(new ResourceLocation(GTCore.ID, "rubber"));
        });
    }

    private void onChunkWatch(ChunkWatchEvent.Watch event){
        event.getWorld().getChunk(event.getPos().x, event.getPos().z).getBlockEntities().values().forEach(b -> {
            if (b instanceof BlockEntityMassStorage storage && storage.isServerSide() && !storage.isRemoved()){
                storage.setSyncSlots(true);
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    private void clientSetup(FMLClientSetupEvent event){
        ClientHandler.init();
    }

    private void onProvidersEvent(AntimatterProvidersEvent event){
        onProviders(event.event);
    }

    private void onCraftingEvent(AntimatterCraftingEvent event){
        onCrafting(event.getEvent());
    }

    public static void registerRecipeLoaders(AntimatterLoaderEvent event) {
        GTCore.registerRecipeLoaders(event.sender, event.registrat);
    }

    private void onRegistration(final RegistryEvent.Register<FoliagePlacerType<?>> e){
        e.getRegistry().register(RubberFoliagePlacer.RUBBER.setRegistryName(GTCore.ID, "rubber_foilage_placer"));
    }
}
