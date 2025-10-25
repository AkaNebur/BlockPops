package com.theplumteam.blockentity;

import com.theplumteam.block.BoxBlock;
import com.theplumteam.block.PopBlockColor;
import com.theplumteam.figure.FigureType;
import com.theplumteam.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BoxBlockEntity extends BlockEntity implements GeoBlockEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(BoxBlockEntity.class);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation BOX_ANIMATION = RawAnimation.begin().thenLoop("animation.box_block.idle");
    private FigureType figureType = FigureType.DEFAULT;

    // Figure positioning - correct values found through testing
    private double figureOffsetX = -0.55;
    private double figureOffsetY = 0.0;
    private double figureOffsetZ = -0.40;
    private double figureScale = 1.0;

    public BoxBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.BOX_BLOCK.get(), pos, blockState);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // Controller for the box model animations
        controllers.add(new AnimationController<>(this, "box_controller", 0, state ->
            state.setAndContinue(BOX_ANIMATION)
        ));

        // Note: Figure animations are handled by the separate figure renderer
        // No controller needed here since we're using a separate GeoBlockRenderer for the figure
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public PopBlockColor getColor() {
        if (getBlockState().getBlock() instanceof BoxBlock boxBlock) {
            return boxBlock.getColor();
        }
        return PopBlockColor.ORIGINAL;
    }

    public FigureType getFigureType() {
        return figureType;
    }

    public void setFigureType(FigureType figureType) {
        this.figureType = figureType;
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    public double getFigureOffsetX() {
        return figureOffsetX;
    }

    public double getFigureOffsetY() {
        return figureOffsetY;
    }

    public double getFigureOffsetZ() {
        return figureOffsetZ;
    }

    public double getFigureScale() {
        return figureScale;
    }

    public void setFigureOffset(double x, double y, double z) {
        this.figureOffsetX = x;
        this.figureOffsetY = y;
        this.figureOffsetZ = z;
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    public void setFigureScale(double scale) {
        this.figureScale = scale;
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("FigureType", figureType.getSerializedName());
        tag.putDouble("FigureOffsetX", figureOffsetX);
        tag.putDouble("FigureOffsetY", figureOffsetY);
        tag.putDouble("FigureOffsetZ", figureOffsetZ);
        tag.putDouble("FigureScale", figureScale);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("FigureType")) {
            this.figureType = FigureType.fromString(tag.getString("FigureType"));
        }
        if (tag.contains("FigureOffsetX")) {
            this.figureOffsetX = tag.getDouble("FigureOffsetX");
        }
        if (tag.contains("FigureOffsetY")) {
            this.figureOffsetY = tag.getDouble("FigureOffsetY");
        }
        if (tag.contains("FigureOffsetZ")) {
            this.figureOffsetZ = tag.getDouble("FigureOffsetZ");
        }
        if (tag.contains("FigureScale")) {
            this.figureScale = tag.getDouble("FigureScale");
        }
    }

    // ===== CHUNK LOAD SYNCHRONIZATION =====
    // getUpdateTag() and handleUpdateTag() are used when chunks are loaded
    // NOTE: getUpdateTag() is ALSO used by getUpdatePacket() for real-time sync!
    // These ensure the client has the correct data when entering the area

    @Override
    public CompoundTag getUpdateTag() {
        // This is sent to the client when the chunk loads AND for real-time updates
        // ClientboundBlockEntityDataPacket.create(this) internally calls this method
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        // This is received on the client during chunk load
        load(tag);
    }

    // ===== REAL-TIME SYNCHRONIZATION =====
    // getUpdatePacket() and onDataPacket() are used for real-time updates
    // These are triggered by level.sendBlockUpdated() and deliver changes immediately

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        // Creates a packet for real-time synchronization
        // Called on the server when level.sendBlockUpdated() is invoked
        // ClientboundBlockEntityDataPacket.create(this) calls getUpdateTag() to get the data
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        // Receives the packet on the client for real-time updates
        // This is what actually makes the changes appear immediately
        CompoundTag tag = packet.getTag();
        if (tag != null) {
            load(tag);
            // Request a render update so the changes are visible immediately
            if (level != null && level.isClientSide) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        if (level.isClientSide && blockEntity instanceof BoxBlockEntity boxBlockEntity) {
            // Animation ticking handled automatically by GeckoLib
        }
    }
}
