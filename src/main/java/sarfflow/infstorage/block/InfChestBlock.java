package sarfflow.infstorage.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfChestBlock extends Block implements BlockEntityProvider {
    public InfChestBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new InfChestBlockEntity(pos, state);
    }

//    @Override
//    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
//        if (world.isClient) return ActionResult.SUCCESS;
//        Inventory blockEntity = (Inventory) world.getBlockEntity(blockPos);
//        if (blockEntity instanceof InfChestBlockEntity infChestBlockEntity) {
//            System.out.println("infChestBlockEntity!");
//        }
//        System.out.println("Click!");
//
//        return ActionResult.SUCCESS;
//    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof InfChestBlockEntity infChestBlockEntity) {
            if (!world.isClient) {
                if (!infChestBlockEntity.isEmpty() || !player.isCreative()) {
                    ItemStack itemStack = new ItemStack(this);
                    blockEntity.setStackNbt(itemStack);
                    ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, itemStack);
                    itemEntity.setToDefaultPickupDelay();
                    world.spawnEntity(itemEntity);
                }
            }
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) return;
        NbtList nbtList = nbt.getCompound("BlockEntityTag").getList("Items", 10);
        tooltip.add(Text.translatable("block.inf_storage.inf_chest.tooltip", nbtList.size()).formatted(Formatting.LIGHT_PURPLE));
    }
}
