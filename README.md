# Better Bilibili

这是一个面向已 Root Android 手机的 Bilibili 功能增强模块，安装后在 LSPosed 中显示为 `Better Bilibili`。

## 内容

- `module/Better-Bilibili.apk`：LSPosed 模块。它包含当前版本的详情页 UnderPlayer 广告处理、推荐卡片空壳修复，以及 BAuxiliary 1.9.3 的兼容功能核心。
- `src/HookInit.java`：模块源码。

## 功能

- 隐藏视频图片/视频下方的 UnderPlayer 广告，并自动收缩广告留下的空白区域。
- 保持首页视频流卡片正常显示，不对首页普通视频封面做全局隐藏。
- 合并 BAuxiliary 原有的开屏广告、布局广告、竖屏界面、通知栏、图集解析和布局精简等功能入口；这些功能依赖 Bilibili 版本，若新版改动对应内部类，可能自动失效。

## 安装

1. 先自行安装 Bilibili APK。
2. 在 LSPosed 中安装 `Better-Bilibili.apk`。
3. 启用模块，并只勾选 `tv.danmaku.bili`（国服b站）。
4. 强制停止并重新打开 Bilibili。

本模块没有独立启动界面；安装后需要从 LSPosed 管理器启用。

## 注意

- 当前广告修复针对 Bilibili 9.7.0 / Android 16 测试机适配，后续版本可能需要重新适配。
- BAuxiliary 兼容核心来自原 BAuxiliary 1.9.3；本项目仅将其与当前广告修复合并，原作者信息保留在模块功能说明中。
- 本项目不修改或绕过账号权限、付费内容或视频版权限制。
