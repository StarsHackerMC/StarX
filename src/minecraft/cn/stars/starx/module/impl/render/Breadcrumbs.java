package cn.stars.starx.module.impl.render;

import cn.stars.starx.event.impl.PreMotionEvent;
import cn.stars.starx.event.impl.Render3DEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.util.render.RenderUtil;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "Breadcrumbs", description = "Shows a trail on your feet", category = Category.RENDER)
public final class Breadcrumbs extends Module {

    List<Vec3> path = new ArrayList<>();

    private final BoolValue timeoutBool = new BoolValue("Fade", this, true);
    private final NumberValue timeout = new NumberValue("Fade Time", this, 15, 1, 150, 0.1);

    @Override
    public void onUpdateAlwaysInGui() {
        timeout.hidden = !timeoutBool.isEnabled();
    }

    @Override
    protected void onEnable() {
        path.clear();
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {

        if (mc.thePlayer.lastTickPosX != mc.thePlayer.posX || mc.thePlayer.lastTickPosY != mc.thePlayer.posY || mc.thePlayer.lastTickPosZ != mc.thePlayer.posZ) {
            path.add(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
        }

        if (timeoutBool.isEnabled())
            while (path.size() > (int) timeout.getValue()) {
                path.remove(0);
            }
    }

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        RenderUtil.renderBreadCrumbs(path);
    }
}
