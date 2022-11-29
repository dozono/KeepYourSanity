package com.dozono.dyinglightmod;

import com.dozono.dyinglightmod.gui.SkillScreen;
import com.dozono.dyinglightmod.monster.entities.EnhancedZombieEntity;
import com.dozono.dyinglightmod.monster.items.CustomSpawnEggItem;
import com.dozono.dyinglightmod.monster.model.EnhancedZombieModel;
import com.dozono.dyinglightmod.monster.renderer.EnhancedZombieRenderer;
import com.dozono.dyinglightmod.msg.*;
import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillContainer;
import com.dozono.dyinglightmod.skill.SkillType;
import com.dozono.dyinglightmod.skill.agility.*;
import com.dozono.dyinglightmod.skill.combat.*;
import com.dozono.dyinglightmod.skill.survival.*;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.KeyEvent;
import java.util.function.Supplier;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DyingLight.MODID)
public class DyingLight {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "dying_light";
    private static final String Version = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "network_channel"),
            () -> Version,
            Version::equals,
            Version::equals);

    private static KeyBinding skillKey;

    public static final DeferredRegister<SkillType> SKILL_REGISTER = DeferredRegister.create(SkillType.class, MODID);
    public static final Supplier<IForgeRegistry<SkillType>> SKILL_REGISTRY = SKILL_REGISTER
            .makeRegistry("skill_registry", RegistryBuilder::new);

    private static final DeferredRegister<EntityType<?>> ENTITY_REGISTER = DeferredRegister
            .create(ForgeRegistries.ENTITIES, MODID);
    private static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<Effect> EFFECT_REGISTER = DeferredRegister.create(ForgeRegistries.POTIONS,
            MODID);

    public static RegistryObject<Effect> UNDYING_EFFECT = DyingLight.EFFECT_REGISTER.register("dying",
            () -> new SkillTypeDeathDenied.UndyingEffect(EffectType.NEUTRAL, 2400));

    public static final RegistryObject<EntityType<EnhancedZombieEntity>> ENHANCED_ZOMBIE = ENTITY_REGISTER.register(
            "enhanced_zombie",
            () -> EntityType.Builder.<EnhancedZombieEntity>of(EnhancedZombieEntity::new, EntityClassification.MONSTER)
                    .sized(1f, 3f)
                    .setTrackingRange(15)
                    .build("enhanced_zombie"));

    public static final RegistryObject<Item> ENHANCED_ZOMBIE_EGG = ITEM_REGISTER.register("enhanced_zombie_egg",
            () -> new CustomSpawnEggItem(DyingLight.ENHANCED_ZOMBIE, 1, 2,
                    new Item.Properties().tab(ItemGroup.TAB_MISC)));

    @CapabilityInject(SkillContainer.class)
    public static Capability<SkillContainer> CapabilitySkillContainer = null;

    @SubscribeEvent
    public void onCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(DyingLight.MODID, "skill"),
                    new SkillContainer(((PlayerEntity) event.getObject())));
        }
    }

    public DyingLight() {
        CHANNEL.registerMessage(1, DoubleJumpMessage.class, DoubleJumpMessage::encode, DoubleJumpMessage::decode,
                DoubleJumpMessage::handle);
        CHANNEL.registerMessage(2, SkillLevelUpMessage.class, SkillLevelUpMessage::encode, SkillLevelUpMessage::decode,
                SkillLevelUpMessage::handle);
        CHANNEL.registerMessage(3, SkillStatusMessage.class, SkillStatusMessage::encode, SkillStatusMessage::decode,
                SkillStatusMessage::handle);
        CHANNEL.registerMessage(4, WallClimbMessage.class, WallClimbMessage::encode, WallClimbMessage::decode,
                WallClimbMessage::handle);
        CHANNEL.registerMessage(5, AquaManMessage.class,AquaManMessage::encode,AquaManMessage::decode,
                AquaManMessage::handle);

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

        MinecraftForge.EVENT_BUS.register(this);

        ITEM_REGISTER.register(bus);
        EFFECT_REGISTER.register(bus);
        ENTITY_REGISTER.register(bus);
        SKILL_REGISTER.register(bus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(DyingLight.ENHANCED_ZOMBIE.get(),
                    EnhancedZombieEntity.func_234342_eQ_().build());
        });
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        skillKey = new KeyBinding("skill_key", KeyEvent.VK_K, DyingLight.MODID);

        ClientRegistry.registerKeyBinding(skillKey);
        ClientRegistry.registerKeyBinding(SkillTypeWallClimb.vKey);
        RenderingRegistry.registerEntityRenderingHandler(DyingLight.ENHANCED_ZOMBIE.get(),
                manager -> new EnhancedZombieRenderer(manager, new EnhancedZombieModel<>(), 0.8f));
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
        LOGGER.info("Got IMC {}",
                event.getIMCStream().map(m -> m.getMessageSupplier().get()).collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            return;

        while (skillKey.consumeClick()) {
            Minecraft minecraft = Minecraft.getInstance();
            ClientPlayerEntity player = minecraft.player;
            SkillContainer skillContainer = player.getCapability(CapabilitySkillContainer)
                    .orElseThrow(() -> new RuntimeException());
            minecraft.setScreen(new SkillScreen(skillContainer));
        }
    }


    @SubscribeEvent
    public void onRegisterEvent(final RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        dispatcher.register((
                Commands.literal(DyingLight.MODID)
                        .requires((s) -> s.hasPermission(0))
                        .then(Commands.argument("level", IntegerArgumentType.integer(0, 3))
                                .executes((ctx) -> {
                                    ServerPlayerEntity player = ctx.getSource().getPlayerOrException();
                                    int level = IntegerArgumentType.getInteger(ctx, "level");
                                    player.getCapability(CapabilitySkillContainer).ifPresent(c -> {
                                        for (Skill skill : c.getSkills()) {
                                            skill.setLevel(level);
                                        }
                                    });
                                    return 0;
                                })
                        )

        ));
    }


    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
