package github.thelawf.gensokyoontology.common.item.spell_card;

import github.thelawf.gensokyoontology.common.entity.projectile.DanmakuEntity;
import github.thelawf.gensokyoontology.common.libs.danmakulib.DanmakuType;
import github.thelawf.gensokyoontology.common.libs.danmakulib.TransformFunction;
import github.thelawf.gensokyoontology.common.libs.danmakulib.spellcard.ISpellCard;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;


public class NamiToTubuNoKyokai extends Item implements ISpellCard<DanmakuType> {
    // 传统艺能：境符「波与粒的境界」
    String spellName;

    public NamiToTubuNoKyokai(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(World worldIn, @NotNull PlayerEntity playerIn, @NotNull Hand handIn) {
        if (!worldIn.isRemote) {
            // 开花弹示例：
            TransformFunction tf = TransformFunction.Builder.create()
                    .setPlayer(playerIn).setInitLocation(playerIn.getPositionVec())
                    .setLifeSpan(120).setShootInterval(1).setExecuteTimes(5)
                    .setExecuteInterval(10).setResultantSpeed(0.75)
                    .setWorld(worldIn);

            for (int i = 0; i < tf.executeTimes; i++) {
                // Vector3d rotationVec = new Vector3d(10,0,0);
                // Vector3d pivot = func.translate(func.x, func.y, func.z);
                // func.rotate(func.initLocation, pivot, new Vector3d(0d, Math.PI / 12, 0d));

                tf.setInitLocation(new Vector3d(tf.initLocation.x + 0.9 * i,
                        tf.initLocation.y, tf.initLocation.z));
                for (int j = 0; j < tf.lifeSpan / tf.shootInterval; j++) {

                    DanmakuEntity danmaku = new DanmakuEntity(playerIn, worldIn);

                    if (j < tf.lifeSpan / 2) {
                        tf.setIncrement(tf.rotateTotal, Math.PI / 10);
                        tf.increaseYaw((float) tf.increment * j);
                        danmaku.setLocationAndAngles(tf.x, tf.y, tf.z,
                                (float) tf.yaw, (float) tf.pitch);
                        danmaku.setNoGravity(true);
                        danmaku.canBeCollidedWith();

                        Vector3d towards = playerIn.getLookVec();
                        danmaku.shoot(tf.speedV3.x, tf.speedV3.y, tf.speedV3.z,
                                (float) tf.resultantSpeed, 0.f);
                        worldIn.addEntity(danmaku);

                    }
                    else {
                        tf.setIncrement(tf.rotateTotal, Math.PI / 10);
                        tf.increaseYaw((float) -tf.increment * j);
                        danmaku.setLocationAndAngles(tf.x, tf.y, tf.z,
                                (float) -tf.yaw, (float) tf.pitch);
                        danmaku.setNoGravity(true);
                        danmaku.canBeCollidedWith();

                        Vector3d towards = playerIn.getLookVec();
                        danmaku.shoot(tf.speedV3.x, tf.speedV3.y, tf.speedV3.z,
                                (float) tf.resultantSpeed, 0.f);
                        worldIn.addEntity(danmaku);
                    }
                }
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }


    @Override
    public String getSpellName() {
        return spellName;
    }

    @Override
    public String setSpellName(String nameIn) {
        return nameIn;
    }
}
