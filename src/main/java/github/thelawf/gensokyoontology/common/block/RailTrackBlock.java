package github.thelawf.gensokyoontology.common.block;

import github.thelawf.gensokyoontology.common.tileentity.RailTrackTileEntity;
import github.thelawf.gensokyoontology.common.util.AxisRotations;
import net.minecraft.advancements.criterion.NetherTravelTrigger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class RailTrackBlock extends DirectionalBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");
    // public static final EnumProperty<AxisRotations> ROTATION = EnumProperty.create("rotation", AxisRotations.class);
    //public static final EnumProperty ANCHORED_PLANE = EnumProperty.create("anchored_plane");

    private static final VoxelShape shape;
    public static final VoxelShape railHorizontal;
    public static final VoxelShape railVertical;

    static {
        VoxelShape railA = Block.makeCuboidShape(-3, 0, 0,0, 3, 16);
        VoxelShape railB = Block.makeCuboidShape(16, 0, 0,19, 3, 16);
        VoxelShape girder1 = Block.makeCuboidShape(0, 0, 3,16, 1, 6);
        VoxelShape girder2 = Block.makeCuboidShape(0, 0, 11,16, 1, 14);

        VoxelShape horizontal = Block.makeCuboidShape(-3,0,0,19, 3, 16);
        VoxelShape vertical = Block.makeCuboidShape(-3,0,0,19, 16, 0);

        shape = VoxelShapes.or(railA,railB,girder1,girder2);
        railHorizontal = VoxelShapes.or(horizontal);
        railVertical = VoxelShapes.or(vertical);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        /*
        this.setDefaultState(this.stateContainer.getBaseState().with(EAST,true));
        if (state.get(BlockStateProperties.EAST) || state.get(BlockStateProperties.WEST) ||
                state.get(BlockStateProperties.SOUTH) || state.get(BlockStateProperties.NORTH)) {
            return railVertical;
        }
        else {
            return railHorizontal;
        }
        */
        return railHorizontal;
    }

    public RailTrackBlock() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(3.f,25).notSolid());
        this.setDefaultState(this.stateContainer.getBaseState());

    }


    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new RailTrackTileEntity();
    }


    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer != null) {
            worldIn.setBlockState(pos, state.with(FACING, getStateFromEntity(pos, placer)));

        }
    }

    public Direction getStateFromEntity(BlockPos clickedPos, LivingEntity entity) {
        Vector3d vec = entity.getPositionVec();
        return Direction.getFacingFromVector((float) vec.x - clickedPos.getX(),
                (float) vec.y - clickedPos.getY(), (float) vec.z - clickedPos.getZ());
    }


    @SuppressWarnings("deprecation")
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite());
    }


    /*
    public static Direction getStateFromEntity(BlockPos clickedBlock, Entity player){
        return Direction.getFacingFromVector((float) (player.getPosX() - clickedBlock.getX()),
                (float) (player.getPosY() - clickedBlock.getY()), (float) (player.getPosZ() - clickedBlock.getZ()));
    }

     */

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
        // builder.add(ROTATION);
        builder.add(CONNECTED);
        super.fillStateContainer(builder);
    }
}
