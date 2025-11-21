## 1. 技术选型

### 编程语言

* **Java（Android 官方主力语言之一）**

  * 理由：API 完整、生态成熟，且你目前在学习 Java，开发体验更友好。

### 开发框架 / 依赖

* **Android Jetpack (AppCompat / Activity / Lifecycle)**

  * 理由：官方维护，兼容性强，Activity Result API 更安全。

* **Material Components**

  * 理由：提供更现代的 Material3 UI 组件，按钮、输入框、BottomSheet 等更易使用。

### 数据库 / 存储

* **SQLite + 自定义 SQLiteOpenHelper**

  * 理由：数据结构清晰（Todo/Note 各一张表），本地持久化稳定可靠，无需后端。
* **SharedPreferences（轻量设置存储）**

  * 理由：保存排序方式、是否显示描述等用户偏好设置。

### 其他技术

* **Android Speech Recognizer（语音识别）**

  * 理由：无需第三方 SDK，系统内置，适配能力强。
* **RecyclerView + Adapter**

  * 理由：标准列表组件，性能好，可扩展（筛选、排序、批量操作）。

---

## 2. 项目结构设计

### 整体架构

这是一个本地单用户 Android App，由三层构成：

* **数据层**：SQLite 数据库存储 todo 和 note
* **业务逻辑层**：Activity + DAO（增删改查、排序、校验）
* **展示层**：RecyclerView 列表、EditText 表单、Button 控件

结构简洁，所有功能本地运行，无需网络。

---

### 目录结构

```
app/src/main/java/com/zxuan/todolist/
├── db/
│   ├── TodoNoteDatabaseHelper.java   # SQLiteOpenHelper，管理两个表
│   ├── TodoDao.java                  # Todo 的增删改查
│   └── NoteDao.java                  # Note 的增删改查
│
├── model/
│   ├── Todo.java                     # 待办实体类
│   └── Note.java                     # 笔记实体类
│
├── ui/
│   ├── MainActivity.java             # 首页切换 Todo / Note
│   ├── AddEditTodoActivity.java      # 添加/编辑 Todo（语音输入）
│   ├── AddEditNoteActivity.java      # 添加/编辑 Note（语音输入）
│   ├── adapter/
│   │   ├── TodoAdapter.java
│   │   └── NoteAdapter.java
│   └── dialog/
│       └── DeleteConfirmDialog.java  # 通用删除对话框
│
└── utils/
    ├── DateUtils.java                # 日期格式、时间戳转换
    └── SpeechUtils.java              # 语音识别工具（封装 intent）
```

---

### 模块职责说明