//        TODO: not work in 1.17 see https://forums.minecraftforge.net/topic/103197-1171-player-capabilities-are-invalidated-on-death-before-they-can-be-copied/
            event.getOriginal().getCapability(CapabilitySkillContainer).ifPresent(oldStore -> {
                event.getEntity().getCapability(CapabilitySkillContainer).ifPresent(newStore -> {
                    CompoundNBT compoundNBT = oldStore.serializeNBT();
                    newStore.deserializeNBT(compoundNBT);
                });
            });
    }


    // You can use EventBusSubscriber to automatically subscribe events on the
    // contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }

        @SubscribeEvent
        public static void onSkillRegistry(final RegistryEvent.Register<SkillType> skillTypeRegister) {
            skillTypeRegister.getRegistry().registerAll(
                    SkillTypeGastrosoph.INSTANCE.setRegistryName("gastrosoph"),
                    SkillTypeLuck.INSTANCE.setRegistryName("luck"),
                    SkillTypeLumberman.Instance.setRegistryName("lumberman"),
                    SkillTypeMandom.INSTANCE.setRegistryName("mandom"),
                    SkillTypeMender.INSTANCE.setRegistryName("mender"),
                    SkillTypeMiner.Instance.setRegistryName("miner"),
                    SkillTypePotionMaster.INSTANCE.setRegistryName("potionmaster"),
                    SkillTypeRegen.INSTANCE.setRegistryName("regen"),
                    SkillTypeSmeltingMaster.INSTANCE.setRegistryName("smeltingmaster"),
                    SkillTypeTireless.INSTANCE.setRegistryName("tireless"),
                    SkillTypeToolMaster.Instance.setRegistryName("toolmaster"),
                    SkillTypeCamouflage.INSTANCE.setRegistryName("camouflage"),
                    SkillTypeChargeShooting.INSTANCE.setRegistryName("chargeshooting"),
                    SkillTypeDamageBlock.INSTANCE.setRegistryName("damageblock"),
                    SkillTypeDeathDenied.INSTANCE.setRegistryName("deathdenied"),
                    SkillTypeKnockBackResist.INSTANCE.setRegistryName("knockbackresist"),
                    SkillTypeMarksmanship.INSTANCE.setRegistryName("marksmanship"),
                    SkillTypeProjectileDeflection.INSTANCE.setRegistryName("projectiledeflection"),
                    SkillTypeWeaponMaster.INSTANCE.setRegistryName("weaponmaster"),
                    SkillTypeAquaMan.INSTANCE.setRegistryName("aquaman"),
                    SkillTypeDoubleJump.INSTANCE.setRegistryName("doublejump"),
                    SkillTypeRunner.INSTANCE.setRegistryName("runner"),
                    SkillTypePlunder.INSTANCE.setRegistryName("plunder"),
                    SkillTypeWallClimb.INSTANCE.setRegistryName("wallclimb"),
                    SkillTypeStrongLegs.INSTANCE.setRegistryName("stronglegs"),
                    SkillTypeBoneCrusher.INSTANCE.setRegistryName("bonecrusher"),
                    SkillTypeLethalPunch.INSTANCE.setRegistryName("lethalpunch")
            );
        }
    }
}
