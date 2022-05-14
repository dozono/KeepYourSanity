package com.dozono.dyinglightmod;

import com.dozono.dyinglightmod.monster.entities.EnhancedZombieEntity;
import com.dozono.dyinglightmod.monster.items.CustomSpawnEggItem;
import com.dozono.dyinglightmod.monster.model.EnhancedZombieModel;
import com.dozono.dyinglightmod.monster.renderer.EnhancedZombieRenderer;
import com.dozono.dyinglightmod.skill.SkillContainer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DyingLight.MODID)
public class DyingLight {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "dying_light";

    private static final DeferredRegister<EntityType<?>> ENTITY_REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    private static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<EntityType<EnhancedZombieEntity>> ENHANCED_ZOMBIE = ENTITY_REGISTER.register("enhanced_zombie",
            () -> EntityType.Builder.<EnhancedZombieEntity>of(EnhancedZombieEntity::new, EntityClassification.MONSTER)
                    .sized(1f, 3f)
                    .setTrackingRange(15)
                    .build("enhanced_zombie"));

    public static final RegistryObject<Item> ENHANCED_ZOMBIE_EGG = ITEM_REGISTER.register("enhanced_zombie_egg",
            () -> new CustomSpawnEggItem(DyingLight.ENHANCED_ZOMBIE, 1, 2, new Item.Properties().tab(ItemGroup.TAB_MISC)));


    @CapabilityInject(SkillContainer.class)
    public static Capability<SkillContainer> CapabilitySkillContainer = null;

    @SubscribeEvent
    public void onCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(DyingLight.MODID, "skill"), new ICapabilitySerializable<CompoundNBT>() {
                SkillContainer container = new SkillContainer(((PlayerEntity) event.getObject()));

                @Override
                public CompoundNBT serializeNBT() {
                    return new CompoundNBT();
                }

                @Override
                public void deserializeNBT(CompoundNBT nbt) {
                }

                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                    if (cap == CapabilitySkillContainer) {
                        return (LazyOptional<T>) LazyOptional.of(() -> container);
                    }
                    return LazyOptional.empty();
                }
            });
        }
    }

    public DyingLight() {

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ITEM_REGISTER.register(bus);
        ENTITY_REGISTER.register(bus);


        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        DeferredWorkQueue.runLater(() ->
        {
            GlobalEntityTypeAttributes.put(DyingLight.ENHANCED_ZOMBIE.get(), EnhancedZombieEntity.func_234342_eQ_().build());
        });
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(DyingLight.ENHANCED_ZOMBIE.get(), new IRenderFactory<EnhancedZombieEntity>() {
            @Override
            public EntityRenderer<? super EnhancedZombieEntity> createRenderFor(EntityRendererManager manager) {
                return new EnhancedZombieRenderer(manager, new EnhancedZombieModel<>(), 0.8f);
            }
        });
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("dyinglightmod", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
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
}
