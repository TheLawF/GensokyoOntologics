package github.thelawf.gensokyoontology.common.dimensions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.thelawf.gensokyoontology.common.dimensions.world.biome.GSKOBiomesProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class GSKOChunkGenerator extends ChunkGenerator {

    private final Settings settings;

    public GSKOChunkGenerator(Registry<Biome> registry, Settings settings) {
        super(new GSKOBiomesProvider(registry), new DimensionStructuresSettings(false));
        this.settings = settings;
    }

    public static final Codec<Settings> SETTINGS_CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                Codec.INT.fieldOf("base").forGetter(Settings::getBaseHeight),
                Codec.FLOAT.fieldOf("verticalvariance").forGetter(Settings::getVerticalVariance),
                Codec.FLOAT.fieldOf("horizontalvariance").forGetter(Settings::getHorizontalVariance)
        ).apply(instance, Settings::new));

    public static final Codec<GSKOChunkGenerator> CHUNK_GEN_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RegistryLookupCodec.getLookUpCodec(Registry.BIOME_KEY).forGetter(GSKOChunkGenerator::getBiomeRegistry),
                    SETTINGS_CODEC.fieldOf("settings").forGetter(GSKOChunkGenerator::getSettings)
            ).apply(instance, GSKOChunkGenerator::new));

    public Registry<Biome> getBiomeRegistry() {
        return ((GSKOBiomesProvider) biomeProvider).getBiomeRegistry();
    }

    public Settings getSettings() {
        return this.settings;
    }

    @Override
    @Nonnull
    protected Codec<? extends ChunkGenerator> func_230347_a_() {
        return CHUNK_GEN_CODEC;
    }

    @Override
    @Nonnull
    public ChunkGenerator func_230349_a_(long p_230349_1_) {
        return new GSKOChunkGenerator(getBiomeRegistry(), settings);
    }

    @Override
    public void generateSurface(@NotNull WorldGenRegion region, @NotNull IChunk chunk) {
        BlockState bedrock = Blocks.BEDROCK.getDefaultState();
        ChunkPos chunkPos = chunk.getPos();

        int x,z;
        BlockPos.Mutable positions = new BlockPos.Mutable();

        for (x = 0; x < 16; x++) {
            for (z = 0; z < 16; z++) {
                chunk.setBlockState(positions.add(x, 0, z), bedrock, true);
            }
        }
    }

    @Override
    public void func_230352_b_(@NotNull IWorld p_230352_1_, @NotNull StructureManager p_230352_2_, @NotNull IChunk p_230352_3_) {

    }

    @Override
    public int getHeight(int x, int z, Heightmap.@NotNull Type heightmapType) {
        return 0;
    }

    @Override
    @Nonnull
    public IBlockReader func_230348_a_(int p_230348_1_, int p_230348_2_) {
        return new Blockreader(new BlockState[0]);
    }

    private static class Settings {
        private final int baseHeight;
        private final float verticalVariance;
        private final float horizontalVariance;

        public Settings(int baseHeight, float verticalVariance, float horizontalVariance) {
            this.baseHeight = baseHeight;
            this.verticalVariance = verticalVariance;
            this.horizontalVariance = horizontalVariance;
        }

        public float getVerticalVariance() {
            return verticalVariance;
        }

        public int getBaseHeight() {
            return baseHeight;
        }

        public float getHorizontalVariance() {
            return horizontalVariance;
        }
    }
}