package net.scottnotfound.merculab.proxy;

// imports
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.scottnotfound.merculab.Config;
import net.scottnotfound.merculab.MercuLab;
import net.scottnotfound.merculab.block.GuiProxy;
import net.scottnotfound.merculab.block.TestBlock;
import net.scottnotfound.merculab.block.TestContainerBlock;
import net.scottnotfound.merculab.block.TestContainerTileEntity;
import net.scottnotfound.merculab.item.TestItem;

import java.io.File;

import static net.scottnotfound.merculab.MercuLab.instance;
import static net.scottnotfound.merculab.MercuLabBlocks.*;


@Mod.EventBusSubscriber
public class CommonProxy {

    public static Configuration config;

    public void preInit(FMLPreInitializationEvent event) {
        File directory = event.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "scottnotfound.cfg"));
        Config.readConfig();
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiProxy());
    }

    public void postInit(FMLPostInitializationEvent event) {
        if (config.hasChanged()) {
            config.save();
        }
    }

    public void serverLoad(FMLServerStartingEvent event) {

    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new TestBlock());
        event.getRegistry().register(new TestContainerBlock());
        GameRegistry.registerTileEntity(TestContainerTileEntity.class, MercuLab.MODID + "_testcontainerblock");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new TestItem());
        event.getRegistry().register(new ItemBlock(testBlock).setRegistryName(testBlock.getRegistryName()));
        event.getRegistry().register(new ItemBlock(testContainerBlock).setRegistryName(testContainerBlock.getRegistryName()));
    }
}
