# Better Bilibili

这是一个面向已 Root Android 手机的 Bilibili 广告屏蔽测试发布包。

## 内容

- `module/BAuxiliary-UnderPlayer-Test.apk`：LSPosed 模块。它会在 Bilibili 视频详情页隐藏 UnderPlayer 广告，并收缩因广告过滤产生的推荐广告空壳；首页视频流不启用这套处理。
- `src/HookInit.java`：模块源码。

## 安装

1. 先自行安装 Bilibili APK。
2. 在 LSPosed 中安装 `BAuxiliary-UnderPlayer-Test.apk`。
3. 启用模块，并只勾选 `tv.danmaku.bili`。
4. 强制停止并重新打开 Bilibili。

本模块没有独立启动界面；安装后需要从 LSPosed 管理器启用。

## 注意

- 这是针对 Bilibili 9.7.0 / Android 16 测试机验证的模块，后续版本可能需要重新适配。
- 本项目不修改或绕过账号权限、付费内容或视频版权限制。
