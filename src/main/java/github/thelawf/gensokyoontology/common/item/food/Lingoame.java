package github.thelawf.gensokyoontology.common.item.food;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class Lingoame extends Item {
    private static final Food food = (new Food.Builder())
            .saturation(4)
            .hunger(6)
            .setAlwaysEdible()
            .build();
    public Lingoame() {
        super(new Properties().group(ItemGroup.FOOD).food(food));
    }
}