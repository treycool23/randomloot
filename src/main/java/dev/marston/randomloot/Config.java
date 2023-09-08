package dev.marston.randomloot;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = RandomLootMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();



    private static final ForgeConfigSpec.DoubleValue CASE_CHANCE = BUILDER
            .comment("chance to find a case in a chest.")
            .defineInRange("caseChance", 0.25, 0.0, 1.0);

    

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double CaseChance;
  

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
    	CaseChance = CASE_CHANCE.get();

    
    }
}
