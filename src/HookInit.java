package bilibili.helper.underplayer;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import android.view.View;
import android.view.ViewGroup;

/**
 * Minimal, reversible test hook for the current Bilibili UnderPlayer ad model.
 * It is intentionally scoped to tv.danmaku.bili in handleLoadPackage().
 */
public final class HookInit implements IXposedHookLoadPackage {
    private static volatile boolean detailActivityActive;

    private static String resourceName(View view) {
        try {
            int id = view.getId();
            return id == View.NO_ID ? "" : view.getResources().getResourceEntryName(id);
        } catch (Throwable ignored) {
            return "";
        }
    }

    private static void collapse(View view) {
        view.setVisibility(View.GONE);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null) {
            params.height = 0;
            view.setLayoutParams(params);
        }
        view.requestLayout();
    }

    private static boolean isDetailActivity(View view) {
        if (!detailActivityActive) return false;
        Context context = view.getContext();
        for (int i = 0; i < 8 && context != null; i++) {
            if (context instanceof Activity) {
                return context.getClass().getName().contains("UnitedBizDetailsActivity");
            }
            if (context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
            } else {
                break;
            }
        }
        return false;
    }

    private static boolean collapseAdAncestors(View adView) {
        View current = adView;
        for (int i = 0; i < 12; i++) {
            Object parentObject = current.getParent();
            if (!(parentObject instanceof View)) return false;
            View parent = (View) parentObject;
            String parentName = resourceName(parent);
            if ("underplayer_container".equals(parentName)) {
                collapse(parent);
                XposedBridge.log("BAuxiliary UnderPlayer test: collapsed underplayer_container");
                return true;
            }
            String className = parent.getClass().getName();
            if ("recycler".equals(parentName) || className.contains("RecyclerView")) {
                collapse(current);
                XposedBridge.log("BAuxiliary UnderPlayer test: collapsed recommendation ad item");
                return true;
            }
            current = parent;
        }
        return false;
    }

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (!"tv.danmaku.bili".equals(lpparam.packageName)) {
            return;
        }

        try {
            XposedHelpers.findAndHookMethod(
                    Activity.class,
                    "onResume",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            detailActivityActive = param.thisObject.getClass().getName()
                                    .contains("UnitedBizDetailsActivity");
                        }
                    });
            XposedHelpers.findAndHookMethod(
                    Activity.class,
                    "onPause",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            if (param.thisObject.getClass().getName()
                                    .contains("UnitedBizDetailsActivity")) {
                                detailActivityActive = false;
                            }
                        }
                    });
            XposedHelpers.findAndHookMethod(
                  View.class,
                  "onAttachedToWindow",
                  new XC_MethodHook() {
                      @Override
                      protected void afterHookedMethod(MethodHookParam param) {
                          View view = (View) param.thisObject;
                          try {
                              if (!isDetailActivity(view)) return;
                              String name = resourceName(view);
                              if ("ad_tint_frame".equals(name)) {
                                  if (collapseAdAncestors(view)) {
                                      view.setVisibility(View.GONE);
                                      XposedBridge.log("BAuxiliary UnderPlayer test: hid ad_tint_frame");
                                  }
                              }
                          } catch (Throwable ignored) { }
                      }
                  });
            XposedBridge.log("BAuxiliary UnderPlayer test: AdUnderPlayer hooks installed");
        } catch (Throwable t) {
            XposedBridge.log("BAuxiliary UnderPlayer test: hook failed: " + t);
        }
    }
}
