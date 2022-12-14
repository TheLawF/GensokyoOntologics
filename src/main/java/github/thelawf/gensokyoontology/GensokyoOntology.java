package github.thelawf.gensokyoontology;

import github.thelawf.gensokyoontology.common.CommonSetUp;
import github.thelawf.gensokyoontology.common.data.GSKORecipeHandler;
import github.thelawf.gensokyoontology.common.dimensions.GSKODimensions;
import github.thelawf.gensokyoontology.common.dimensions.world.biome.GSKOBiomesProvider;
import github.thelawf.gensokyoontology.common.particle.GSKOParticleRegistry;
import github.thelawf.gensokyoontology.core.init.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(GensokyoOntology.MODID)
public class GensokyoOntology {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "gensokyoontology";

    public GensokyoOntology() {
        // final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // ClientOnlyRegistry cog = new ClientOnlyRegistry(modEventBus);
        // Register ourselves for server and other game events we are interested in

        IEventBus modEvent = FMLJavaModLoadingContext.get().getModEventBus();

        modEvent.addListener(CommonSetUp::init);

        MinecraftForge.EVENT_BUS.register(this);
        ItemRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        FluidRegistry.FLUIDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BlockRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        GSKOParticleRegistry.PARTICLE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        EffectRegistry.POTION_EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TileEntityTypeRegistry.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        EntityRegistry.GSKO_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class DataGenEvent {
        @SubscribeEvent
        public static void dataGen(GatherDataEvent event) {
            event.getGenerator().addProvider(new GSKORecipeHandler(event.getGenerator()));
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
    public static class RenderTypeRegistry {
        @SubscribeEvent
        public static void onRenderTypeSetUp(FMLClientSetupEvent event){
            event.enqueueWork(() -> {
                RenderTypeLookup.setRenderLayer(FluidRegistry.HOT_SPRING_SOURCE.get(),
                        RenderType.getTranslucent());
                RenderTypeLookup.setRenderLayer(FluidRegistry.HOT_SPRING_FLOWING.get(),
                        RenderType.getTranslucent());

                RenderTypeLookup.setRenderLayer(BlockRegistry.LYCORIS_RADIATA.get(),
                        RenderType.getCutout());
                RenderTypeLookup.setRenderLayer(BlockRegistry.ONION_CROP_BLOCK.get(),
                        RenderType.getCutout());
                RenderTypeLookup.setRenderLayer(BlockRegistry.SPACE_FISSURE_BLOCK.get(),
                        RenderType.getCutout());
            });

        }
    }

    @Mod.EventBusSubscriber(modid = GensokyoOntology.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class WorldGenRegistryEvents{
        @SubscribeEvent
        public static void registerBiomes(RegistryEvent.Register<Biome> event) {
            // GSKODimensions.register();
        }
    }

}