* **db/**
  管理数据库生命周期，提供增删改查方法。

* **model/**
  统一数据结构，便于管理字段和扩展属性（如优先级、是否完成等）。

* **ui/**
  UI 展示、表单输入、交互逻辑处理（语音识别、跳转、选择日期）。

* **adapter/**
  RecyclerView 渲染每一行的数据，负责勾选完成状态、点击跳转编辑等。

* **utils/**
  工具类，通用逻辑与 UI 解耦。

---

## 3. 需求细节与决策

### 1) Todo 描述是否必填？

* **决策**：可选
* **原因**：移动端输入不方便，标题即可表达任务核心内容。
* **实现**：

  * UI 默认隐藏描述栏
  * 用户点击“添加描述”展开输入框
  * SQLite 中字段允许 null

---

### 2) Todo 状态如何显示？

* **UI 显示**：

  * 已完成的任务会变成灰色
  * 文字加删除线
  * 勾选框 checked = true

* **排序逻辑**：

  * 所有排序方式下，**未完成任务排前面**
  * 用户体验更符合真实习惯

---

### 3) 排序逻辑设计

支持以下排序方式：

| 排序方式  | 规则          |
| ----- | ----------- |
| 按创建时间 | 最新 → 最旧     |
| 按截止日期 | 最近截止 → 最晚截止 |
| 按优先级  | 高 > 中 > 低   |
| 按是否完成 | 未完成优先       |

**实现方法**：DAO 层返回列表后执行排序，避免频繁 SQL 查询。

---

### 4) 语音识别功能

#### 设计思路

添加任务时可能需要快速记录内容，因此加入语言转文本。

#### 实现细节

* 使用 `RecognizerIntent.ACTION_RECOGNIZE_SPEECH`
* 调用 `ActivityResultLauncher` 获取返回值
* 权限请求使用 `ActivityResultContracts.RequestPermission`
* 支持识别：

  * 标题
  * 描述
  * 笔记正文

#### 错误处理包括：

* 设备不支持语音识别
* 用户未授权麦克风
* 得到 null 识别结果
* Google 语音服务不可用

---

### 5) 数据库设计

#### 表结构：Todo

```
id INTEGER PRIMARY KEY AUTOINCREMENT
title TEXT NOT NULL
description TEXT
priority INTEGER
deadline INTEGER
completed INTEGER
created_at INTEGER NOT NULL
```

#### 表结构：Note

```
id INTEGER PRIMARY KEY AUTOINCREMENT
title TEXT NOT NULL
body TEXT
created_at INTEGER NOT NULL
updated_at INTEGER
```

设计尽量保持兼容性与后续扩展性（如添加标签、收藏、提醒功能）。

---

## 4. AI 使用说明

### 使用的 AI 工具

* **Cursor**（辅助搭建安卓开发框架）
* **ChatGPT**（负责关键逻辑、语音识别、崩溃排查）

---

### 使用 AI 的主要场景

#### 1. 语音识别崩溃排查

出现的问题：

* 点击语音按钮直接闪退
* 识别结果始终为 null
* 某些手机提示“不支持语音输入”

AI 给出的排查路线：

1. 是否正确注册 ActivityResultLauncher
2. 是否在 onCreate() 里初始化
3. 权限是否写进 Manifest
4. intent 的 EXTRA_LANGUAGE_MODEL 是否写错
5. 是否需要检查设备支持（最终确定需添加）

**最终修复方法**：
添加设备支持检测 + 完整语音启动逻辑

---

#### 2. RecyclerView 数据更新问题（不刷新的情况）

AI 帮助定位问题：submitList 使用的列表未 copy，导致 DiffUtil 不执行。

**最终修复**：
改为使用新的 ArrayList(submittedList)

---

### 3. AI 输出需要人为修改的地方

最大的问题就是依赖下载太慢了，AI 提供的镜像配置丰富都行不通，最后还是我自己去找的对应版本的腾讯云链接解决了这个问题。

AI 给的 Android 语音识别代码中：

* 某些手机上不支持指定语言 → 删除多余配置
* AI 给的权限请求代码已过时 → 改为 ActivityResult API
* 部分代码会造成空指针 → 统一检查 null

---

### 测试过的环境

| 设备               | Android 版本 | 语音识别      |
| ---------------- | ---------- | --------- |
| 小米 11            | Android 13 | ✔ 支持      |
| 华为 Mate 40       | Android 11 | ✔ 支持      |
| 红米 Note 系列       | Android 10 | ✔ 支持      |
| 模拟器（Google APIs） | Android 14 | ✔ 支持      |
| 模拟器（非 Google）    | Android 14 | ❌ 不支持（正常） |

---

### 已知问题

1. 部分国产 ROM（无 Google 服务）语音识别可用性不稳定
2. 暂未实现数据导入导出
3. Note 编辑界面暂不支持富文本
4. 不支持云同步
5. 列表暂未加入拖拽排序

---

## 5. 总结与反思

### 改进方向

1. **数据同步**

   * 接入后端（SpringBoot）
   * 使用 SQLite + Room，减少模板代码

2. **功能升级**

   * 添加任务提醒（AlarmManager）
   * Note 增加 Markdown 支持
   * 添加任务标签 / 搜索功能

3. **用户体验**

   * 底部导航优化
   * 加入暗色主题
   * 列表支持滑动删除、归档

4. **代码质量**

   * 引入 ViewBinding / DataBinding
   * Adapter 使用 ListAdapter + DiffUtil 统一刷新
   * Activity 重构为 MVVM 架构（ViewModel + LiveData）

---

### 亮点实现

* **语音识别完整适配（权限 + 设备支持 + 异常处理）**
* **本地数据库稳定可靠**
* **多模块拆分，结构清晰**
* **RecyclerView 展示效率高**
* **UI 简洁，符合移动端使用习惯**

---
